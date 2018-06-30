package it.andrea.reader.writer.api;

import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;

import it.andrea.reader.writer.api.utility.TestUtility;

public abstract class TestConfigurator {

	protected static Properties loadingProperties;
	protected static Properties reportProperties;

	@BeforeClass
	public static void setUp() throws IOException {
		loadingProperties = TestUtility.loadproperties("loading_files.properties");
		reportProperties = TestUtility.loadproperties("report.properties");
	}

}
