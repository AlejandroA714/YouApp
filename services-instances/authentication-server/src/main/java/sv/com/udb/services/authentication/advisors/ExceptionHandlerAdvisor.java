package sv.com.udb.services.authentication.advisors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerAdvisor {
   @ExceptionHandler(MethodArgumentNotValidException.class)
   protected ResponseEntity<Map> handleMethodArgumentNotValid(
         MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach((error) -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });
      // x.
      // String bodyOfResponse = ex.handleMethodArgumentNotValid();
      // return new ResponseEntity(errorMessage, headers, status);
      // BindingResult result = ex.getBindingResult();
      return ResponseEntity.badRequest().body(errors);
      // List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
      // .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(),
      // f.getCode()))
      // .collect(Collectors.toList());
   }
}