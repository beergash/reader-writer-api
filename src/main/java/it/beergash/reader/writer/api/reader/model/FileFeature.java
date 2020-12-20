package it.beergash.reader.writer.api.reader.model;

import java.util.List;

import it.beergash.reader.writer.api.reader.interfaces.IFileRewriter;

/**
 * Represent the features of a file to read. Reader API needs this info to
 * understand to read and map values from file
 * 
 * @author Andrea Aresta
 *
 */
public class FileFeature {

	private String description;
	private String type;
	private boolean hasHeader;
	private boolean hasFooter;
	private List<FileSheet> sheets;
	private IFileRewriter rewriterClass;

	public FileFeature() {
	}

	public FileFeature(boolean hasHeader, String type) {
		super();
		this.hasHeader = hasHeader;
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * file type
	 * @return
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * indicates if a file has a header line. If true first row is discarded
	 */
	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	/**
	 * indicates if a file has a footer line. If last first row is discarded
	 */
	public boolean isHasFooter() {
		return hasFooter;
	}

	public void setHasFooter(boolean hasFooter) {
		this.hasFooter = hasFooter;
	}

	/**
	 * indicates all sheets of a file. Example: excel with many sheets, or text
	 * fixed position file with different configuration
	 * 
	 * @return
	 */
	public List<FileSheet> getSheets() {
		return sheets;
	}

	public void setSheets(List<FileSheet> sheets) {
		this.sheets = sheets;
	}

	/**
	 * 
	 * @return rewriter class invoked right before operating reader
	 */
	public IFileRewriter getRewriterClass() {
		return rewriterClass;
	}

	public void setRewriterClass(IFileRewriter rewriterClass) {
		this.rewriterClass = rewriterClass;
	}

}
