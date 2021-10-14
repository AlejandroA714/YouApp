package sv.com.udb.services.authentication.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateAttributeConverter
      implements AttributeConverter<LocalDate, Date> {
   @Override
   public Date convertToDatabaseColumn(LocalDate attribute) {
      return attribute == null ? null : Date.valueOf(attribute);
   }

   @Override
   public LocalDate convertToEntityAttribute(Date dbData) {
      return dbData == null ? null : dbData.toLocalDate();
   }
}
