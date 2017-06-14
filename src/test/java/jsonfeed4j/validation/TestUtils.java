package jsonfeed4j.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.Assertions;

public class TestUtils {
  private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  
  static void assertInvalid(Object o, Class<?>... groups) {
    assertFalse(validator.validate(o, groups).isEmpty(), "Object was not INVALID");
  }
  
  static void assertInvalidProperty(String path, Object o, Class<?>... groups) {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o, groups);
    if (constraintViolations.isEmpty()) {
      Assertions.fail("No constraint violation on field " + path);
      return;
    }
    
    ConstraintViolation<Object> constraintViolation = constraintViolations.iterator().next();
    assertEquals(path, constraintViolation.getPropertyPath().toString());
  }

  static void assertValid(Object o, Class<?>... groups) {
    assertTrue(validator.validate(o, groups).isEmpty(), "Object was not valid");
  }
  
  public static void assertEmpty(Optional<?> optional) {
    assertFalse(optional.isPresent());
  }
  
  static Set<ConstraintViolation<Object>> validate(Object o) {
    return validator.validate(o);
  }

  private TestUtils() {}
}
