package jsonfeed4j;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import jsonfeed4j.gson.GsonJsonFeedReader;

public class ItemsLevelTest {

  @Test
  public void should_read_2_items() throws Exception {
    JsonFeed jsonFeed = feed("minimal");
    
    List<Item> items = jsonFeed.getItems();
    
    assertEquals(2, items.size());
    assertEquals("http://moandjiezana.com/1", items.get(0).getId());
    assertEquals("Some text", items.get(0).getContentText());
    assertEquals("http://moandjiezana.com/2", items.get(1).getId());
    assertEquals("Some text 2", items.get(1).getContentText());
  }
  
  @Test
  public void should_read_extensions() throws Exception {
    Extensions extensions = feed("extensions").getItems().get(0).getExtensions();
    
    Map<String, Object> colorsExtension = extensions.getMap("colors");
    List<Object> users = extensions.getList("users");
    
    assertEquals("red", colorsExtension.get("background"));
    assertEquals("blue", colorsExtension.get("text"));
    assertEquals("a/b/c", extensions.get("pluginPath"));
    assertEquals(Arrays.asList("alex", "brian", "cathy"), users);
  }
  
  private JsonFeed feed(String feed) {
    return new GsonJsonFeedReader().read(this.getClass().getResourceAsStream("/itemsLevel/" + feed + ".json"));
  }
}
