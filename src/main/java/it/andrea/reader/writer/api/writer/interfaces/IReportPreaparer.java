package it.andrea.reader.writer.api.writer.interfaces;

import it.andrea.reader.writer.api.writer.model.ReportFeature;
import it.andrea.reader.writer.api.writer.model.ReportSheet;

public interface IReportPreaparer {

	public String generateFileName(ReportFeature repFeature);

	public String buildSheetHeader(ReportSheet sheet);

	public String buildSheetFooter(ReportSheet sheet);

	public String buildSheetName(ReportSheet sheet);

}
