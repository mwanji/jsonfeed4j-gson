package jsonfeed4j.gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Path;

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
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import jsonfeed4j.JsonFeed;
import jsonfeed4j.JsonFeedReader;
import jsonfeed4j.Versions;

public class GsonJsonFeedReader implements JsonFeedReader {
  
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Versions.class, new VersionsDeserializer())
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();
  
  @Override
  public JsonFeed read(String jsonFeed) {
    return gson.fromJson(jsonFeed, JsonFeed.class);
  }

  @Override
  public JsonFeed read(Path jsonFeed) {
    return null;
  }

  @Override
  public JsonFeed read(InputStream jsonFeed) {
    return read(new InputStreamReader(jsonFeed));
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

  private static class VersionsDeserializer implements JsonDeserializer<Versions> {

    @Override
    public Versions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return Versions.fromUrl(json.getAsString());
    }
  }
}
