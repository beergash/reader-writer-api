package it.andrea.reader.writer.api.reader.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.andrea.reader.writer.api.model.FileTypes;
import it.andrea.reader.writer.api.reader.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.model.DataType;
import it.andrea.reader.writer.api.utils.FileLoaderUtils;
import it.andrea.reader.writer.api.reader.interfaces.IFileReader;

/**
 * Reads Excel files and gets result into {@link FileResult}
 * 
 * @author Andrea Aresta
 */
@Service
@Qualifier(FileTypes.EXCEL)
public class ExcelFileReader implements IFileReader {

	@Override
	public Map<String, FileResult> readFile(File file, FileFeature fileFeature) throws IOException, FileReaderException {
		log.debug("reading excel file : " + file.getName());
		try (InputStream is = new FileInputStream(file)) {
			return readFile(is, fileFeature);
		}
	}

	private static final Logger log = LoggerFactory.getLogger(ExcelFileReader.class);

	@Override
	public Map<String, FileResult> readFile(InputStream is, FileFeature fileFeature) throws IOException, FileReaderException {
		log.debug("Reading excel file");
		Map<String, FileResult> result = new LinkedHashMap<String, FileResult>();
		Object targetObject = null;
		try {
			Workbook workbook = WorkbookFactory.create(is);
			int sheetsNumber = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetsNumber; i++) {
				FileSheet fileSheet = fileFeature.getSheets().get(i);
				List<FileTrace> fields = fileSheet.getFields();
				Sheet excelSheet = workbook.getSheetAt(i);
				int firstRow = fileSheet.isHasHeader() ? 1 : 0;
				for (int rowIdx = firstRow; rowIdx <= excelSheet.getLastRowNum(); rowIdx++) {
					try {
						targetObject = fileSheet.getTargetClass() == null ? null : fileSheet.getTargetClass().newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new FileReaderException("cannot instantiate class " + fileSheet.getTargetClass(), e);
					}
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					Row excelRow = excelSheet.getRow(rowIdx);
					for (FileTrace f : fields) {
						Cell cell = excelRow.getCell(Integer.valueOf(f.getPosition()) - 1, MissingCellPolicy.CREATE_NULL_AS_BLANK);
						String stringValue = getStringValueFromCell(cell, f);
						Object value = FileLoaderUtils.convertValue(stringValue, f);
						buildData(targetObject, row, f, value);
					}
					FileLoaderUtils.managesResult(result, fileSheet, row, targetObject);
				}
			}
			workbook.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new FileReaderException(e.getMessage(), e);
		}
		return result;
	}

	private String getStringValueFromCell(Cell cell, FileTrace f) {
		String stringValue = "";
		if (CellType.STRING.equals(cell.getCellTypeEnum()) || CellType.BLANK.equals(cell.getCellTypeEnum())) {
			stringValue = cell.getStringCellValue();
		}
		if (CellType.NUMERIC.equals(cell.getCellTypeEnum()) && (DataType.STRING.equals(f.getDataType())) || DataType.NUMBER.equals(f.getDataType())) {
			stringValue = String.valueOf(cell.getNumericCellValue());
		}
		if (CellType.NUMERIC.equals(cell.getCellTypeEnum()) && (DataType.DATE.equals(f.getDataType()))) {
			Date dateValue = cell.getDateCellValue();
			DateFormat df = new SimpleDateFormat(f.getDateFormat());
			stringValue = df.format(dateValue);
		}
		return stringValue.trim();
	}

	private void buildData(Object targetObject, Map<String, Object> row, FileTrace f, Object value) throws FileReaderException {
		if (targetObject == null) {
			row.put(f.getLabel(), value);
		} else {
			try {
				FileLoaderUtils.setEntityObject(targetObject, f, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new FileReaderException(e);
			}
		}
	}

}
