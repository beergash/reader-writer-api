package it.andrea.reader.writer.api.reader.model;

/**
 * @author Andrea Aresta
 *
 */
public enum FileType {

	CSV("csv"),
	FIXED_POSITION("fixed_position"),
	EXCEL("excel");


	private FileType(String description) {
		this.description = description;
	}
	private String description;
	
	public String getDescription() {
		return description;
	}


}
