package it.andrea.reader.writer.api.writer.impl;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.writer.BaseReportTextCreator;
import it.andrea.reader.writer.api.writer.ReportUtils;
import it.andrea.reader.writer.api.writer.interfaces.IReportPreaparer;
import it.andrea.reader.writer.api.writer.model.ReportSheet;
import it.andrea.reader.writer.api.writer.model.ReportTrace;

@Service
@Qualifier("csv")
public class CsvReportCreator extends BaseReportTextCreator {

	@Autowired
	private ReportUtils repUtils;

	private static final Logger log = LoggerFactory.getLogger(CsvReportCreator.class);

	@Override
	protected LinkedList<String> createLines(ReportSheet sheet, IReportPreaparer preparer) {
		log.info("Creates lines of fixed position report");
		LinkedList<String> lines = new LinkedList<String>();
		List<Map<String, Object>> reportData = sheet.getData();
		List<ReportTrace> trace = sheet.getTrace();
		trace.sort(Comparator.comparing(ReportTrace::getPosition));
		if (sheet.getHeader() != null && !"".equalsIgnoreCase(sheet.getHeader())) {
			lines.add(preparer.buildSheetHeader(sheet));
		}
		for (Map<String, Object> record : reportData) {
			StringBuffer line = new StringBuffer();
			String separator = sheet.getSeparator();
			for (ReportTrace arc : trace) {
				Object value = record.get(arc.getFieldName()) == null ? "" : record.get(arc.getFieldName());
				String formattedValue = repUtils.formatValue(value, arc);
				line.append(formattedValue);
				line.append(separator);
			}
			line.deleteCharAt(line.lastIndexOf(separator));
			lines.add(line.toString());
		}
		if (sheet.getFooter() != null && !"".equalsIgnoreCase(sheet.getFooter())) {
			lines.add(preparer.buildSheetFooter(sheet));
		}
		return lines;
	}

}
