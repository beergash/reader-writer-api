package it.beergash.reader.writer.api.utils;

import it.beergash.reader.writer.api.exception.FileReaderException;
import it.beergash.reader.writer.api.reader.interfaces.IFileReader;
import it.beergash.reader.writer.api.reader.model.FileFeature;
import it.beergash.reader.writer.api.reader.model.FileResult;
import it.beergash.reader.writer.api.model.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
	@Qualifier(FileType.CSV)
	private IFileReader csvReader;

	@Autowired
	@Qualifier(FileType.FIXED_POSITION)
	private IFileReader fixedPositionReader;

	@Autowired
	@Qualifier(FileType.EXCEL)
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

	private IFileReader getFileReader(String fileType) {
		IFileReader fileReader = null;
		switch (fileType) {
		case FileType.CSV:
			fileReader = csvReader;
			break;
		case FileType.FIXED_POSITION:
			fileReader = fixedPositionReader;
			break;
		case FileType.EXCEL:
			fileReader = excelReader;
			break;
		}
		return fileReader;
	}
}
