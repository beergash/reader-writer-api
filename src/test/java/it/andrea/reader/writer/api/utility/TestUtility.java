package it.andrea.reader.writer.api.utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.exception.FileWriterException;
import it.andrea.reader.writer.api.model.DataType;
import it.andrea.reader.writer.api.model.DateFormat;
import it.andrea.reader.writer.api.reader.interfaces.IFileRewriter;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileSheet;
import it.andrea.reader.writer.api.reader.model.FileTrace;
import it.andrea.reader.writer.api.reader.model.FileType;
import it.andrea.reader.writer.api.reader.model.Matcher;
import it.andrea.reader.writer.api.reader.model.MatchingOperator;
import it.andrea.reader.writer.api.writer.interfaces.IReportPreaparer;
import it.andrea.reader.writer.api.writer.model.ReportFeature;
import it.andrea.reader.writer.api.writer.model.ReportSheet;
import it.andrea.reader.writer.api.writer.model.ReportTrace;

public class TestUtility {

	private static final Logger log = LoggerFactory.getLogger(TestUtility.class);

	public static final String INPUT_TEST_FILES_FOLDER = "/input/";
	public static final String OUTPUT_TEST_FILES_FOLDER = System.getProperty("user.dir") + "/src/test/resources/output/";

	public static void getFieldPropertiesByHeader(String header, String separator, String prefix) {
		String[] fields = header.split(separator);
		for (int i = 0; i < fields.length; i++) {
			String fieldIndex = String.valueOf(i + 1);
			StringJoiner joiner = new StringJoiner(",");
			String propKey = prefix + ".sheet1.field" + fieldIndex + "=";
			joiner.add(fieldIndex);
			joiner.add(fields[i]);
			joiner.add(DataType.STRING.name().toLowerCase());
			joiner.add(" ");
			joiner.add(" ");
			joiner.add("");
			System.out.println(propKey + joiner.toString());
		}
	}

	public static ReportFeature getReportFeaturesByPropertiesFile(Properties props, String prefix) throws FileWriterException {
		List<ReportSheet> sheets = new ArrayList<ReportSheet>();
		ReportFeature rf = new ReportFeature();
		rf.setFilename(props.getProperty(prefix + ".filename"));
		String relativeDir = Optional.ofNullable(props.getProperty(prefix + ".dir")).orElse("");
		rf.setOutputDirectory(OUTPUT_TEST_FILES_FOLDER + relativeDir);
		if (props.getProperty(prefix + ".preparer") != null) {
			IReportPreaparer preparer = null;
			try {
				preparer = (IReportPreaparer) Class.forName(prefix + ".preparer").newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new FileWriterException("cannot instantiate preparer class: " + props.getProperty(prefix + ".preparer"), e);
			}
			rf.setPreparer(preparer);
		}
		SortedSet<String> configuredSheets = getDistinctProps(props, prefix + ".sheet", 1);
		configuredSheets.stream().forEach(s -> {
			String prefixSheet = prefix + "." + s + ".";
			SortedSet<String> configuredFieds = getDistinctProps(props, prefixSheet + "field", 2);
			ReportSheet sheet = new ReportSheet();
			sheet.setName(props.getProperty(prefixSheet + "name"));
			sheet.setHeader(props.getProperty(prefixSheet + "header"));
			sheet.setFooter(props.getProperty(prefixSheet + "footer"));
			sheet.setSeparator(props.getProperty(prefixSheet + "separator"));
			String typedDataClass = props.getProperty(prefixSheet + "typedDataClass");
			if (typedDataClass != null) {
				try {
					sheet.setTypedDataClass(Class.forName(typedDataClass));
				} catch (ClassNotFoundException e) {
					log.error("cannot instantiate target typed class: " + typedDataClass, e);
				}
			}
			List<ReportTrace> fields = new ArrayList<ReportTrace>();
			configuredFieds.stream().forEach(f -> {
				String prefixField = prefixSheet + f + ".";
				ReportTrace trace = new ReportTrace();
				trace.setPosition(Integer.valueOf(props.getProperty(prefixField + "position")));
				trace.setLabel(props.getProperty(prefixField + "label"));
				trace.setFieldName(props.getProperty(prefixField + "fieldName"));
				trace.setDataType(DataType.valueOf(props.getProperty(prefixField + "dataType").toUpperCase()));
				if (props.getProperty(prefixField + "fieldLength") != null) {
					trace.setFieldLength(Integer.valueOf(props.getProperty(prefixField + "fieldLength")));
				}
				trace.setFillerCharacter(props.getProperty(prefixField + "fillerCharacter"));
				trace.setPadType(props.getProperty(prefixField + "padType") == null ? null : props.getProperty(prefixField + "padType").toUpperCase());
				DateFormat inpFormat = props.getProperty(prefixField + "inputDateFormat") == null ? null : DateFormat.fromFormat(props.getProperty(prefixField + "inputDateFormat"));
				trace.setInputDateFormat(inpFormat);
				DateFormat outDateFormat = props.getProperty(prefixField + "outputDateFormat") == null ? null : DateFormat.fromFormat(props.getProperty(prefixField + "outputDateFormat"));
				trace.setOutputDateFormat(outDateFormat);
				trace.setNumberFormat(props.getProperty(prefixField + "numberFormat"));
				fields.add(trace);
				sheet.setTrace(fields);
			});
			sheets.add(sheet);
		});
		rf.setSheets(sheets);
		return rf;
	}

