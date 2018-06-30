package it.andrea.reader.writer.api.reader;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.andrea.reader.writer.api.TestConfigurator;
import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.reader.interfaces.IFileReader;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileResult;
import it.andrea.reader.writer.api.utility.TestUtility;
import it.andrea.reader.writer.api.utility.model.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = { "it.andrea.reader.writer.api" })
@TestPropertySource(locations = "classpath:test.properties")
public class ExcelFileReaderTest extends TestConfigurator {

	@Autowired
	@Qualifier("excel")
	private IFileReader fileReader;

	private static final Logger log = LoggerFactory.getLogger(ExcelFileReaderTest.class);

	@Test
	public void testReadFileExcel() throws IOException, FileReaderException {
		String methodName = "testReadFileExcel";
		log.info("Begin test: " + methodName);
		List<Map<String, Object>> matches = new LinkedList<Map<String, Object>>();
		List<Map<String, Object>> persons = new LinkedList<Map<String, Object>>();
		final String sourceFile = TestUtility.INPUT_TEST_FILES_FOLDER + "test_excel.xlsx";
		FileFeature fileFeature = TestUtility.getFileFeaturesByPropertiesFile(loadingProperties, "test_excel");
		try (InputStream is = this.getClass().getResourceAsStream(sourceFile)) {
			Map<String, FileResult> result = fileReader.readFile(is, fileFeature);
			matches = result.get("matches").getData();
			persons = result.get("persons").getData();
		}
		Assert.assertEquals(5, matches.size());
		Assert.assertEquals("Milan", matches.get(0).get("Team A"));
		Assert.assertEquals("Lazio", matches.get(1).get("Team B"));
		Date expectedDate = Date.from(LocalDate.of(2018, 1, 15).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date actualDate = (Date) matches.get(2).get("Date");
		Assert.assertEquals(expectedDate, actualDate);

		Assert.assertEquals(4, persons.size());
		Assert.assertEquals("Kevin", persons.get(0).get("Name"));
		Assert.assertEquals("Mills", persons.get(1).get("Last Name"));
		BigDecimal age2 = (BigDecimal) persons.get(2).get("Age");
		Assert.assertEquals(20, age2.intValue());
		log.info("End test: " + methodName);
	}

	@Test
	public void testReadFileExcelMapToObj() throws IOException, FileReaderException {
		String methodName = "testReadFileExcel";
		log.info("Begin test: " + methodName);
		List<Map<String, Object>> matches = new LinkedList<Map<String, Object>>();
		List<Object> persons = new ArrayList<Object>();
		final String sourceFile = TestUtility.INPUT_TEST_FILES_FOLDER + "test_excel.xlsx";
		FileFeature fileFeature = TestUtility.getFileFeaturesByPropertiesFile(loadingProperties, "test_excel_map_obj");
		try (InputStream is = this.getClass().getResourceAsStream(sourceFile)) {
			Map<String, FileResult> result = fileReader.readFile(is, fileFeature);
			matches = result.get("matches").getData();
			persons = result.get("persons").getMappedData();
		}
		Assert.assertEquals(5, matches.size());
		Assert.assertEquals("Milan", matches.get(0).get("Team A"));
		Assert.assertEquals("Lazio", matches.get(1).get("Team B"));
		Date expectedDate = Date.from(LocalDate.of(2018, 1, 15).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date actualDate = (Date) matches.get(2).get("Date");
		Assert.assertEquals(expectedDate, actualDate);

		Assert.assertEquals(4, persons.size());
		Person firstPerson = (Person) persons.get(0);
		Person secondPerson = (Person) persons.get(1);
		Person thirdPerson = (Person) persons.get(2);
		Assert.assertEquals("Kevin", firstPerson.getName());
		Assert.assertEquals("Mills", secondPerson.getLastName());
		Assert.assertEquals("Susanne", thirdPerson.getName());
		log.info("End test: " + methodName);
	}
}
