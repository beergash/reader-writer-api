package it.andrea.reader.writer.api.writer.interfaces;

import java.io.File;
import java.io.IOException;

import it.andrea.reader.writer.api.exception.FileWriterException;
import it.andrea.reader.writer.api.writer.model.ReportFeature;

public interface IReportWriter {

	public File writeReport(ReportFeature repFeature) throws IOException, FileWriterException;

}
