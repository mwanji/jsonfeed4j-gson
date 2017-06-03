package jsonfeed4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import jsonfeed4j.gson.GsonJsonFeedReader;

public class AttachmentsLevelTest {
  
  @Test
  public void should_read_all_fields() throws Exception {
    Attachment attachment = feed("attachments").getItems().get(0).getAttachments().get(0);
    
    assertEquals("http://moandjiezana.com/1/attachments/full", attachment.getUrl());
    assertEquals("audio/mpeg", attachment.getMimeType().getBaseType());
    assertEquals("Title 1", attachment.getTitle().get());
    assertEquals(120, attachment.getDurationInSeconds().get().intValue());
    assertEquals(12345678, attachment.getSizeInBytes().get().intValue());
  }
  
  @Test
  public void should_leave_optional_fields_empty() throws Exception {
    Attachment attachment = feed("attachments").getItems().get(0).getAttachments().get(1);
    
    assertNotPresent(attachment.getTitle());
    assertNotPresent(attachment.getDurationInSeconds());
    assertNotPresent(attachment.getSizeInBytes());
  }
  
  @Test
  public void should_read_extensions() throws Exception {
    Extensions extensions = feed("attachments").getItems().get(0).getAttachments().get(2).getExtensions();
    Map<String, Object> colorsExtension = extensions.getMap("colors");
    List<Object> users = extensions.getList("users");
    
    assertEquals("red", colorsExtension.get("background"));
    assertEquals("blue", colorsExtension.get("text"));
    assertEquals("a/b/c", extensions.get("pluginPath"));
    assertEquals(Arrays.asList("alex", "brian", "cathy"), users);
  }
  
  private JsonFeed feed(String feed) {
    return new GsonJsonFeedReader().read(new InputStreamReader(this.getClass().getResourceAsStream("/attachmentsLevel/" + feed + ".json")));
  }
  
  private void assertNotPresent(Optional<?> optional) {
    assertFalse(optional.isPresent());
  }

}
