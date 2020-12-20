package it.beergash.reader.writer.api.reader.model;

import java.util.ArrayList;

import java.util.List;

/**
 * Sheet configuration of a file. A single file can have more {@link FileSheet} configurations
 * @author Andrea Aresta
 *
 */
public class FileSheet {

	private String name;
	private String separator;
	private List<FileTrace> fields;
	private List<Matcher> validationConditions = new ArrayList<Matcher>();
	private boolean hasHeader;
	private boolean hasFooter;
	private Class<?> targetClass;

	public FileSheet() {
	}

	public FileSheet(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return delimiter of csv files
	 */
	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @return name identifier of the sheet
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return trace of the sheet
	 */
	public List<FileTrace> getFields() {
		return fields;
	}

	public void setFields(List<FileTrace> fields) {
		this.fields = fields;
	}

	/**
	 * 
	 * @return validation conditions to understand if the record read by file has to map in this sheet
	 */
	public List<Matcher> getValidationConditions() {
		return validationConditions;
	}

	public void setValidationConditions(List<Matcher> validationConditions) {
		this.validationConditions = validationConditions;
	}

	/**
	 * 
	 * @return header of the single sheet (for example excel sheets). If it's true, first sheet line is skipped
	 */
	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	/**
	 * 
	 * @return footer of the single sheet (for example excel sheets). If it's true, last sheet line is skipped
	 */
	public boolean isHasFooter() {
		return hasFooter;
	}

	public void setHasFooter(boolean hasFooter) {
		this.hasFooter = hasFooter;
	}
	
	/**
	 * @return class representing the target object of the record read
	 */
	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

}
