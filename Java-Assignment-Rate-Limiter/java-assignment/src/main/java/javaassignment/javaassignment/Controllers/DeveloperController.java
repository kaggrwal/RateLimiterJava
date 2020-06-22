package javaassignment.javaassignment.Controllers;


import javaassignment.javaassignment.Constants.APIConstants;
import javaassignment.javaassignment.Constants.HeaderConstants;
import javaassignment.javaassignment.Constants.UtilityConstants;
import javaassignment.javaassignment.Enums.ApisIdentifier;
import javaassignment.javaassignment.Exceptions.ApplicationException;
import javaassignment.javaassignment.Utils.RateLimiterUtils;
import javaassignment.javaassignment.Validators.HeaderValidator;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstants.API_BASE + APIConstants.API_DEVELOPER)
public class DeveloperController {

  @Autowired
  RateLimiterUtils rateLimiterUtils;

  @GetMapping
  public ResponseEntity<String> getDevelopers(HttpServletRequest servletRequest)
      throws ApplicationException {

    if(!HeaderValidator.userIdValidator(servletRequest.getHeader(HeaderConstants.HEADER_KEY_X_UUID)))
    {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check userId");
    }
    if (rateLimiterUtils.isAllowed(servletRequest.getHeader(HeaderConstants.HEADER_KEY_X_UUID),
        ApisIdentifier.API_GET_DEVELOPER_ALL)) {

      // Service Call for further processing
      return ResponseEntity.ok(UtilityConstants.REQUEST_ACCEPTED);
    } else {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .body(UtilityConstants.TOO_MANY_REQUESTS);
    }
  }


  @GetMapping(value = APIConstants.API_APPEND_USER_ID)
  public ResponseEntity<String> getDeveloper(HttpServletRequest servletRequest)
      throws ApplicationException {

    if(!HeaderValidator.userIdValidator(servletRequest.getHeader(HeaderConstants.HEADER_KEY_X_UUID)))
    {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check userId");
    }

    if (rateLimiterUtils.isAllowed(servletRequest.getHeader(HeaderConstants.HEADER_KEY_X_UUID),
        ApisIdentifier.API_GET_DEVELOPER_ONE)) {

      // Service Call for further processing
      return ResponseEntity.ok(UtilityConstants.REQUEST_ACCEPTED);
    } else {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .body(UtilityConstants.TOO_MANY_REQUESTS);
    }
  }


}
