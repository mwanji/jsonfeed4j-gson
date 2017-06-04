package jsonfeed4j.validation;

import static jsonfeed4j.validation.TestUtils.assertInvalid;
import static jsonfeed4j.validation.TestUtils.assertValid;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.jupiter.api.Test;

import jsonfeed4j.Hub;
import jsonfeed4j.JsonFeed;
import jsonfeed4j.gson.GsonJsonFeedReader;

public class HubValidationTest {
  
  private List<Hub> hubs = read().getHubs();

  @Test
  public void should_validate_hub() throws Exception {
    assertValid(hubs.get(0));
  }

  @Test
  public void should_fail_hub_with_no_url() throws Exception {
    assertInvalid(hubs.get(1));
  }

  @Test
  public void should_fail_hub_with_no_type() throws Exception {
    assertInvalid(hubs.get(2));
  }

  @Test
  public void should_fail_hub_with_empty_url_and_type() throws Exception {
    assertInvalid(hubs.get(3));
  }

  @Test
  public void should_fail_hub_with_no_url_or_type() throws Exception {
    assertInvalid(hubs.get(4));
  }
  
  private JsonFeed read() {
    return new GsonJsonFeedReader().read(new InputStreamReader(this.getClass().getResourceAsStream("/validation/hub_validation.json")));
  }
}
