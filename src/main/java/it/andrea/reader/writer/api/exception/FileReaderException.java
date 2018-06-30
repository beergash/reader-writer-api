package it.andrea.reader.writer.api.exception;

/**
 * Exception class of the reader API
 * @author Andrea Aresta
 *
 */
public class FileReaderException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileReaderException() {
	}

	public FileReaderException(String s) {
		super(s);
	}

	public FileReaderException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public FileReaderException(Throwable throwable) {
		super(throwable);
	}

}
