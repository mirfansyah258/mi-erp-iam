package com.mi.iam.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mi.iam.models.dto.Response;

public class ResponseHandler {
  public static ResponseEntity<Object> generateResponse(HttpStatus code, String message, Object responseObj) {
    Response res = new Response();
    if(code.value() >= 400) {
      res.setError(responseObj);
    } else {
      res.setData(responseObj);
    }
    res.setMessage(message);
    res.setVersion("0.0.1");

    return new ResponseEntity<Object>(res, code);
  }
}