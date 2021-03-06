package it.beergash.reader.writer.api.writer.model;

import java.util.List;

import it.beergash.reader.writer.api.writer.interfaces.IReportPreaparer;

public class ReportFeature {

	private String filename;
	private String outputDirectory;
	private List<ReportSheet> sheets;
	private IReportPreaparer preparer;
	private boolean appendMode = false;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public List<ReportSheet> getSheets() {
		return sheets;
	}

	public void setSheets(List<ReportSheet> sheets) {
		this.sheets = sheets;
	}

	public IReportPreaparer getPreparer() {
		return preparer;
	}

	public void setPreparer(IReportPreaparer preparer) {
		this.preparer = preparer;
	}

	public boolean isAppendMode() {
		return appendMode;
	}
	public void setAppendMode(boolean appendMode) {
		this.appendMode = appendMode;
	}
}
