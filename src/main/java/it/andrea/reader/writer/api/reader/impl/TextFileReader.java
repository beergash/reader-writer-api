package it.andrea.reader.writer.api.reader.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.model.DataType;
import it.andrea.reader.writer.api.reader.FileLoaderUtils;
import it.andrea.reader.writer.api.reader.interfaces.IFileReader;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileResult;
import it.andrea.reader.writer.api.reader.model.FileTrace;

/**
 * Generic reader class for flat text files (csv, fixed position)
 * @author Andrea Aresta
 *
 */
public abstract class TextFileReader implements IFileReader {

	private static final Logger log = LoggerFactory.getLogger(TextFileReader.class);
	
	@Override
	public Map<String, FileResult> readFile(InputStream is, FileFeature fileFeature) throws IOException, FileReaderException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			return readBufferedReader(fileFeature, reader);
		}
	}

	@Override
	public Map<String, FileResult> readFile(File file, FileFeature fileFeature) throws IOException, FileReaderException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return readBufferedReader(fileFeature, reader);
		}
	}
	
	protected Map<String, FileResult> readBufferedReader(FileFeature fileFeature, BufferedReader reader) throws IOException, FileReaderException {
		log.debug("reading file csv");
		Map<String, FileResult> result = new TreeMap<String, FileResult>();
		String header = "";
		if (fileFeature.isHasHeader()) {
			header = reader.readLine();
		}
		String line = "";
		while ((line = reader.readLine()) != null) {
			readLine(fileFeature, line, result, header);
		}
		return result;
	}
	
	protected abstract void readLine(FileFeature fileFeature, String line, Map<String, FileResult> result, String header) throws FileReaderException;
	
	protected List<FileTrace> getDeafultTrace(String header, String separator) {
		List<FileTrace> trace = new ArrayList<FileTrace>();
		String[] fields = header.split(separator);
		for (int i = 0; i < fields.length; i++) {
			FileTrace ft = new FileTrace(String.valueOf(i + 1), fields[i], DataType.STRING);
			trace.add(ft);
		}
		return trace;
	}
	


	protected void buildData(Object targetObject, Map<String, Object> row, FileTrace f, Object value) throws FileReaderException {
		if (targetObject == null) {
			row.put(f.getLabel(), value);
		} else {
			try {
				FileLoaderUtils.setEntityObject(targetObject, f, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new FileReaderException(e);
			}
		}
	}

}
