package sv.com.udb.services.authentication.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter
public class DateConverter implements AttributeConverter<LocalDate, String> {
   private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
         .ofPattern("yyyy-MM-dd");

   @Override
   public String convertToDatabaseColumn(LocalDate attribute) {
      return attribute == null ? null : attribute.format(dateTimeFormatter);
   }

   @Override
   public LocalDate convertToEntityAttribute(String dbData) {
      return dbData == null ? null : LocalDate.parse(dbData, dateTimeFormatter);
   }
}
