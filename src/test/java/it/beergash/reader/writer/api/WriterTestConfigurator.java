package it.beergash.reader.writer.api;

import it.beergash.reader.writer.api.writer.model.ReportFeature;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;

import java.io.File;
import java.io.IOException;

public class WriterTestConfigurator extends TestConfigurator {

    protected static ReportFeature repFeature;

    @AfterClass
    public static void cleanOutputDirectory() throws IOException {
        FileUtils.cleanDirectory(new File(repFeature.getOutputDirectory()));
    }
}
