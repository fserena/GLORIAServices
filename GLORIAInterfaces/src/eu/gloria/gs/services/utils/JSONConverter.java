/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class JSONConverter {

	private static ObjectMapper mapper;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	static {
		mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
	}

	public static String toJSON(Object obj) throws JsonGenerationException,
			JsonMappingException, IOException {

		String json = mapper.writeValueAsString(obj);

		return json;
	}

	public static Object fromJSON(String str, Class<?> cl, Class<?> el)
			throws JsonParseException, JsonMappingException, IOException {

		Object value = null;

		if (str == null) {
			return cl.cast(value);
		}

		if (el == null) {
			if (cl.equals(Date.class)) {
				try {
					value = Long.parseLong(str);
					value = mapper.readValue(str, cl);
					
				} catch (NumberFormatException e) {
					try {
						value = dateFormat.parse(str);
					} catch (ParseException e1) {
						value = mapper.readValue(str, cl);
					}
				}
			} else {
				value = mapper.readValue(str, cl);
			}
		} else {
			JavaType type = mapper.getTypeFactory().constructCollectionType(
					(Class<? extends Collection>) cl, el);

			value = mapper.readValue(str, type);
		}

		return value;
	}
}
