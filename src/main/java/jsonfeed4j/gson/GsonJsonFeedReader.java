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
    
    JsonObject output = new JsonObject();
    JsonObject extensions = new JsonObject();
    output.add("extensions", extensions);
    JsonObject root = new JsonParser().parse(jsonFeed).getAsJsonObject();
    for (String key : root.keySet()) {
      if (key.startsWith("_")) {
        extensions.add(key.substring(1), root.get(key));
      } else {
        output.add(key, root.get(key));
      }
    }
    
    return gson.fromJson(output, JsonFeed.class);
    
//    try (JsonReader reader = new JsonReader(jsonFeed)) {
//      JsonObject output = new JsonObject();
//      String currentName = null;
//      JsonArray currentArray = null;
//      
//      while (reader.hasNext()) {
//        JsonToken jsonToken = reader.peek();
//        switch (jsonToken) {
//        case NAME:
//          currentName = toCamelCase(reader.nextName());
//          break;
//        case BEGIN_ARRAY:
//          reader.beginArray();
//          currentArray = new JsonArray();
//          break;
//        case END_ARRAY:
//          reader.endArray();
//          output.add(currentName, currentArray);
//        case STRING:
//          output.add(currentName, new JsonPrimitive(reader.nextString()));
//          break;
//        case BOOLEAN:
//          output.add(currentName, new JsonPrimitive(reader.nextBoolean()));
//          break;
//        case BEGIN_OBJECT:
//          reader.beginObject();
//          break;
//        case END_DOCUMENT:
//          break;
//        case END_OBJECT:
//          reader.endObject();
//          break;
//        case NULL:
//          reader.nextNull();
//          break;
//        case NUMBER:
//          break;
//        default:
//          break;
//        }
//      }
//      System.out.println(output);
//      return gson.fromJson(output, JsonFeed.class);
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
    
    
//    return gson.fromJson(jsonFeed, JsonFeed.class);
  }

  private static class VersionsDeserializer implements JsonDeserializer<Versions> {

    @Override
    public Versions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return Versions.fromUrl(json.getAsString());
    }
  }
}
