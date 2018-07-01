package it.andrea.reader.writer.api.writer.impl.excel;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import it.andrea.reader.writer.api.exception.FileWriterException;
import it.andrea.reader.writer.api.writer.ReportUtils;
import it.andrea.reader.writer.api.writer.model.ReportTrace;

/**
 * @author Andrea Aresta
 */
public class ExcelHelper {

	public static void definesCellTypeByValue(CellStyle decimalStyle, CellStyle integerStyle, Cell cell, Object value, ReportTrace trace) throws FileWriterException {
		switch (trace.getDataType()) {
		case STRING:
			cell.setCellValue(value.toString());
			cell.setCellType(CellType.STRING);
			break;
		case DATE:
			String formattedValue = ReportUtils.formatDateField(value, trace);
			cell.setCellValue(formattedValue);
			cell.setCellType(CellType.STRING);
			break;
		case NUMBER:
			cell.setCellType(CellType.NUMERIC);
			cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
			cell.setCellStyle(decimalStyle);
			break;
		case INTEGER:
			cell.setCellType(CellType.NUMERIC);
			cell.setCellValue(new Integer(value.toString()));
			cell.setCellStyle(integerStyle);
			break;
		default:
			cell.setCellValue(value.toString());
			cell.setCellType(CellType.STRING);
			break;
		}
	}

	public static void buildRowHeader(Workbook workbook, Sheet sheet, List<ReportTrace> trace) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		BorderStyle border = BorderStyle.THIN;
		style.setBorderBottom(border);
		style.setBorderTop(border);
		style.setBorderRight(border);
		style.setBorderLeft(border);
		Row rowHeader = sheet.createRow(0);
		AtomicInteger colNum = new AtomicInteger(0);
		trace.stream().forEach(c -> {
			Cell cell = rowHeader.createCell(colNum.getAndIncrement());
			cell.setCellStyle(style);
			cell.setCellValue(c.getLabel());

		});
	}

}
