package it.beergash.reader.writer.api.reader.impl;

import it.beergash.reader.writer.api.exception.FileReaderException;
import it.beergash.reader.writer.api.model.FileType;
import it.beergash.reader.writer.api.reader.model.*;
import it.beergash.reader.writer.api.utils.FileLoaderUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Reads Csv files and gets result into {@link FileResult}
 * 
 * @author Andrea Aresta
 */
@Service
@Qualifier(FileType.CSV)
public class CsvFileReader extends TextFileReader {

	protected void readLine(FileFeature fileFeature, String line, Map<String, FileResult> result, String header) throws FileReaderException {
		for (FileSheet s : fileFeature.getSheets()) {
			Object targetObject = null;
			try {
				targetObject = s.getTargetClass() == null ? null : s.getTargetClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new FileReaderException("cannot instantiate class " + s.getTargetClass(), e);
			}
			List<FileTrace> fields = (s.getFields() == null || s.getFields().size() == 0) ? getDeafultTrace(header, s.getSeparator()) : s.getFields();
			Map<String, Object> row = new TreeMap<String, Object>();
			String[] lineSplitted = line.split(s.getSeparator());
			if (isRecordValidForSheet(lineSplitted, s.getValidationConditions())) {
				for (FileTrace f : fields) {
					try {
						Object value = FileLoaderUtils.convertValue(lineSplitted[Integer.valueOf(f.getPosition()) - 1].trim(), f);
						buildData(targetObject, row, f, value);
					} catch (ArrayIndexOutOfBoundsException e) {
						throw new FileReaderException(String.format("Can't get position index %s for line %s", f.getPosition(), line));
					}
				}
				FileLoaderUtils.managesResult(result, s, row, targetObject);
			}
		}
	}

	private boolean isRecordValidForSheet(String[] lineSplitted, List<Matcher> validationConditions) {
		return validationConditions.stream().allMatch(c -> FileLoaderUtils.isMatched(lineSplitted[Integer.valueOf(c.getPosition()) - 1], c));
	}

}
