package javaassignment.javaassignment.Validators;

import java.util.function.Predicate;

public class HeaderValidator {

  private static final Predicate<String> notNull = (s-> s!= null && !s.isEmpty());

  public static boolean userIdValidator(String input)
  {
      return notNull.test(input);
  }


}
