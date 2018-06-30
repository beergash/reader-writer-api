package it.andrea.reader.writer.api.reader.model;

import it.andrea.reader.writer.api.model.DataType;

public class FileTrace {

	private String position;
	private DataType dataType;
	private String dateFormat;
	private String numberFormat;
	private String label;
	private String fieldJavaAttribute;

	public FileTrace() {
	}

	public FileTrace(String position, String label, DataType dataType) {
		super();
		this.position = position;
		this.label = label;
		this.dataType = dataType;
	}

	/**
	 * @return position of record. 
	 * It is a single position index for csv and excel sources, it's a range interval for fixed position files
	 */
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return data type
	 */
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return date format for date data type
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return number format for number data type
	 */

	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	/**
	 * @return label name of the column
	 */
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * the java attribute name of the object class to save record
	 * @return
	 */
	public String getFieldJavaAttribute() {
		return fieldJavaAttribute;
	}

	public void setFieldJavaAttribute(String fieldJavaAttribute) {
		this.fieldJavaAttribute = fieldJavaAttribute;
	}

}