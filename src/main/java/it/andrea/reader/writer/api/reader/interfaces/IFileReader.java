package it.andrea.reader.writer.api.reader.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileResult;

public interface IFileReader {

	public Map<String, FileResult> readFile(InputStream is, FileFeature fileFeature) throws IOException, FileReaderException;

	public Map<String, FileResult> readFile(File file, FileFeature fileFeature) throws IOException, FileReaderException;

}
