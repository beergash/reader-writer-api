package it.beergash.reader.writer.api.writer;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import it.beergash.reader.writer.api.exception.FileWriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.beergash.reader.writer.api.model.DataType;
import it.beergash.reader.writer.api.writer.model.ReportSheet;
import it.beergash.reader.writer.api.writer.model.ReportTrace;

/**
 * Contains utility methods used by writer API
 * 
 * @author Andrea Aresta
 */
public class ReportUtils {

	private static final Logger log = LoggerFactory.getLogger(ReportUtils.class);

	public static String formatValue(Object value, ReportTrace trace) throws FileWriterException {
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

	public static String formatDateField(Object value, ReportTrace trace) throws FileWriterException {
		Date dateToConvert = null;
		if (value instanceof Date) {
			dateToConvert = (Date) value;
		} else {
			String valueToString = value == null ? "" : value.toString().trim();
			SimpleDateFormat format = new SimpleDateFormat(trace.getInputDateFormat().getFormat().trim());
			try {
				dateToConvert = !value.equals("") ? format.parse(valueToString) : null;
			} catch (ParseException e) {
				log.error("unable to convert date field " + trace.getFieldName() + "with value " + value + "with format " + trace.getInputDateFormat().getFormat().trim());
				throw new FileWriterException("unable to convert date field " + trace.getFieldName() + "with value " + value + "with format " + trace.getInputDateFormat().getFormat().trim(), e);
			}
		}
		DateFormat df = new SimpleDateFormat(trace.getOutputDateFormat().getFormat().trim());
		String formattedValue = dateToConvert != null ? df.format(dateToConvert) : "";
		return formattedValue;
	}

	public static Object findsValueByListType(ReportSheet sheet, Map<String, Object> record, Object typedRecord, ReportTrace cfg) throws FileWriterException {
		Object value = null;
		String fieldName = cfg.getFieldName();
		if (typedRecord != null) {
			try {
				value = new PropertyDescriptor(fieldName, sheet.getTypedDataClass()).getReadMethod().invoke(typedRecord);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
				log.error("Cannot invoke getter of " + fieldName + " for class ", e);
				throw new FileWriterException("Cannot invoke getter of " + fieldName + " for class ", e);
			}
		} else {
			value = record.get(fieldName);
		}
		return value;
	}

}
