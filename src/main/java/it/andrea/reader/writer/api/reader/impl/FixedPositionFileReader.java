package it.andrea.reader.writer.api.reader.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import it.andrea.reader.writer.api.exception.FileReaderException;
import it.andrea.reader.writer.api.reader.FileLoaderUtils;
import it.andrea.reader.writer.api.reader.model.FileFeature;
import it.andrea.reader.writer.api.reader.model.FileResult;
import it.andrea.reader.writer.api.reader.model.FileSheet;
import it.andrea.reader.writer.api.reader.model.FileTrace;
import it.andrea.reader.writer.api.reader.model.Matcher;

/**
 * Reads Test fixed position files and gets result into {@link FileResult}
 * 
 * @author Andrea Aresta
 */
@Service
@Qualifier("fixed_position")
public class FixedPositionFileReader extends TextFileReader {

	private static final String INTERVAL_POSITION_CHAR_SEPARATOR = "-";

	protected void readLine(FileFeature fileFeature, String line, Map<String, FileResult> result, String header) throws FileReaderException {
		for (FileSheet s : fileFeature.getSheets()) {
			List<FileTrace> fields = (s.getFields() == null || s.getFields().size() == 0) ? getDeafultTrace(header, s.getSeparator()) : s.getFields();
			Object targetObject = null;
			try {
				targetObject = s.getTargetClass() == null ? null : s.getTargetClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new FileReaderException("cannot instantiate class " + s.getTargetClass(), e);
			}
			Map<String, Object> row = new TreeMap<String, Object>();
			if (isRecordValidForSheet(line, s.getValidationConditions())) {
				for (FileTrace f : fields) {
					String[] rangePosition = f.getPosition().split(INTERVAL_POSITION_CHAR_SEPARATOR);
					int startPosition = Integer.valueOf(rangePosition[0]) - 1;
					int endPosition = Integer.valueOf(rangePosition[1]);
					Object value = FileLoaderUtils.convertValue(line.substring(startPosition, endPosition).trim(), f);
					buildData(targetObject, row, f, value);
				}
				FileLoaderUtils.managesResult(result, s, row, targetObject);
			}
		}
	}

	private boolean isRecordValidForSheet(String line, List<Matcher> validationConditions) {
		return validationConditions.stream()
				.allMatch(c -> FileLoaderUtils.isMatched(
						line.substring(Integer.valueOf(c.getPosition().split(INTERVAL_POSITION_CHAR_SEPARATOR)[0]) - 1, Integer.valueOf(c.getPosition().split(INTERVAL_POSITION_CHAR_SEPARATOR)[1])),
						c));
	}

}
