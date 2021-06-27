package it.espr.parser.imdb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParserTest {

	private ImdbParser parser = new ImdbParser();
	private FileReader reader = new FileReader();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@org.junit.Test
	public void Test() throws ParsingException {
		String file = reader.readFileFromClassPath("/pages/tt9147456.imdb");
		ImdbMovie movie = parser.parse(file);
		System.out.println(gson.toJson(movie));
	}
}
