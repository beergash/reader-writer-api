package it.andrea.reader.writer.api.writer.impl;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
@Qualifier("fixed_position")
public class FixedPositionReportCreator extends BaseReportTextCreator {

	private static final Logger log = LoggerFactory.getLogger(FixedPositionReportCreator.class);

	private static final String LEFT_PAD = "LEFT";

	@Override
	protected LinkedList<String> createLines(ReportSheet sheet, IReportPreaparer preparer) throws FileWriterException {
		log.debug("Creates lines of fixed position report");
		LinkedList<String> lines = new LinkedList<String>();
		List<Map<String, Object>> reportData = sheet.getData();
		if (sheet.getHeader() != null && !"".equalsIgnoreCase(sheet.getHeader())) {
			lines.add(preparer.buildSheetHeader(sheet));
		}
		List<ReportTrace> trace = sheet.getTrace();
		trace.sort(Comparator.comparing(ReportTrace::getPosition));
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
		StringBuffer line = new StringBuffer();
		for (ReportTrace cfg : trace) {
			String fillerCharacter = cfg.getFillerCharacter() == null ? " " : cfg.getFillerCharacter();
			Integer fieldLength = cfg.getFieldLength();
			String formattedValue = ReportUtils.formatValue(record.get(cfg.getFieldName()), cfg);
			formattedValue = formattedValue.length() > fieldLength ? formattedValue.substring(0, fieldLength) : formattedValue;
			if (LEFT_PAD.equalsIgnoreCase(cfg.getPadType())) {
				line.append(StringUtils.leftPad(formattedValue, fieldLength, fillerCharacter));
			} else {
				line.append(StringUtils.rightPad(formattedValue, fieldLength, fillerCharacter));
			}
			if (sheet.getSeparator() != null)
				line.append(sheet.getSeparator());
		}
		lines.add(line.toString());
	}

}
