package it.andrea.reader.writer.api.model;

import java.util.Objects;

public enum DateFormat {

	YEAR_MONTH_DAY_DASHED("yyyy-MM-dd"),
	DAY_MONTH_YEAR_DASHED("dd-MM-yyyy"),
	YEAR_MONTH_DAY_SLASHED("yyyy/MM/dd"),
	DAY_MONTH_YEAR_SLASHED("dd/MM/yyyy"),
	YEAR_MONTH_DAY("yyyyMMdd"),
	DAY_MONTH_YEAR("ddMMyyyy");

	private String format;

	private DateFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public static DateFormat fromFormat(String format) {
		DateFormat dateFormat = null;
		for (DateFormat df : DateFormat.values()) {
			if (format.equals(df.getFormat())) {
				dateFormat = df;
				break;
			}
		}
		Objects.requireNonNull(dateFormat, "It does not exist a date format: "+format);
		return dateFormat;
	}

}
