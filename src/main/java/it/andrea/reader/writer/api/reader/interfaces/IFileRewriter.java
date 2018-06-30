package it.andrea.reader.writer.api.reader.interfaces;

import java.io.File;
import java.io.InputStream;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.reader.model.FileFeature;

/**
 * Lets to rewrite a file before reading.
 * @author Andrea Aresta
 *
 */
public interface IFileRewriter {
	
	public File reWriteFile(InputStream sourceFile, FileFeature fileFeature) throws FileReaderException;

}