	private static SortedSet<String> getDistinctProps(Properties props, String prefix, int index) {
		SortedSet<String> configuredSheets = new TreeSet<String>();
		props.forEach((k, v) -> {
			if (String.valueOf(k).startsWith(prefix)) {
				configuredSheets.add(String.valueOf(k).split("\\.")[index]);
			}
		});
		return configuredSheets;
	}

	public static FileFeature getFileFeaturesByPropertiesFile(Properties props, String prefix) throws FileReaderException {
		List<FileSheet> fileSheets = new ArrayList<FileSheet>();
		FileFeature ff = new FileFeature(Boolean.valueOf(props.getProperty(prefix + ".header")), FileType.valueOf(props.getProperty(prefix + ".type").toUpperCase()));
		ff.setHasFooter(Boolean.valueOf(props.getProperty(prefix + ".footer")));
		ff.setDescription(props.getProperty(prefix + ".description"));
		String rewriterClassName = props.getProperty(prefix + ".rewriter.className");
		if (rewriterClassName != null) {
			IFileRewriter rewriterClass = null;
			try {
				rewriterClass = (IFileRewriter) Class.forName(rewriterClassName).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new FileReaderException("cannot instantiate rewriter class: " + rewriterClassName, e);
			}
			ff.setRewriterClass(rewriterClass);
		}
		SortedSet<String> configuredSheets = new TreeSet<String>();
		props.forEach((k, v) -> {
			if (String.valueOf(k).startsWith(prefix + ".sheet")) {
				configuredSheets.add(String.valueOf(k).split("\\.")[1]);
			}
		});
		configuredSheets.stream().forEach(s -> {
			String prefixSheet = prefix + "." + s + ".";
			FileSheet sheet = new FileSheet(props.getProperty(prefixSheet + "sheetName"));
			sheet.setHasHeader(Boolean.valueOf(props.getProperty(prefixSheet + "header")));
			sheet.setHasFooter(Boolean.valueOf(props.getProperty(prefixSheet + "footer")));
			sheet.setSeparator(props.getProperty(prefixSheet + "separator"));
			String targetClassName = props.getProperty(prefixSheet + "targetClassName");
			if (targetClassName != null) {
				try {
					sheet.setTargetClass(Class.forName(targetClassName));
				} catch (ClassNotFoundException e) {
					log.error("class " + targetClassName + " does not exist", e);
				}
			}
			List<FileTrace> fields = new ArrayList<FileTrace>();
			List<Matcher> validationConditions = new ArrayList<Matcher>();
			props.forEach((key, value) -> {
				FileTrace trace = new FileTrace();
				if (String.valueOf(key).startsWith(prefixSheet + "field")) {
					String[] attributes = String.valueOf(value).split(",");
					try {
						trace.setPosition(attributes[0]);
						trace.setLabel(attributes[1]);
						trace.setDataType(DataType.valueOf(attributes[2].toUpperCase()));
						trace.setDateFormat(attributes[3]);
						trace.setNumberFormat(attributes[4]);
						trace.setFieldJavaAttribute(attributes[5]);
					} catch (ArrayIndexOutOfBoundsException e) {
						log.warn("Attribute not set. Set default value", e.getCause());
					}
					fields.add(trace);
				}
				if (String.valueOf(key).startsWith(prefixSheet + "matcher")) {
					String[] attributes = String.valueOf(value).split(",");
					Matcher mtc = new Matcher(attributes[0], MatchingOperator.valueOf(attributes[1].toUpperCase()), attributes[2]);
					validationConditions.add(mtc);
				}
			});
			sheet.setFields(fields);
			sheet.setValidationConditions(validationConditions);
			fileSheets.add(sheet);
		});
		ff.setSheets(fileSheets);
		return ff;
	}

	public static Properties loadproperties(String filename) throws IOException {
		String workDir = System.getProperty("user.dir");
		Path fileLocation = Paths.get(workDir + "/src/test/resources/" + filename);
		try (InputStream stream = Files.newInputStream(fileLocation)) {
			Properties config = new Properties();
			config.load(stream);
			return config;
		}
	}

}
