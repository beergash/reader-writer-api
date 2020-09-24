package it.andrea.reader.writer.api.reader.model;

import java.util.List;
import java.util.Map;

/**
 * Contains all reault data read by source file
 * @author Andrea Aresta
 *
 */
public class FileResult {

	private List<Map<String, Object>> data;
	private List<? extends Object> mappedData;

	/**
	 * @return list of records read by file. Key of map is the name of label indicating the column and the object the value
	 */
	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

    /**
     * list of object representing the records
     * @return
     */
	public List<? extends Object> getMappedData() {
		return mappedData;
	}

	public void setMappedData(List<? extends Object> mappedData) {
		this.mappedData = mappedData;
	}

}
