package jsonfeed4j.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.Validation;
import javax.validation.Validator;

class Utils {
  private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  
  static void assertInvalid(Object o) {
    assertFalse(validator.validate(o).isEmpty(), "Object was not INVALID");
  }

  static void assertValid(Object o) {
    assertTrue(validator.validate(o).isEmpty(), "Object was not valid");
  }

  private Utils() {}
}
