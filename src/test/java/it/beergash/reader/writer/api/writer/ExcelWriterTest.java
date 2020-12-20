package it.beergash.reader.writer.api.writer;

import it.beergash.reader.writer.api.TestConfigurator;
import it.beergash.reader.writer.api.exception.FileWriterException;
import it.beergash.reader.writer.api.utility.TestUtility;
import it.beergash.reader.writer.api.utility.model.Match;
import it.beergash.reader.writer.api.utility.model.Person;
import it.beergash.reader.writer.api.writer.interfaces.IReportWriter;
import it.beergash.reader.writer.api.writer.model.ReportFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = { "it.beergash.reader.writer.api" })
@TestPropertySource(locations = "classpath:test.properties")
public class ExcelWriterTest extends TestConfigurator {

	private static final String TEST_FOLDER = System.getProperty("user.dir") + "/src/test/resources/";

	@Autowired
	@Qualifier("excel")
	private IReportWriter excelWriter;

	private List<Map<String, Object>> sheet1Data;
	private List<Map<String, Object>> sheet2Data;
	private List<Person> persons = new ArrayList<Person>();
	private List<Match> matches = new ArrayList<Match>();

	@Before
	public void prepareData() {
		buildMapData();
		buildTypedData();
	}

	private void buildMapData() {
		sheet1Data = new ArrayList<Map<String, Object>>();
		Map<String, Object> row1 = new HashMap<String, Object>();
		row1.put("Name", "Michael");
		row1.put("Birth_Date", LocalDate.of(1986, 11, 13));
		row1.put("Salary", new BigDecimal("1500.5"));
		Map<String, Object> row2 = new HashMap<String, Object>();
		row2.put("Name", "Kevin");
		row2.put("Birth_Date", LocalDate.of(1972, 12, 4));
		row2.put("Salary", new BigDecimal("2200"));
		sheet1Data.add(row1);
		sheet1Data.add(row2);
		sheet2Data = new ArrayList<Map<String, Object>>();
		row1 = new HashMap<String, Object>();
		row1.put("Team_A", "Brazil");
		row1.put("Team_B", "Mexico");
		row1.put("Match_Date", LocalDate.of(2018, 7, 1));
		row1.put("Score", "2-0");
		row2 = new HashMap<String, Object>();
		row2.put("Team_A", "France");
		row2.put("Team_B", "Argentina");
		row2.put("Match_Date", LocalDate.of(2018, 6, 30));
		row2.put("Score", "4-3");
		sheet2Data.add(row1);
		sheet2Data.add(row2);
	}

	private void buildTypedData() {
		persons.add(new Person("Michael", new GregorianCalendar(1986, 11, 13).getTime(), new BigDecimal("1500.5")));
		persons.add(new Person("Kevin", new GregorianCalendar(1972, 12, 4).getTime(), new BigDecimal("2200")));
		matches.add(new Match("Brazil", "Mexico", LocalDate.of(2018, 7, 1), "2-0"));
		matches.add(new Match("France", "Argentina", LocalDate.of(2018, 6, 30), "2-1"));
	}

	@Test
	public void testWriteExcel() throws IOException, FileWriterException {
		ReportFeature repFeature = TestUtility.getReportFeaturesByPropertiesFile(reportProperties, "excel1");
		repFeature.setOutputDirectory(TEST_FOLDER + "output/");
		repFeature.getSheets().get(0).setData(sheet1Data);
		repFeature.getSheets().get(1).setData(sheet2Data);
		excelWriter.writeReport(repFeature);
	}
}
