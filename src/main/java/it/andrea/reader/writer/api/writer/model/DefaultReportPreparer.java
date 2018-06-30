package it.andrea.reader.writer.api.writer.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.andrea.reader.writer.api.writer.interfaces.IReportPreaparer;

public class DefaultReportPreparer implements IReportPreaparer {

	@Override
	public String generateFileName(ReportFeature repFeature) {
		return repFeature.getFilename();
	}

	@Override
	public String buildSheetHeader(ReportSheet sheet) {
		String header = sheet.getHeader();
		if ("HEADER_BY_LABELS".equalsIgnoreCase(header)) {
			List<ReportTrace> trace = sheet.getTrace();
			String delimiter = Optional.ofNullable(sheet.getSeparator()).orElse(";");
			header = trace.stream().sorted(Comparator.comparing(ReportTrace::getPosition)).map(ReportTrace::getLabel).collect(Collectors.joining(delimiter));
		}
		return header;
	}

	@Override
	public String buildSheetFooter(ReportSheet sheet) {
		return sheet.getFooter();
	}

	@Override
	public String buildSheetName(ReportSheet sheet) {
		return sheet.getName();
	}

}
