package it.andrea.reader.writer.api.reader.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.reader.FileLoaderUtils;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileResult;
import it.andrea.reader.writer.api.reader.model.FileSheet;
import it.andrea.reader.writer.api.reader.model.FileTrace;
import it.andrea.reader.writer.api.reader.model.Matcher;

/**
 * Reads Csv files and gets result into {@link FileResult}
 * 
 * @author Andrea Aresta
 */
@Service
@Qualifier("csv")
public class CsvFileReader extends TextFileReader {

	@Autowired
	private FileLoaderUtils loaderUtils;

	protected void readLine(FileFeature fileFeature, String line, Map<String, FileResult> result, String header) throws FileReaderException {
		for (FileSheet s : fileFeature.getSheets()) {
			Object targetObject = null;
			try {
				targetObject = s.getTargetClass() == null ? null : s.getTargetClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new FileReaderException("cannot instantiate class " + s.getTargetClass(), e);
			}
			List<FileTrace> fields = (s.getFields() == null || s.getFields().size() == 0) ? getDeafultTrace(header, s.getSeparator()) : s.getFields();
			Map<String, Object> row = new TreeMap<String, Object>();
			String[] lineSplitted = line.split(s.getSeparator());
			if (isRecordValidForSheet(lineSplitted, s.getValidationConditions())) {
				for (FileTrace f : fields) {
					Object value = loaderUtils.convertValue(lineSplitted[Integer.valueOf(f.getPosition()) - 1].trim(), f);
					buildData(targetObject, row, f, value);
				}
				loaderUtils.managesResult(result, s, row, targetObject);
			}
		}
	}

	private boolean isRecordValidForSheet(String[] lineSplitted, List<Matcher> validationConditions) {
		return validationConditions.stream().allMatch(c -> loaderUtils.isRecordValid(lineSplitted[Integer.valueOf(c.getPosition()) - 1], c));
	}

}
