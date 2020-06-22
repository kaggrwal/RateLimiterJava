package javaassignment.javaassignment.Validators;

import org.junit.Assert;
import org.junit.Test;

public class HeaderValidatorTest {


  @Test
  public void userIdValidatorTest_NonEmptyUserID()
  {
    Assert.assertTrue(HeaderValidator.userIdValidator("userid112"));
  }

  @Test
  public void userIdValidatorTest_EmptyUserID()
  {
    Assert.assertFalse(HeaderValidator.userIdValidator(""));
  }

  @Test
  public void userIdValidatorTest_NullUserID()
  {
    Assert.assertFalse(HeaderValidator.userIdValidator(null));
  }

}
