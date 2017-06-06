package jsonfeed4j.validation;

import static jsonfeed4j.validation.TestUtils.assertInvalidProperty;
import static jsonfeed4j.validation.TestUtils.assertValid;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.jupiter.api.Test;

import jsonfeed4j.Item;
import jsonfeed4j.JsonFeed;
import jsonfeed4j.gson.GsonJsonFeedReader;

public class ItemValidationTest {
  
  @Test
  public void should_validate_with_only_html_content() throws Exception {
    List<Item> items = read("item_validation").getItems();
    
    assertValid(items.get(0));
  }

  @Test
  public void should_validate_with_only_text_content() throws Exception {
    List<Item> items = read("item_validation").getItems();
    
    assertValid(items.get(1));
  }

  @Test
  public void should_validate_with_both_contents() throws Exception {
    List<Item> items = read("item_validation").getItems();
    
    assertValid(items.get(2));
  }

  @Test
  public void should_fail_with_no_content() throws Exception {
    Item item = read("item_validation").getItems().get(3);
    assertInvalidProperty("", item);
  }
  
  @Test
  public void should_fail_with_no_id() throws Exception {
    Item item = read("item_validation").getItems().get(4);
    
    assertInvalidProperty("id", item);
  }
  
  @Test
  public void should_fail_with_invalid_author() throws Exception {
    Item item = read("author_validation").getItems().get(0);
    
    assertInvalidProperty("author", item);
  }
  
  @Test
  public void should_validate_with_no_author() throws Exception {
    Item item = read("author_validation").getItems().get(5);
    
    assertValid(item);
  }
  
  private JsonFeed read(String feed) {
    return new GsonJsonFeedReader().read(new InputStreamReader(this.getClass().getResourceAsStream("/validation/" + feed + ".json")));
  }
}
