package sv.com.udb.youapp.services.advisors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import sv.com.udb.youapp.services.models.Error;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {
   @NonNull
   private ObjectMapper objectMapper;

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<Error> handlingRemoteCoreException(
         MethodArgumentNotValidException ex, WebRequest request) {
      LOGGER.error("Ocurrio un error en {}", request.getContextPath(), ex);
      var x = Error.builder().message("validation.error")
            .status(HttpStatus.NOT_ACCEPTABLE.value())
            .exception(ex.getClass().getName());
      if (null != ex.getBindingResult()) {
         x.errors(ex.getBindingResult().getAllErrors());
      }
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(x.build());
   }

   @ExceptionHandler(ResponseStatusException.class)
   public ResponseEntity<Error> handlingResponseStatusException(
         ResponseStatusException e, WebRequest request) {
      if (LOGGER.isTraceEnabled()) {
         LOGGER.error("Handling error", e);
      }
      return ResponseEntity.status(e.getStatus())
            .body(Error.builder().message(e.getReason())
                  .handler(getClass().getName()).status(e.getStatus().value())
                  .exception(e.getClass().getName()).build());
   }

   @ExceptionHandler(RuntimeException.class)
   public ResponseEntity<Error> handleRuntime(RuntimeException e,
         WebRequest request) {
      LOGGER.error("Ocurrio un error en {}", request.getContextPath(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Error.builder().message(e.getMessage())
                  .handler(getClass().getName())
                  .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                  .exception(e.getClass().getName()).build());
   }

   @ExceptionHandler({ Exception.class })
   @ResponseBody
   public ResponseEntity<Error> handleGeneral(Exception e, WebRequest request) {
      LOGGER.error("Ocurrio un error en {}", request.getContextPath(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Error.builder().message(e.getMessage())
                  .handler(getClass().getName())
                  .path(request.getDescription(false))
                  .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                  .exception(e.getClass().getName()).build());
   }
}
