package jsonfeed4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
  public void optional_fields_shoud_not_be_present() throws Exception {
    Item item = feed("minimal").getItems().get(0);
    
    assertNotPresent(item.getUrl());
    assertNotPresent(item.getExternalUrl());
    assertNotPresent(item.getTitle());
    assertNotPresent(item.getSummary());
    assertNotPresent(item.getImage());
    assertNotPresent(item.getBannerImage());
    assertNotPresent(item.getDatePublished());
    assertNotPresent(item.getDateModified());
    assertNotPresent(item.getTags());
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
  
  @Test
  public void should_read_url() throws Exception {
    Item item = basicItem();
    
    assertEquals("http://moandjiezana.com/blog/1", item.getUrl().get());
  }
  
  @Test
  public void should_read_content_html() throws Exception {
    Item item = basicItem();
    
    assertEquals("<h1>Some HTML</h1>", item.getContentHtml());
  }
  
  @Test
  public void should_read_summary() throws Exception {
    Item item = basicItem();
    
    assertEquals("My summary", item.getSummary().get());
  }
  
  @Test
  public void should_read_image() throws Exception {
    Item item = basicItem();
    
    assertEquals("http://moandjiezana.com/image.jpg", item.getImage().get());
  }
  
  @Test
  public void should_read_banner_image() throws Exception {
    Item item = basicItem();
    
    assertEquals("http://moandjiezana.com/banner_image.jpg", item.getBannerImage().get());
  }
  
  @Test
  public void should_read_date_published() throws Exception {
    Item item = basicItem();
    
    assertEquals("2010-02-07T14:04:00-05:00", DateTimeFormatter.ISO_DATE_TIME.format(item.getDatePublished().get()));
  }  
  
  @Test
  public void should_read_date_modified() throws Exception {
    Item item = basicItem();
    
    assertEquals("2010-02-08T14:04:00-05:00", DateTimeFormatter.ISO_DATE_TIME.format(item.getDateModified().get()));
  }
  
  @Test
  public void should_read_tags() throws Exception {
    Item item = basicItem();
    
    assertEquals(Arrays.asList("rockets", "mermaids"), item.getTags().get());
  }
  
  private JsonFeed feed(String feed) {
    return new GsonJsonFeedReader().read(new InputStreamReader(this.getClass().getResourceAsStream("/itemsLevel/" + feed + ".json")));
  }
  
  private Item basicItem() {
    return feed("basic").getItems().get(0);
  }
  
  private void assertNotPresent(Optional<?> optional) {
    assertFalse(optional.isPresent());
  }
  
  public static void main(String[] args) {
    new ItemsLevelTest().basicItem();
  }
}
