package it.espr.parser.imdb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.espr.jsonldparser.deserialiser.GsonDeserialiser;
import it.espr.utils.http.StringHttpClient;

public class ImdbParser {

	private static final Logger log = LoggerFactory.getLogger(ImdbParser.class);

	SimpleDateFormat dateParsingFormat = new SimpleDateFormat("yyyy-MM-dd");
	Pattern urlPattern = Pattern.compile(".*/(tt[0-9]+)/.*");

	static class JsonLdData {
		public String url;
		public String name;
		public String image;
		public List<String> genre;
		public String datePublished;
		public AggregateRating aggregateRating;
	}

	static class AggregateRating {
		public Float ratingValue;
	}

	public ImdbMovie get(String id) throws ParsingException {
		return this.parse(new StringHttpClient().get("https://www.imdb.com/title/" + id + "/"));
	}

	public ImdbMovie parse(String html) throws ParsingException {
		it.espr.jsonldparser.Parser ldParser = new it.espr.jsonldparser.Parser(new GsonDeserialiser());
		JsonLdData data = ldParser.parseHtml(html, JsonLdData.class);

		ImdbMovie movie = new ImdbMovie();

		movie.id = parseId(data.url);
		movie.title = data.name;
		movie.poster = data.image;
		movie.rating = data.aggregateRating != null ? data.aggregateRating.ratingValue : null;
		movie.genres = data.genre;
		movie.year = parseYear(data.datePublished);

		return movie;
	}

	String parseId(String url) {
		try {
			Matcher matcher = urlPattern.matcher(url);
			if (matcher.matches())
				return matcher.group(1);
		} catch (NullPointerException e) {
			log.debug("No movie url found (null)");
		} catch (Exception e) {
			log.error("Couldn't parse an id from url {}", url);
		}

		return null;
	}

	Integer parseYear(String date) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateParsingFormat.parse(date));
			return calendar.get(Calendar.YEAR);
		} catch (NullPointerException e) {
			log.debug("No movie date found (null)");
		} catch (Exception e) {
			log.error("Couldn't parse a year from {}", date);
		}

		return null;
	}
}
