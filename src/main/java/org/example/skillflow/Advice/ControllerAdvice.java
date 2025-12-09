package org.example.skillflow.Advice;

import jakarta.validation.ConstraintViolationException;
import org.example.skillflow.API.APIException;
import org.example.skillflow.API.APIResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.sql.SQLIntegrityConstraintViolationException;


    @RestControllerAdvice
    public class ControllerAdvice {

        @ExceptionHandler(value = APIException.class)
        public ResponseEntity<?> APIException(APIException apiExecption) {
            String message = apiExecption.getMessage();
            return ResponseEntity.status(400).body(new APIResponse(message));
        }

        @ExceptionHandler(value = MethodArgumentNotValidException.class)
        public ResponseEntity<?> ValidException(MethodArgumentNotValidException e){
            String error = e.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new APIResponse(error));
        }

        @ExceptionHandler(value = HttpMessageNotReadableException.class)
        public ResponseEntity<?> HttpMessageNotReadable(){
            return ResponseEntity.status(400).body(new APIResponse("your input not readable"));
        }

        @ExceptionHandler(value = NoResourceFoundException.class)
        public ResponseEntity<?> urlError(){
            return ResponseEntity.status(404).body(new APIResponse("wrong Url"));
        }

        @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
        public ResponseEntity<?> SQLError(APIException apiExecption){
            return ResponseEntity.status(400).body(new APIResponse(apiExecption.getMessage()));
        }

        @ExceptionHandler(value = DataIntegrityViolationException.class)
        public ResponseEntity<?> DataIntegrity(){
            return ResponseEntity.status(400).body(new APIResponse("make sure the entered data is correct"));
        }


        @ExceptionHandler(value = ConstraintViolationException.class)
        public ResponseEntity<?> ConstraintViolationException(ConstraintViolationException e){
            String error = e.getConstraintViolations().iterator().next().getMessage();
            return ResponseEntity.status(400).body(new APIResponse(error));
        }


        @ExceptionHandler(value = NullPointerException.class)
        public ResponseEntity<?> NullPointerException(NullPointerException e){
            return ResponseEntity.status(400).body(new APIResponse(e.getMessage()));
        }

    }

