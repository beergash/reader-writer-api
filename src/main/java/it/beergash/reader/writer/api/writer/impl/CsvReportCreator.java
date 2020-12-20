package it.beergash.reader.writer.api.writer.impl;

import it.beergash.reader.writer.api.exception.FileWriterException;
import it.beergash.reader.writer.api.model.FileType;
import it.beergash.reader.writer.api.writer.BaseReportTextCreator;
import it.beergash.reader.writer.api.writer.ReportUtils;
import it.beergash.reader.writer.api.writer.interfaces.IReportPreaparer;
import it.beergash.reader.writer.api.writer.model.ReportSheet;
import it.beergash.reader.writer.api.writer.model.ReportTrace;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier(FileType.CSV)
public class CsvReportCreator extends BaseReportTextCreator {

	private static final Logger log = LoggerFactory.getLogger(CsvReportCreator.class);

	@Override
	protected LinkedList<String> createLines(ReportSheet sheet, IReportPreaparer preparer) throws FileWriterException {
		log.debug("Creates lines of csv report");
		LinkedList<String> lines = new LinkedList<String>();
		List<Map<String, Object>> reportData = sheet.getData();
		List<ReportTrace> trace = sheet.getTrace();
		trace.sort(Comparator.comparing(ReportTrace::getPosition));
		if (!StringUtils.isEmpty(sheet.getHeader())) {
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
		if (!StringUtils.isEmpty(sheet.getFooter())) {
			lines.add(preparer.buildSheetFooter(sheet));
		}
		return lines;
	}

	private void buildRow(ReportSheet sheet, LinkedList<String> lines, List<ReportTrace> trace, Map<String, Object> record, Object typedRecord) {
		String line = trace.stream()
				.map(cfg -> {
					Object value = ReportUtils.findsValueByListType(sheet, record, typedRecord, cfg);
					return ReportUtils.formatValue(value, cfg);
				}).collect(Collectors.joining(sheet.getSeparator()));
		if (!StringUtils.isEmpty(line)) {
			lines.add(line);
		}
	}

}
