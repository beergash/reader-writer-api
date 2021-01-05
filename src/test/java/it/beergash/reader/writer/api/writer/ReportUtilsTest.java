package it.beergash.reader.writer.api.writer;

import it.beergash.reader.writer.api.exception.FileWriterException;
import it.beergash.reader.writer.api.model.DataType;
import it.beergash.reader.writer.api.model.DateFormat;
import it.beergash.reader.writer.api.writer.model.ReportTrace;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class ReportUtilsTest {

	@Test
	public void testWriteDate() throws FileWriterException {
		ReportTrace rt = new ReportTrace();
		rt.setInputDateFormat(DateFormat.YEAR_MONTH_DAY_DASHED);
		rt.setOutputDateFormat(DateFormat.DAY_MONTH_YEAR_SLASHED);
		rt.setDataType(DataType.DATE);
		rt.setFieldName("date");
		LocalDate date1 = LocalDate.of(2018, 12, 11);
		String formattedValue = ReportUtils.formatValue(date1, rt);
		Assert.assertEquals("11/12/2018", formattedValue);
		String date2 = "12-11-2018";
		rt.setInputDateFormat(DateFormat.DAY_MONTH_YEAR_DASHED);
		rt.setOutputDateFormat(DateFormat.YEAR_MONTH_DAY);
		Assert.assertEquals("20181112", ReportUtils.formatValue(date2, rt));
	}

}
