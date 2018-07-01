package it.andrea.reader.writer.api.writer.impl;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.exception.FileWriterException;
import it.andrea.reader.writer.api.writer.BaseReportTextCreator;
import it.andrea.reader.writer.api.writer.ReportUtils;
import it.andrea.reader.writer.api.writer.interfaces.IReportPreaparer;
import it.andrea.reader.writer.api.writer.model.ReportSheet;
import it.andrea.reader.writer.api.writer.model.ReportTrace;

@Service
@Qualifier("csv")
public class CsvReportCreator extends BaseReportTextCreator {

	private static final Logger log = LoggerFactory.getLogger(CsvReportCreator.class);

	@Override
	protected LinkedList<String> createLines(ReportSheet sheet, IReportPreaparer preparer) throws FileWriterException {
		log.info("Creates lines of csv report");
		LinkedList<String> lines = new LinkedList<String>();
		List<Map<String, Object>> reportData = sheet.getData();
		List<ReportTrace> trace = sheet.getTrace();
		trace.sort(Comparator.comparing(ReportTrace::getPosition));
		if (sheet.getHeader() != null && !"".equalsIgnoreCase(sheet.getHeader())) {
			lines.add(preparer.buildSheetHeader(sheet));
		}
		if (reportData != null) {
			for (Map<String, Object> record : reportData) {
				buildRow(sheet, lines, trace, record, null);
			}
		} else {
			for (Object record : sheet.getTypedData()) {
				buildRow(sheet, lines, trace, null, record);
			}
		}
		if (sheet.getFooter() != null && !"".equalsIgnoreCase(sheet.getFooter())) {
			lines.add(preparer.buildSheetFooter(sheet));
		}
		return lines;
	}

	private void buildRow(ReportSheet sheet, LinkedList<String> lines, List<ReportTrace> trace, Map<String, Object> record, Object typedRecord) throws FileWriterException {
		String separator = sheet.getSeparator();
		StringJoiner joiner = new StringJoiner(separator);
		for (ReportTrace cfg : trace) {
			Object value = ReportUtils.findsValueByListType(sheet, record, typedRecord, cfg);
			String formattedValue = ReportUtils.formatValue(value, cfg);
			joiner.add(formattedValue);
		}
		String line = joiner.toString();
		lines.add(line.toString());
	}

}
