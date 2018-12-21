package net.digitaltsunami.wheredigo.ui;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.ui.*;
import net.digitaltsunami.wheredigo.model.Spend;
import net.digitaltsunami.wheredigo.service.WheredigoService;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.vaadin.event.ShortcutAction.KeyCode.ENTER;
import static com.vaadin.event.ShortcutAction.KeyCode.ESCAPE;
import static com.vaadin.ui.themes.ValoTheme.BUTTON_PRIMARY;

public class SpendForm extends FormLayout {
    private final WheredigoService wheredigoService;
    private TextField category = new TextField("Category");
    private TextField subcategory = new TextField("Subcategory");
    private TextField amount = new TextField("Amount");
    private DateField transDate = new DateField();
    private TextField note = new TextField("Notes");
    private TextField vendor = new TextField("Vendor");
    private TextField tags = new TextField("Tags");
    private final MyUI myUi;

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");

    private Binder<Spend> binder = new Binder<>(Spend.class);
    private Spend spend;

    public SpendForm(MyUI myUi, WheredigoService wheredigoService) {
        this.myUi = myUi;
        this.wheredigoService = wheredigoService;

        save.setStyleName(BUTTON_PRIMARY);
        save.setClickShortcut(ENTER);

        cancel.setClickShortcut(ESCAPE);

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        addComponents(transDate, amount, category, subcategory, note, vendor, tags, buttons);

        binder.forField(amount)
                .withNullRepresentation("")
                .withConverter(new StringToBigDecimalConverter(new BigDecimal(10.00), "Price must be in ##.## format"))
                .bind(Spend::getAmount, Spend::setAmount);

        binder.forField(transDate)
                .withNullRepresentation(LocalDate.now())
                .withConverter(new Converter<LocalDate, ZonedDateTime>() {
                    @Override
                    public Result<java.time.ZonedDateTime> convertToModel(LocalDate value, ValueContext context) {
                        if (value == null) {
                            return Result.ok(ZonedDateTime.now());
                        }
                        else {
                            return Result.ok(value.atStartOfDay(OffsetDateTime.now().getOffset()));
                        }
                    }

                    @Override
                    public LocalDate convertToPresentation(java.time.ZonedDateTime value, ValueContext context) {
                        return LocalDate.from(value);
                    }
                })
                .bind(Spend::getTransDate, Spend::setTransDate);

        binder.forField(tags)
                .withNullRepresentation("")
                .withConverter(new Converter<String, String[]>() {
                    @Override
                    public Result<String[]> convertToModel(String value, ValueContext context) {
                        if (value == null) {
                            return Result.ok(new String[]{});
                        }
                        else {
                            return Result.ok(
                                    Arrays.stream(value.split(","))
                                            .map(tag -> tag.trim())
                                            .toArray(String[]::new));
                        }
                    }

                    @Override
                    public String convertToPresentation(String[] value, ValueContext context) {
                        if (value == null) {
                            return null;
                        }
                        return Arrays.stream(value)
                                .collect(Collectors.joining(", "));

                    }
                })
                .bind(Spend::getTags, Spend::setTags);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> this.cancel());
    }

    public void setSpend(Spend spend) {
        this.spend = spend;
        binder.setBean(spend);
        setVisible(true);
        transDate.focus();
    }

    private void cancel() {
        setVisible(false);
    }

    private void save() {
        wheredigoService.recordSpend(spend);
        myUi.updateTransList();
        setVisible(false);
    }
}
