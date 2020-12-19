package it.andrea.reader.writer.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.reader.interfaces.IFileReader;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileResult;
import it.andrea.reader.writer.api.reader.model.FileType;

/**
 * Manager class of reader API. Given a file source such as InputStream or File
 * and {@link FileFeature}, calls the right {@link IFileReader and reads the
 * source}
 * 
 * @author Andrea Aresta
 *
 */
@Service
public class FileReaderManager {

	@Autowired
	@Qualifier("csv")
	private IFileReader csvReader;

	@Autowired
	@Qualifier("fixed_position")
	private IFileReader fixedPositionReader;

	@Autowired
	@Qualifier("fixed_position")
	private IFileReader excelReader;

	/**
	 * loads the InputStream source and puts result into {@link FileResult} grouped
	 * by sheet file
	 * 
	 * @param source
	 * @param fileFeature
	 * @return
	 * @throws IOException
	 * @throws FileReaderException
	 */
	public Map<String, FileResult> readFile(InputStream source, FileFeature fileFeature) throws IOException, FileReaderException {
		Map<String, FileResult> result = null;
		IFileReader fileReader = getFileReader(fileFeature.getType());
		if (fileFeature.getRewriterClass() != null) {
			File sourceFile = fileFeature.getRewriterClass().reWriteFile(source, fileFeature);
			result = fileReader.readFile(sourceFile, fileFeature);
			sourceFile.delete();
		} else {
			result = fileReader.readFile(source, fileFeature);
		}
		return result;
	}

	/**
	 * loads the File source and puts result into {@link FileResult} grouped by
	 * sheet file
	 * 
	 * @param source
	 * @param fileFeature
	 * @return
	 * @throws IOException
	 * @throws FileReaderException
	 */
	public Map<String, FileResult> readFile(File source, FileFeature fileFeature) throws IOException, FileReaderException {
		Map<String, FileResult> result = null;
		IFileReader fileReader = getFileReader(fileFeature.getType());
		if (fileFeature.getRewriterClass() != null) {
			File sourceFile = fileFeature.getRewriterClass().reWriteFile(new FileInputStream(source), fileFeature);
			result = fileReader.readFile(sourceFile, fileFeature);
			sourceFile.delete();
		} else {
			result = fileReader.readFile(source, fileFeature);
		}
		return result;
	}

	private IFileReader getFileReader(FileType fileType) {
		IFileReader fileReader = null;
		switch (fileType) {
		case CSV:
			fileReader = csvReader;
			break;
		case FIXED_POSITION:
			fileReader = fixedPositionReader;
			break;
		case EXCEL:
			fileReader = excelReader;
			break;
		}
		return fileReader;
	}
}
