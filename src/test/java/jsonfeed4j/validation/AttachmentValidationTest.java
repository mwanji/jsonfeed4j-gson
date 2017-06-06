package jsonfeed4j.validation;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.jupiter.api.Test;

import jsonfeed4j.Attachment;
import jsonfeed4j.gson.GsonJsonFeedReader;

public class AttachmentValidationTest {
  
  @Test
  public void should_fail_on_invalid_mime_type() throws Exception {
    Attachment attachment = attachments().get(0);
    
    TestUtils.assertInvalidProperty("mimeType", attachment);
  }
  
  @Test
  public void should_fail_on_missing_url() throws Exception {
    Attachment attachment = attachments().get(3);
    
    TestUtils.assertInvalidProperty("url", attachment);
  }
  
  @Test
  public void should_fail_on_empty_url() throws Exception {
    Attachment attachment = attachments().get(1);
    
    TestUtils.assertInvalidProperty("url", attachment);
  }
  
  private List<Attachment> attachments() {
    return new GsonJsonFeedReader().read(new InputStreamReader(this.getClass().getResourceAsStream("/attachmentsLevel/attachments.json"))).getItems().get(2).getAttachments();
  }

}
