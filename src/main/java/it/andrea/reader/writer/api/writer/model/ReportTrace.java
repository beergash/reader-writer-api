package it.andrea.reader.writer.api.writer.model;

import it.andrea.reader.writer.api.model.DataType;
import it.andrea.reader.writer.api.model.DateFormat;

public class ReportTrace {

	private Integer position;
	private String label = "";
	private String fieldName;
	private Integer fieldLength;
	private String fillerCharacter;
	private String padType;
	private DataType dataType;
	private DateFormat inputDateFormat;
	private DateFormat outputDateFormat;
	private String numberFormat;

	/**
	 * @return column index of the field. For fixed position it's the start position
	 */
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * @return label of column
	 */
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return name identifier to retrieve data
	 */
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return field length for fixed position reports
	 */
	public Integer getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}

	/**
	 * @return filler character for fixed position reports
	 */
	public String getFillerCharacter() {
		return fillerCharacter;
	}

	public void setFillerCharacter(String fillerCharacter) {
		this.fillerCharacter = fillerCharacter;
	}

	/**
	 * @return pad type for fixed position reports. Only possible values are right,
	 *         left
	 */
	public String getPadType() {
		return padType;
	}

	public void setPadType(String padType) {
		this.padType = padType;
	}

	/**
	 * @return data type filed
	 */
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public DateFormat getInputDateFormat() {
		return inputDateFormat;
	}

	public void setInputDateFormat(DateFormat inputDateFormat) {
		this.inputDateFormat = inputDateFormat;
	}

	public DateFormat getOutputDateFormat() {
		return outputDateFormat;
	}

	public void setOutputDateFormat(DateFormat outputDateFormat) {
		this.outputDateFormat = outputDateFormat;
	}

	/**
	 * @return number format of field to display. It's considered only if data type
	 *         is NUMBER
	 */
	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

}
