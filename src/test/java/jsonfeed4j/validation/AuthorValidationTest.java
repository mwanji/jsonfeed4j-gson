package jsonfeed4j.validation;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.jupiter.api.Test;

import jsonfeed4j.Item;
import jsonfeed4j.JsonFeed;
import jsonfeed4j.gson.GsonJsonFeedReader;

public class AuthorValidationTest {
  
  @Test
  public void should_fail_author_with_no_info() throws Exception {
    List<Item> items = read("author_validation").getItems();
    
    Utils.assertInvalid(items.get(0).getAuthor().get());
  }
  
  private JsonFeed read(String feed) {
    return new GsonJsonFeedReader().read(new InputStreamReader(this.getClass().getResourceAsStream("/validation/" + feed + ".json")));
  }

}
