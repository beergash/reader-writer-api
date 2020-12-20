package it.beergash.reader.writer.api.reader;

import it.beergash.reader.writer.api.TestConfigurator;
import it.beergash.reader.writer.api.exception.FileReaderException;
import it.beergash.reader.writer.api.reader.interfaces.IFileReader;
import it.beergash.reader.writer.api.reader.model.FileFeature;
import it.beergash.reader.writer.api.reader.model.FileResult;
import it.beergash.reader.writer.api.utility.TestUtility;
import it.beergash.reader.writer.api.utility.model.Person;
import it.beergash.reader.writer.api.utils.FileReaderManager;
import org.junit.Assert;
import org.junit.Ignore;
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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = { "it.beergash.reader.writer.api" })
@TestPropertySource(locations = "classpath:test.properties")
public class CsvFileReaderTest extends TestConfigurator {

	@Autowired
	@Qualifier("csv")
	private IFileReader fileReader;

	@Autowired
	private FileReaderManager readerManager;

	private static final Logger log = LoggerFactory.getLogger(CsvFileReaderTest.class);

	@Test
	public void testLoadFile2Sheets() throws IOException, FileReaderException {
		String methodName = "testLoadFile2Sheets";
		log.info("Begin test: " + methodName);
		List<Map<String, Object>> maleData;
		List<Map<String, Object>> femaleData;
		final String sourceFile = TestUtility.INPUT_TEST_FILES_FOLDER + "sample2sheets.csv";
		FileFeature fileFeature = TestUtility.getFileFeaturesByPropertiesFile(loadingProperties, "sample2sheets");
		try (InputStream is = this.getClass().getResourceAsStream(sourceFile)) {
			Map<String, FileResult> result = fileReader.readFile(is, fileFeature);
			maleData = result.get("sheetMale").getData();
			femaleData = result.get("sheetFemale").getData();
		}
		Assert.assertEquals(3, maleData.size());
		Assert.assertEquals("andrea", maleData.get(0).get("Name"));
		Assert.assertEquals("bottini", maleData.get(1).get("Surname"));
		BigDecimal age1 = (BigDecimal) maleData.get(2).get("Age");
		Assert.assertEquals(38, age1.intValue());

		Assert.assertEquals(6, femaleData.size());
		Assert.assertEquals("federica", femaleData.get(0).get("Name"));
		Assert.assertEquals("verdi", femaleData.get(0).get("Surname"));
		BigDecimal age2 = (BigDecimal) femaleData.get(1).get("Age");
		Assert.assertEquals(25, age2.intValue());
		Assert.assertEquals("jessica", femaleData.get(5).get("Name"));
		log.info("End test: " + methodName);
	}

	@Test
	public void testFileMappedToObject() throws IOException, FileReaderException {
		String methodName = "testFileMappedToObject";
		log.info("Begin test: " + methodName);
		List<? extends Object> result;
		final String sourceFile = TestUtility.INPUT_TEST_FILES_FOLDER + "test_map_objects.csv";
		FileFeature fileFeature = TestUtility.getFileFeaturesByPropertiesFile(loadingProperties, "test_map_obj");
		try (InputStream is = this.getClass().getResourceAsStream(sourceFile)) {
			result = readerManager.readFile(is, fileFeature).get("test_map_obj").getMappedData();
		}
		Assert.assertEquals(5, result.size());
		Person firstRow = (Person) result.get(0);
		Person secondRow = (Person) result.get(1);
		Person thirdRow = (Person) result.get(2);
		Date expectedBirthDate = Date.from(LocalDate.of(1985, 6, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Assert.assertEquals("Mike", firstRow.getName());
		Assert.assertEquals("Stewart", secondRow.getLastName());
		Assert.assertEquals(expectedBirthDate, thirdRow.getBirthDate());
		log.info("End test: " + methodName);
	}

	@Test
	@Ignore
	public void testLoadFile2SheetsWithRewriter() throws IOException, FileReaderException {
		String methodName = "testLoadFile2SheetsWithRewriter";
		log.info("Begin test: " + methodName);
		List<Map<String, Object>> maleData;
		List<Map<String, Object>> femaleData;
		final String sourceFile = TestUtility.INPUT_TEST_FILES_FOLDER + "test_csv_to_rewrite.csv";
		FileFeature fileFeature = TestUtility.getFileFeaturesByPropertiesFile(loadingProperties, "csvrewriter");
		try (InputStream is = this.getClass().getResourceAsStream(sourceFile)) {
			Map<String, FileResult> result = readerManager.readFile(is, fileFeature);
			maleData = result.get("sheetMale").getData();
			femaleData = result.get("sheetFemale").getData();
		}
		Assert.assertEquals(3, maleData.size());
		Assert.assertEquals("andrea", maleData.get(0).get("Name"));
		Assert.assertEquals("bottini", maleData.get(1).get("Surname"));
		BigDecimal age1 = (BigDecimal) maleData.get(2).get("Age");
		Assert.assertEquals(38, age1.intValue());

		Assert.assertEquals(6, femaleData.size());
		Assert.assertEquals("federica", femaleData.get(0).get("Name"));
		Assert.assertEquals("verdi", femaleData.get(0).get("Surname"));
		BigDecimal age2 = (BigDecimal) femaleData.get(1).get("Age");
		Assert.assertEquals(25, age2.intValue());
		Assert.assertEquals("jessica", femaleData.get(5).get("Name"));
		log.info("End test: " + methodName);
	}

}
