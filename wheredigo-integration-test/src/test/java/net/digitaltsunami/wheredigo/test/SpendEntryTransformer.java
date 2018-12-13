package net.digitaltsunami.wheredigo.test;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;

public class SpendEntryTransformer implements TypeRegistryConfigurer {
    public Locale locale() {
        return Locale.getDefault();
    }

    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(SpendEntry.class,
                        (Map<String, String> row) -> {
                            SpendEntry entry = new SpendEntry();
                            entry.setDate(ZonedDateTime.parse(row.get("date")));
                            entry.setAmount(new BigDecimal(row.get("amount")));
                            entry.setVendor(row.get("vendor"));
                            entry.setNote(row.get("note"));
                            entry.setCategory(row.get("category"));
                            entry.setSubcategory(row.get("subcategory"));

                            return entry;
                        }
                )
        );
    }
}