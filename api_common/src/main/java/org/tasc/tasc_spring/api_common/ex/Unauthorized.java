package org.tasc.tasc_spring.api_common.ex;



public class Unauthorized extends Exception {
   public Unauthorized(int statusCode, String message) {
       super(message,statusCode);
   }
}
