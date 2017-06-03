package jsonfeed4j;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import jsonfeed4j.JsonFeed;
import jsonfeed4j.Versions;
import jsonfeed4j.gson.GsonJsonFeedReader;

public class TopLevelTest {

  @Test
  public void should_read_minimal_valid_feed_with_no_items() {
    JsonFeed jsonFeed = feed("minimal");
    
    assertEquals(Versions.VERSION_1, jsonFeed.getVersion());
    assertEquals("My Test Feed", jsonFeed.getTitle());
    assertTrue(jsonFeed.getItems().isEmpty(), "Items should be an empty collection");
  }
  
  @Test
  public void should_get_home_page_url() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertEquals("http://moandjiezana.com", jsonFeed.getHomePageUrl().get());
  }
  
  @Test
  public void should_get_feed_url() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertEquals("http://moandjiezana.com/feed.json", jsonFeed.getFeedUrl().get());
  }
  
  @Test
  public void should_get_feed_description() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertEquals("Some words about this feed", jsonFeed.getDescription().get());
  }
  
  @Test
  public void should_get_user_comment() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertEquals("Here's what you can expect", jsonFeed.getUserComment().get());
  }
  
  @Test
  public void should_get_next_url() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertEquals("http://moandjiezana.com/feed.json?page=2", jsonFeed.getNextUrl().get());
  }
  
  @Test
  public void should_get_icon() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertEquals("http://moandjiezana.com/icon.jpg", jsonFeed.getIcon().get());
  }
  
  @Test
  public void should_get_favicon() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertEquals("http://moandjiezana.com/favicon.jpg", jsonFeed.getFavicon().get());
  }
  
  @Test
  public void should_get_expired() throws Exception {
    JsonFeed jsonFeed = feed("basic_expired");
    
    assertTrue(jsonFeed.isExpired());
  }
  
  @Test
  public void should_get_not_expired() throws Exception {
    JsonFeed jsonFeed = feed("basic");
    
    assertFalse(jsonFeed.isExpired());
  }
  
  @Test
  public void should_get_extensions() throws Exception {
    JsonFeed jsonFeed = feed("extensions");
    
    Map<String, Object> colorsExtension = jsonFeed.getExtensions().getMap("colors");
    List<Object> users = jsonFeed.getExtensions().getList("users");
    
    assertEquals("red", colorsExtension.get("background"));
    assertEquals("blue", colorsExtension.get("text"));
    assertEquals("a/b/c", jsonFeed.getExtensions().get("pluginPath"));
    assertEquals(Arrays.asList("alex", "brian", "cathy"), users);
  }
  
  private JsonFeed feed(String feed) {
    return new GsonJsonFeedReader().read(this.getClass().getResourceAsStream("/topLevel/" + feed + ".json"));
  }
}
