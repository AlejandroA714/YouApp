package sv.com.udb.youapp.services.models;

import java.util.Date;
import java.util.List;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Error {
   private String            code;
   private String            errorMessage;
   private String            message;
   private String            developerCode;
   private String            developerMessage;
   @Default
   private Date              timestamp = new Date();
   private String            path;
   private String            exception;
   private int               status;
   private Throwable         throwable;
   @Singular
   private List<String>      arguments;
   @Singular
   private List<ObjectError> errors;
   private String            handler;
}