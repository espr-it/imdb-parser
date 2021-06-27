package it.espr.parser.imdb;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReader {

	private static final Logger log = LoggerFactory.getLogger(FileReader.class);

	@SuppressWarnings("resource")
	public String readFileFromClassPath(String path) {
		return new Scanner(FileReader.class.getResourceAsStream(path), "UTF-8").useDelimiter("\\A").next();

	}

	public String readFile(String path) {
		try {
			log.debug("Reading file from {}", path);
			return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
		} catch (Exception e) {
			log.debug("Problem when reading file", e);
		}
		return null;
	}

	public void writeFile(String path, String content) {
		try {
			log.debug("Writing file to {}", path);
			File file = new File(path);
			file.getParentFile().mkdirs();
			FileWriter fw = new FileWriter(file, false);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			log.error("Problem when writing file", e);
		}
	}
}
