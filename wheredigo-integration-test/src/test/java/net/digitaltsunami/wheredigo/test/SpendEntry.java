package net.digitaltsunami.wheredigo.test;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SpendEntry {
    String id;
    ZonedDateTime date;
    BigDecimal amount;
    String category;
    String subcategory;
    String vendor;
    String note;
}
