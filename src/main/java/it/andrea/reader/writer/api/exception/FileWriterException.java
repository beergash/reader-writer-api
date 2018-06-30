package it.andrea.reader.writer.api.exception;

/**
 * Exception class of the writer API
 * @author Andrea Aresta
 *
 */
public class FileWriterException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileWriterException() {
	}

	public FileWriterException(String s) {
		super(s);
	}

	public FileWriterException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public FileWriterException(Throwable throwable) {
		super(throwable);
	}

}
