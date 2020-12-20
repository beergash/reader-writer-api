package it.beergash.reader.writer.api.utils;

import it.beergash.reader.writer.api.exception.FileReaderException;
import it.beergash.reader.writer.api.reader.impl.ExcelFileReader;
import it.beergash.reader.writer.api.reader.model.FileResult;
import it.beergash.reader.writer.api.reader.model.FileSheet;
import it.beergash.reader.writer.api.reader.model.FileTrace;
import it.beergash.reader.writer.api.reader.model.Matcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Contains utility methods used by reader API
 * 
 * @author Andrea Aresta
 */
public class FileLoaderUtils {

	private static final Logger log = LoggerFactory.getLogger(ExcelFileReader.class);

	/**
	 * converts the value read from file into the data type provided
	 * 
	 * @param value
	 * @param trace
	 * @return
	 * @throws FileReaderException
	 */
	public static Object convertValue(String value, FileTrace trace) throws FileReaderException {
		value = trace.getInterpreter() == null ? value : trace.getInterpreter().interpretValue(value);
		Object formattedValue;
		switch (trace.getDataType()) {
			case STRING:
				formattedValue = value;
				break;
			case DATE:
				SimpleDateFormat formatter = new SimpleDateFormat(trace.getDateFormat());
				try {
					formattedValue = StringUtils.isEmpty(value) ? null : formatter.parse(value);
				} catch (ParseException e) {
					String errorMsg = "Unable to convert date : " + value + " with format " + trace.getDateFormat();
					log.error(errorMsg, e);
					throw new FileReaderException(errorMsg, e);
				}
				break;
			case NUMBER:
				try {
					formattedValue = (value == null || "".equals(value)) ? null : new BigDecimal(value.replace(",", "."));
				} catch (NumberFormatException e) {
					String errorMsg = "Unable to convert number : " + value;
					log.error(errorMsg, e);
					throw new FileReaderException(errorMsg, e);
				}
				break;
		default:
			formattedValue = value;
			break;
		}
		return formattedValue;
	}

	/**
	 * establishes if this record file is valid for the sheet
	 * 
	 * @param value
	 * @param matcher
	 * @return
	 */
	public static boolean isMatched(String value, Matcher matcher) {
		boolean isRecordValid = false;
		switch (matcher.getOperator()) {
		case EQ:
			isRecordValid = value.equals(matcher.getTargetValue());
			break;
		case NE:
			isRecordValid = !value.equals(matcher.getTargetValue());
			break;
		case LE:
			isRecordValid = Double.valueOf(value) <= Double.valueOf(matcher.getTargetValue());
			break;
		case LT:
			isRecordValid = Double.valueOf(value) < Double.valueOf(matcher.getTargetValue());
			break;
		case GE:
			isRecordValid = Double.valueOf(value) >= Double.valueOf(matcher.getTargetValue());
			break;
		case GT:
			isRecordValid = Double.valueOf(value) > Double.valueOf(matcher.getTargetValue());
			break;
		default:
			isRecordValid = true;
			break;
		}
		return isRecordValid;
	}
	
	public static void managesResult(Map<String, FileResult> result, FileSheet sheet, Map<String, Object> row, Object record) {
		if (record == null) {
			managesResult(result, sheet, row);
		} else {
			managesResult(result, sheet, record);
		}
	}

	private static void managesResult(Map<String, FileResult> result, FileSheet sheet, Map<String, Object> row) {
		if (result.get(sheet.getName()) != null) {
			LinkedList<Map<String, Object>> data = (LinkedList<Map<String, Object>>) result.get(sheet.getName()).getData();
			data.add(row);
		} else {
			List<Map<String, Object>> data = new LinkedList<Map<String, Object>>();
			data.add(row);
			FileResult fr = new FileResult();
			fr.setData(data);
			result.put(sheet.getName(), fr);
		}
	}

	private static void managesResult(Map<String, FileResult> result, FileSheet sheet, Object record) {
		if (result.get(sheet.getName()) != null) {
			LinkedList<Object> records = (LinkedList<Object>) result.get(sheet.getName()).getMappedData();
			records.add(record);
		} else {
			List<Object> records = new LinkedList<Object>();
			records.add(record);
			FileResult fr = new FileResult();
			fr.setMappedData(records);
			result.put(sheet.getName(), fr);
		}
	}

	public static void setEntityObject(Object targetObject, FileTrace recordConfig, Object value)
			throws FileReaderException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String fieldJavaAttribute = recordConfig.getFieldJavaAttribute();
		String setterMethodName = "set" + fieldJavaAttribute.substring(0, 1).toUpperCase() + fieldJavaAttribute.substring(1);
		Method setterMethod = null;
		try {
			switch (recordConfig.getDataType()) {
				case STRING:
					setterMethod = targetObject.getClass().getMethod(setterMethodName, String.class);
					break;
				case DATE:
					setterMethod = targetObject.getClass().getMethod(setterMethodName, Date.class);
					break;
				case NUMBER:
					setterMethod = targetObject.getClass().getMethod(setterMethodName, BigDecimal.class);
					break;
				default:
					setterMethod = targetObject.getClass().getMethod(setterMethodName, String.class);
					break;
			}
		} catch (NoSuchMethodException e) {
			String errMsg = "not valid java attribute " + fieldJavaAttribute;
			log.error(errMsg);
			throw new FileReaderException(errMsg, e);
		}
		setterMethod.invoke(targetObject, value);

	}

}
