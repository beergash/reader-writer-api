package it.andrea.reader.writer.api.writer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.andrea.reader.writer.api.writer.impl.excel.ExcelColumn;

/**
 * @author Andrea Aresta
 */
public class ReportSheet {

	private String name;
	private String header;
	private String footer;
	private String separator;
	private List<Map<String, Object>> data;
	private List<ReportTrace> trace;
	private Map<String, ExcelColumn> excelColumnProps = new HashMap<String, ExcelColumn>();

	public ReportSheet() {
		super();
	}

	public ReportSheet(String name, List<Map<String, Object>> data) {
		super();
		this.name = name;
		this.data = data;
	}

	/**
	 * @return name of the sheet
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return header of the sheet
	 */
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return footer of the sheet
	 */
	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return separator character for text reports
	 */
	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @return report data
	 */
	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	/**
	 * @return trace of sheet
	 */
	public List<ReportTrace> getTrace() {
		return trace;
	}

	public void setTrace(List<ReportTrace> trace) {
		this.trace = trace;
	}

	public Map<String, ExcelColumn> getExcelColumnProps() {
		return excelColumnProps;
	}

	public void setExcelColumnProps(Map<String, ExcelColumn> excelColumnProps) {
		this.excelColumnProps = excelColumnProps;
	}
}
