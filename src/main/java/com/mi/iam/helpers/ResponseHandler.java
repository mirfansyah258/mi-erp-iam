package com.mi.iam.helpers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
  public static ResponseEntity<Object> generateResponse(HttpStatus code, String message, Object responseObj) {
    Map<String, Object> map = new HashMap<String, Object>();

    map.put((code.value() >= 400) ? "error" : "data", responseObj);
    map.put("message", message);
    map.put("version", "0.0.1");

    return new ResponseEntity<Object>(map, code);
  }
}