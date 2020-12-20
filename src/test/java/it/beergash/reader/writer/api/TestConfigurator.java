package it.beergash.reader.writer.api;

import java.io.IOException;
import java.util.Properties;

import it.beergash.reader.writer.api.utility.TestUtility;
import org.junit.BeforeClass;

public abstract class TestConfigurator {

	protected static Properties loadingProperties;
	protected static Properties reportProperties;

	@BeforeClass
	public static void setUp() throws IOException {
		loadingProperties = TestUtility.loadproperties("loading_files.properties");
		reportProperties = TestUtility.loadproperties("report.properties");
	}

}
