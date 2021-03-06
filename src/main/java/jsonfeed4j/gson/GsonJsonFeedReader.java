package jsonfeed4j.gson;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import jsonfeed4j.JsonFeed;
import jsonfeed4j.JsonFeedReader;
import jsonfeed4j.Version;

public class GsonJsonFeedReader implements JsonFeedReader {
  
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Version.class, new VersionDeserializer())
      .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer())
      .registerTypeAdapter(MimeType.class, new MimeTypeDeserializer())
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();
  
  @Override
  public JsonFeed read(String jsonFeed) {
    return read(new StringReader(jsonFeed));
  }

  @Override
  public JsonFeed read(Reader jsonFeed) {
    
    JsonObject root = new JsonParser().parse(jsonFeed).getAsJsonObject();
    JsonObject output = populate(root);
    
    return gson.fromJson(output, JsonFeed.class);
  }

  private JsonObject populate(JsonObject root) {
    JsonObject output = new JsonObject();
    JsonObject extensions = new JsonObject();
    output.add("extensions", extensions);
    
    for (String key : root.keySet()) {
      JsonElement element = root.get(key);
      if (key.startsWith("_")) {
        extensions.add(key.substring(1), root.get(key));
      } else if (element.isJsonObject()) {
        JsonObject nestedObject = populate(element.getAsJsonObject());
        output.add(key, nestedObject);
      } else if (element.isJsonArray()) {
        JsonArray nestedArray = populate(element.getAsJsonArray());
        output.add(key, nestedArray);
      } else {
        output.add(key, root.get(key));
      }
    }
    
    return output;
  }
  
  private JsonArray populate(JsonArray root) {
    JsonArray output = new JsonArray();
    for (JsonElement element : root) {
      if (element.isJsonObject()) {
        JsonObject nestedObject = populate(element.getAsJsonObject());
        output.add(nestedObject);
      } else {
        output.add(element);
      }
    }
    
    return output;
  }

  private static class VersionDeserializer implements JsonDeserializer<Version> {
    @Override
    public Version deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return Version.fromUrl(json.getAsString());
    }
  }
  
  private static class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      try {
        return ZonedDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(json.getAsString()));
      } catch (DateTimeException e) {
        return null;
      }
    }
  }
  
  private static class MimeTypeDeserializer implements JsonDeserializer<MimeType> {
    @Override
    public MimeType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      try {
        return new MimeType(json.getAsString());
      } catch (MimeTypeParseException e) {
        return null;
      }
    }
  }
}
