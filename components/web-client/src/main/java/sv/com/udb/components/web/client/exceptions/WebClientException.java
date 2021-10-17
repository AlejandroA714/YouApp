package sv.com.udb.components.web.client.exceptions;

import lombok.Builder.Default;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.Serial;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
public class WebClientException extends WebClientResponseException {
   @Serial
   private static final long serialVersionUID = -8611927138213749807L;
   private HttpStatus        internalCode; // BAD_REQUEST
   private String            rawInternalCode;
   private String            rawBody;
   private JSONObject        responseBody;
   @Default
   private LocalDateTime     timestamp        = LocalDateTime.now();

   public WebClientException(int statusCode, String statusText,
         HttpHeaders headers, byte[] body, Charset charset) {
      super(statusCode, statusText, headers, body, charset);
   }

   public WebClientException(WebClientResponseException ex, String message,
         Object body) {
      super(message, HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getHeaders(),
            ex.getResponseBodyAsByteArray(), null, ex.getRequest());
      this.internalCode = ex.getStatusCode();
      this.rawInternalCode = ex.getStatusText();
      this.rawBody = bodyToStr(body);
      this.responseBody = new JSONObject(ex.getResponseBodyAsString());
   }

   public String getInternalMessage() {
      return this.responseBody.getString("message");
   }

   public Object getFromResponseBody(String property) {
      return this.responseBody.get(property);
   }

   protected String bodyToStr(Object args) {
      String rawBody = "";
      if (args == null) return rawBody;
      if (args instanceof Collection) {
         String rawCollection = "Collection[";
         Collection var = (Collection) args;
         var d = var.iterator();
         while (d.hasNext()) {
            rawCollection += d.next().toString();
            if (d.hasNext()) rawCollection += " , ";
         }
         rawCollection += "]";
         rawBody += rawCollection;
      }
      else
         rawBody = args.toString();
      return rawBody;
   }

   @Override
   public String toString() {
      return String.format("WebClientException.%s$%s", getRawInternalCode(),
            getMessage());
   }
}
