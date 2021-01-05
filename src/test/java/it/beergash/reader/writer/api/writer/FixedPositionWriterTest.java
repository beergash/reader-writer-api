package it.beergash.reader.writer.api.writer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.beergash.reader.writer.api.TestConfigurator;
import it.beergash.reader.writer.api.WriterTestConfigurator;
import it.beergash.reader.writer.api.exception.FileWriterException;
import it.beergash.reader.writer.api.model.FileType;
import it.beergash.reader.writer.api.utility.TestUtility;
import it.beergash.reader.writer.api.writer.interfaces.IReportWriter;
import it.beergash.reader.writer.api.writer.model.ReportFeature;
import it.beergash.reader.writer.api.writer.model.ReportSheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FixedPositionWriterTest extends WriterTestConfigurator {

	@Autowired
	@Qualifier(FileType.FIXED_POSITION)
	private IReportWriter reportWriter;

	@Test
	public void testWriteFixedPositionReport() throws IOException, FileWriterException {
		List<Map<String, Object>> outputData = new ArrayList<Map<String, Object>>();
		Map<String, Object> row1 = new HashMap<String, Object>();
		row1.put("Month", "DECEMBER");
		row1.put("Amount", new BigDecimal(200D));
		row1.put("Currency", "EUR");
		Map<String, Object> row2 = new HashMap<String, Object>();
		row2.put("Month", "MAY");
		row2.put("Amount", new BigDecimal(150D));
		row2.put("Currency", "EUR");
		outputData.add(row1);
		outputData.add(row2);
		repFeature = TestUtility.getReportFeaturesByPropertiesFile(reportProperties, "fp1");
		ReportSheet sheet = repFeature.getSheets().get(0);
		sheet.setData(outputData);
		reportWriter.writeReport(repFeature);
	}

}
