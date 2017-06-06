package jsonfeed4j.validation;

import static jsonfeed4j.validation.TestUtils.assertInvalidProperty;
import static jsonfeed4j.validation.TestUtils.validate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.junit.jupiter.api.Test;

import jsonfeed4j.JsonFeed;
import jsonfeed4j.gson.GsonJsonFeedReader;

public class TopLevelValidationTest {

  @Test
  public void should_fail_on_missing_version() throws Exception {
    assertInvalidProperty("version", read("missing_version"));
  }

  @Test
  public void should_fail_on_missing_title() throws Exception {
    assertInvalidProperty("title", read("missing_title"));
  }

  @Test
  public void should_fail_on_empty_title() throws Exception {
    assertInvalidProperty("title", read("empty_title"));
  }
  
  @Test
  public void should_fail_on_invalid_nested_objects() {
    Set<ConstraintViolation<Object>> constraintViolations = validate(read("invalid_nested_objects"));
    List<String> paths = constraintViolations.stream().map(ConstraintViolation::getPropertyPath).map(Path::toString).sorted().collect(Collectors.toList());
    
    assertEquals(Arrays.asList("author", "hubs[0].url", "items[0].id"), paths);
  }
  
  private JsonFeed read(String feed) {
    return new GsonJsonFeedReader().read(new InputStreamReader(this.getClass().getResourceAsStream("/validation/" + feed + ".json")));
  }
}
