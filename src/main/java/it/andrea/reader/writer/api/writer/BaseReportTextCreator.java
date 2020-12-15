package it.andrea.reader.writer.api.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.andrea.reader.writer.api.exception.FileWriterException;
import it.andrea.reader.writer.api.writer.interfaces.IReportPreaparer;
import it.andrea.reader.writer.api.writer.interfaces.IReportWriter;
import it.andrea.reader.writer.api.writer.model.DefaultReportPreparer;
import it.andrea.reader.writer.api.writer.model.ReportFeature;
import it.andrea.reader.writer.api.writer.model.ReportSheet;

public abstract class BaseReportTextCreator implements IReportWriter {

	private static final Logger log = LoggerFactory.getLogger(BaseReportTextCreator.class);

	public File writeReport(ReportFeature repFeature) throws IOException, FileWriterException {
		log.debug("Creating report: " + repFeature.getFilename());
		IReportPreaparer preparer = Optional.ofNullable(repFeature.getPreparer()).orElse(new DefaultReportPreparer());
		String filename = preparer.generateFileName(repFeature);
		String fullFilename = repFeature.getOutputDirectory() + File.separator + filename;
		List<ReportSheet> sheet = repFeature.getSheets();
		List<String> sheetFileData = new LinkedList<String>();
		for (ReportSheet reportTextSheet : sheet) {
			sheetFileData.addAll(createLines(reportTextSheet, preparer));
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilename));
		for (int i=0; i<sheetFileData.size(); i++) {
			writer.write(sheetFileData.get(i));
			if (i != sheetFileData.size() -1) {
			writer.newLine();
			}
		}
		writer.flush();
		writer.close();
		log.debug("created file: " + fullFilename);
		return new File(fullFilename);
	}

	protected abstract LinkedList<String> createLines(ReportSheet sheet, IReportPreaparer preparer) throws FileWriterException;

}
