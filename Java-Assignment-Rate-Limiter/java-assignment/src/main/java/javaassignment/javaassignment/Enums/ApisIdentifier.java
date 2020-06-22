package javaassignment.javaassignment.Enums;

import javaassignment.javaassignment.Constants.APIConstants;
import javaassignment.javaassignment.Constants.UtilityConstants;

public enum ApisIdentifier {

  API_GET_DEVELOPER_ALL(APIConstants.API_METHOD_GET+ UtilityConstants.HYPHEN+APIConstants.API_BASE+APIConstants.API_DEVELOPER),
  API_GET_DEVELOPER_ONE(APIConstants.API_METHOD_GET+ UtilityConstants.HYPHEN+APIConstants.API_BASE+APIConstants.API_DEVELOPER+APIConstants.API_APPEND_USER_ID),
  API_GET_ORGANIZATION_ALL(APIConstants.API_METHOD_GET+ UtilityConstants.HYPHEN+APIConstants.API_BASE+APIConstants.API_ORGANIZATION),;

  ApisIdentifier(String s) {

  }
}
