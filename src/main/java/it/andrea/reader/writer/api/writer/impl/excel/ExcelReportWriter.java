package it.andrea.reader.writer.api.writer.impl.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.exception.FileWriterException;
import it.andrea.reader.writer.api.writer.interfaces.IReportPreaparer;
import it.andrea.reader.writer.api.writer.interfaces.IReportWriter;
import it.andrea.reader.writer.api.writer.model.DefaultReportPreparer;
import it.andrea.reader.writer.api.writer.model.ReportFeature;
import it.andrea.reader.writer.api.writer.model.ReportSheet;
import it.andrea.reader.writer.api.writer.model.ReportTrace;

/**
 * @author Andrea Aresta
 */
@Service
@Qualifier("excel")
public class ExcelReportWriter implements IReportWriter {
	
	@Autowired
	private ExcelHelper excelHelper;

	private static final Logger log = LoggerFactory.getLogger(ExcelReportWriter.class);

	public File writeReport(ReportFeature repFeature) throws IOException, FileWriterException {
		IReportPreaparer preparer = Optional.ofNullable(repFeature.getPreparer()).orElse(new DefaultReportPreparer());
		String filename = preparer.generateFileName(repFeature);
		String fullFilename = repFeature.getOutputDirectory() + File.separator + filename;
		File file = new File(fullFilename);
		List<ReportSheet> sheets = repFeature.getSheets();
		File parentDir = new File(file.getAbsoluteFile().getParent());
		parentDir.mkdirs();
		Workbook workbook = new HSSFWorkbook();
		FileOutputStream outputStream = null;
		sheets.forEach(s -> {
			buildSheet(s, workbook);
		});
		try {
			outputStream = new FileOutputStream(fullFilename);
			workbook.write(outputStream);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			workbook.close();
		}
		log.info("created file: " + fullFilename);
		return file;
	}

	private void buildSheet(ReportSheet excelSheet, Workbook wb) {
		List<ReportTrace> trace = excelSheet.getTrace().stream().sorted(Comparator.comparing(ReportTrace::getPosition))
				.collect(Collectors.toList());
		Sheet sheet = wb.createSheet(excelSheet.getName());
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle decimalStyle = wb.createCellStyle();
		decimalStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00####;-#,##0.00####;0.00"));
		CellStyle integerStyle = wb.createCellStyle();
		integerStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0;-#,##0;0"));
		List<Map<String, Object>> data = excelSheet.getData();
		excelHelper.buildRowHeader(wb, sheet, trace);
		AtomicInteger rowNum = new AtomicInteger(1);
		data.forEach(d -> {
			Row row = sheet.createRow(rowNum.getAndIncrement());
			AtomicInteger colNum = new AtomicInteger(0);
			trace.stream().forEach(t -> {
				Object field = d.get(t.getFieldName());
				Cell cell = row.createCell(colNum.getAndIncrement());
				excelHelper.definesCellTypeByValue(decimalStyle, integerStyle, cell, field, t);
			});
		});
		for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
			sheet.autoSizeColumn(i);
		}
	}

}
