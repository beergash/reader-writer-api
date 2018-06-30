package it.andrea.reader.writer.api.writer;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.andrea.reader.writer.api.exception.FileWriterException;
import it.andrea.reader.writer.api.model.DataType;
import it.andrea.reader.writer.api.model.DateFormat;
import it.andrea.reader.writer.api.writer.model.ReportTrace;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = { "it.andrea.reader.writer.api" })
@TestPropertySource(locations = "classpath:test.properties")
public class ReportUtilsTest {

	@Autowired
	private ReportUtils reportUtils;

	@Test
	public void testWriteDate() throws IOException, FileWriterException {
		ReportTrace rt = new ReportTrace();
		rt.setInputDateFormat(DateFormat.YEAR_MONTH_DAY_DASHED);
		rt.setOutputDateFormat(DateFormat.DAY_MONTH_YEAR_SLASHED);
		rt.setDataType(DataType.DATE);
		rt.setFieldName("date");
		LocalDate date1 = LocalDate.of(2018, 12, 11);
		String formattedValue = reportUtils.formatValue(date1, rt);
		Assert.assertEquals("11/12/2018", formattedValue);
		String date2 = "12-11-2018";
		rt.setInputDateFormat(DateFormat.DAY_MONTH_YEAR_DASHED);
		rt.setOutputDateFormat(DateFormat.YEAR_MONTH_DAY);
		Assert.assertEquals("20181112", reportUtils.formatValue(date2, rt));
	}

}
