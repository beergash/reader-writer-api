package it.andrea.reader.writer.api.writer.impl;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
@Qualifier("fixed_position")
public class FixedPositionReportCreator extends BaseReportTextCreator {

	@Autowired
	private ReportUtils repUtils;

	private static final Logger log = LoggerFactory.getLogger(FixedPositionReportCreator.class);

	private static final String LEFT_PAD = "LEFT";

	@Override
	protected LinkedList<String> createLines(ReportSheet sheet, IReportPreaparer preparer) {
		log.info("Creates lines of fixed position report");
		LinkedList<String> lines = new LinkedList<String>();
		List<Map<String, Object>> reportData = sheet.getData();
		log.info("number records" + reportData.size());
		if (sheet.getHeader() != null && !"".equalsIgnoreCase(sheet.getHeader())) {
			lines.add(preparer.buildSheetHeader(sheet));
		}
		List<ReportTrace> trace = sheet.getTrace();
		trace.sort(Comparator.comparing(ReportTrace::getPosition));
		for (Map<String, Object> record : reportData) {
			StringBuffer line = new StringBuffer();
			for (ReportTrace cfg : trace) {
				String fillerCharacter = cfg.getFillerCharacter() == null ? " " : cfg.getFillerCharacter();
				Integer fieldLength = cfg.getFieldLength();
				String formattedValue = repUtils.formatValue(record.get(cfg.getFieldName()), cfg);
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
		if (sheet.getFooter() != null && !"".equalsIgnoreCase(sheet.getFooter())) {
			lines.add(preparer.buildSheetFooter(sheet));
		}
		return lines;
	}

}
