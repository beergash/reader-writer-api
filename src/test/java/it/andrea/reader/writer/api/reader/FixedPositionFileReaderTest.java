package it.andrea.reader.writer.api.reader;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.andrea.reader.writer.api.TestConfigurator;
import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileResult;
import it.andrea.reader.writer.api.utility.TestUtility;
import it.andrea.reader.writer.api.utility.model.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = { "it.andrea.reader.writer.api" })
@TestPropertySource(locations = "classpath:test.properties")
public class FixedPositionFileReaderTest extends TestConfigurator {

	@Autowired
	private FileReaderManager readerManager;

	private static final Logger log = LoggerFactory.getLogger(FixedPositionFileReaderTest.class);

	@Test
	public void testSimpleFixedPosFile() throws IOException, FileReaderException {
		String methodName = "testLoadFixedPosFile";
		log.info("Begin test: " + methodName);
		List<Map<String, Object>> data = new LinkedList<Map<String, Object>>();
		final String sourceFile = TestUtility.INPUT_TEST_FILES_FOLDER + "simple_fixed_pos.txt";
		FileFeature fileFeature = TestUtility.getFileFeaturesByPropertiesFile(loadingProperties, "simple_fixed_pos");
		try (InputStream is = this.getClass().getResourceAsStream(sourceFile)) {
			Map<String, FileResult> result = readerManager.readFile(is, fileFeature);
			data = result.get("simple_fixed_pos_sh1").getData();
		}
		Assert.assertEquals(3, data.size());
		Date expectedFirstRowBirthDate = Date.from(LocalDate.of(1981, 3, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date actualFirstRowBirthDay = (Date) data.get(0).get("Birth Date");
		Assert.assertEquals(expectedFirstRowBirthDate, actualFirstRowBirthDay);
		Assert.assertEquals("FEDERICO", data.get(1).get("Name").toString());
		Assert.assertEquals("VERDI", data.get(2).get("Surname").toString());
		log.info("End test: " + methodName);
	}
	
	@Test
	public void testSimpleFixedPosFileMappedToObj() throws IOException, FileReaderException {
		String methodName = "testSimpleFixedPosFileMappedToObj";
		log.info("Begin test: " + methodName);
		List<? extends Object> data = null;
		final String sourceFile = TestUtility.INPUT_TEST_FILES_FOLDER + "simple_fixed_pos.txt";
		FileFeature fileFeature = TestUtility.getFileFeaturesByPropertiesFile(loadingProperties, "simple_fixed_pos_map_obj");
		try (InputStream is = this.getClass().getResourceAsStream(sourceFile)) {
			Map<String, FileResult> result = readerManager.readFile(is, fileFeature);
			data = result.get("simple_fixed_pos_sh1").getMappedData();
		}
		Assert.assertEquals(3, data.size());
		Person firstRow = (Person) data.get(0);
		Person secondRow = (Person) data.get(1);
		Person thirdRow = (Person) data.get(2);
		Date expectedFirstRowBirthDate = Date.from(LocalDate.of(1981, 3, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Assert.assertEquals(expectedFirstRowBirthDate, firstRow.getBirthDate());
		Assert.assertEquals("FEDERICO", secondRow.getName());
		Assert.assertEquals("VERDI", thirdRow.getLastName());
		log.info("hash code: "+firstRow.hashCode() + "---" + firstRow.toString());
		log.info("hash code: "+secondRow.hashCode() + "---" + secondRow.toString());
		log.info("hash code: "+thirdRow.hashCode() + "---" + thirdRow.toString());
		log.info("End test: " + methodName);
	}

}
