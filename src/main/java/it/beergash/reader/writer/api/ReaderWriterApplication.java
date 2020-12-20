package it.beergash.reader.writer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ReaderWriterApplication {

	private static final Logger log = LoggerFactory.getLogger(ReaderWriterApplication.class);

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(ReaderWriterApplication.class, args);
	}
}
