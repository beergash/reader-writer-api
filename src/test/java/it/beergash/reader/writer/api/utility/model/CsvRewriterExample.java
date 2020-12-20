package it.beergash.reader.writer.api.utility.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import it.beergash.reader.writer.api.exception.FileReaderException;
import it.beergash.reader.writer.api.reader.interfaces.IFileRewriter;
import it.beergash.reader.writer.api.reader.model.FileFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvRewriterExample implements IFileRewriter {

	private static final Logger log = LoggerFactory.getLogger(CsvRewriterExample.class);

	@Override
	public File reWriteFile(InputStream sourceFile, FileFeature fileFeature) throws FileReaderException {
		File file = new File(System.getProperty("user.dir") + "/src/test/resources/input/rewriter/"
				+ fileFeature.getDescription() + ".txt");
		BufferedWriter bw = null;
		String line = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(sourceFile, StandardCharsets.UTF_8))) {
			/// skipping first two rows like in the example file
			br.readLine();
			br.readLine();
			bw = new BufferedWriter(new FileWriter(file));
			while ((line = br.readLine()) != null) {
				bw.write(line + "\n");
			}
			bw.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		log.debug("rewritten file: " + file.getName());
		return file;
	}

}
