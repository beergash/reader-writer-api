package it.andrea.reader.writer.api.writer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.model.DataType;
import it.andrea.reader.writer.api.writer.model.ReportTrace;

/**
 * Contains utility methods used by writer API
 * 
 * @author Andrea Aresta
 */
@Service
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class ReportUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ReportUtils.class);

	public String formatValue(Object value, ReportTrace trace) {
		String formattedValue = value == null ? "" : value.toString().trim();
		if (DataType.DATE.equals(trace.getDataType())) {
			formattedValue = formatDateField(value, trace);
		}
		if (DataType.NUMBER.equals(trace.getDataType())) {
			try {
			} catch (NumberFormatException e) {
				if (value == null || "".equals(value)) {
					formattedValue = "";
				} else {
					log.error("unable to convert number field " + value + "with value " + trace.getFieldName() + "with format " + trace.getNumberFormat());
				}
			}
		}
		return formattedValue;
	}

	public String formatDateField(Object value, ReportTrace trace) {
		String formattedValue = value == null ? "" : value.toString().trim();
		SimpleDateFormat format = new SimpleDateFormat(trace.getInputDateFormat().getFormat().trim());
		Date valueConverted = null;
		try {
			valueConverted = !value.equals("") ? format.parse(formattedValue) : null;
		} catch (ParseException e) {
			log.error("unable to convert date field " + trace.getFieldName() + "with value " + value + "with format " + trace.getInputDateFormat().getFormat().trim());
		}
		DateFormat df = new SimpleDateFormat(trace.getOutputDateFormat().getFormat().trim());
		formattedValue = valueConverted != null ? df.format(valueConverted) : "";
		return formattedValue;
	}

}
