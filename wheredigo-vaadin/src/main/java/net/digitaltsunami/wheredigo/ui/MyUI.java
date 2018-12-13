package net.digitaltsunami.wheredigo.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;
import net.digitaltsunami.wheredigo.model.Spend;
import net.digitaltsunami.wheredigo.service.WheredigoService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Theme("valo")
@SpringUI
public class MyUI extends UI implements Action.Handler {

    @Autowired
    private WheredigoService service;
    private SpendForm form;
    private Grid<Spend> grid = new Grid<>();
    private HeaderRow filteringHeader;
    private String catFilter;
    private String subCatFilter;
    private String vendorFilter;
    private String noteFilter;

    private Action actionNew;
    private Action actionDup;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();

        initActions();

        layout.setSizeFull();
        form.setVisible(false);

        // Tool Bar
        Button addTransactionBtn = getAddTransButton();
        Button deleteTransactionBtn = getDeleteTransButton();
        deleteTransactionBtn.setEnabled(false);
        HorizontalLayout toolBar = new HorizontalLayout(addTransactionBtn, deleteTransactionBtn);

        // Transaction Grid
        addSpendColumns();
        grid.getEditor()
                .setEnabled(true)
                .setBuffered(true)
                .addSaveListener(new EditorSaveListener<Spend>() {
                    @Override
                    public void onEditorSave(EditorSaveEvent<Spend> event) {
                        service.updateSpend(event.getBean());
                        updateTransList();

                    }
                });

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();
        grid.setSortOrder(GridSortOrder
                .desc(grid.getColumn("Date")));

        grid.asSingleSelect().addValueChangeListener(event -> {
            deleteTransactionBtn.setEnabled(!grid.asSingleSelect().isEmpty());
        });

        // Layout for grid and edit form.
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        main.setExpandRatio(grid, 1);

        VerticalLayout all = new VerticalLayout(toolBar);
        all.addComponentsAndExpand(main);
        all.setSizeFull();
        layout.addComponents(all);

        // fetch list of Customers from service and assign it to Grid
        updateTransList();

        setColumnFiltering();

        setContent(layout);
        addActionHandler(this);
    }

    private void addSpendColumns() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Binder<Spend> binder = grid.getEditor().getBinder();
        TextField amountField = new TextField();
        TextField categoryField = new TextField();
        TextField subcategoryField = new TextField();
        TextField vendorField = new TextField();
        TextField noteField = new TextField();
        Binder.Binding<Spend, BigDecimal> amountBinder = binder
                .forField(amountField)
                .withConverter(new StringToBigDecimalConverter("Unable to convert to BigDecimal"))
                .bind(Spend::getAmount, Spend::setAmount);
        grid.setItems(Collections.emptyList());
        grid.addColumn(Spend::getTransDate, df::format)
                .setId("Date")
                .setCaption("Date");
        grid.addColumn(Spend::getAmount)
                .setId("Amount")
                .setCaption("Amount")
                .setEditorBinding(amountBinder)
                .setRenderer(new NumberRenderer(NumberFormat.getCurrencyInstance()))
                .setStyleGenerator(s -> "v-align-right");
        grid.addColumn(Spend::getCategory)
                .setEditorComponent(categoryField, Spend::setCategory)
                .setId("Category")
                .setCaption("Category");
        grid.addColumn(Spend::getSubcategory)
                .setEditorComponent(subcategoryField, Spend::setSubcategory)
                .setId("Subcategory")
                .setCaption("Subcategory");
        grid.addColumn(Spend::getVendor)
                .setEditorComponent(vendorField, Spend::setVendor)
                .setId("Vendor")
                .setCaption("Vendor");
        grid.addColumn(Spend::getNote)
                .setEditorComponent(noteField, Spend::setNote)
                .setId("Notes")
                .setCaption("Notes");
    }

    private Button getAddTransButton() {
        Button addTransactionBtn = new Button("Record new transaction");
        addTransactionBtn.addClickListener(e -> addSpendAction());
        return addTransactionBtn;
    }

    private void addSpendAction() {
        grid.asSingleSelect().clear();
        Spend newSpend = new Spend();
        newSpend.amount = BigDecimal.valueOf(0.0);
        newSpend.transDate = LocalDateTime.now().atZone(ZoneId.systemDefault());

        form.setSpend(newSpend);
        form.setVisible(true);
    }

    private void duplicateCurrentAction() {
        Spend selectedSpend = grid.asSingleSelect().getValue();
        if (selectedSpend != null) {
            grid.asSingleSelect().clear();
            Spend newSpend = new Spend();
            newSpend.amount = selectedSpend.getAmount();
            newSpend.transDate = LocalDateTime.now().atZone(ZoneId.systemDefault());
            newSpend.category = selectedSpend.getCategory();
            newSpend.subcategory = selectedSpend.getSubcategory();
            newSpend.note = selectedSpend.getNote();
            newSpend.vendor = selectedSpend.getVendor();
            form.setSpend(newSpend);
            form.setVisible(true);
        }
    }

    private Button getDeleteTransButton() {
        Button deleteTransactionBtn = new Button("Delete transaction");
        deleteTransactionBtn.addClickListener(e -> {
            Spend selectedSpend = grid.asSingleSelect().getValue();
            if (selectedSpend != null) {
                service.delete(selectedSpend);
                grid.asSingleSelect().clear();
                updateTransList();
            }
        });
        return deleteTransactionBtn;
    }

    protected void updateTransList() {

        Iterable<Spend> transactions;
        catFilter = isEmpty(catFilter) ? null : catFilter;
        subCatFilter = isEmpty(subCatFilter) ? null : subCatFilter;
        vendorFilter = isEmpty(vendorFilter) ? null : vendorFilter;
        noteFilter = isEmpty(noteFilter) ? null : noteFilter;
        boolean filterOff = isEmpty(catFilter)
                && isEmpty(subCatFilter)
                && isEmpty(vendorFilter)
                && isEmpty(noteFilter);

        if (filterOff) {
            transactions = service.findAll();
        } else {
            transactions = service.findAllByFilter(catFilter, subCatFilter, vendorFilter, noteFilter);
        }
        List<Spend> spendList = new ArrayList<>();
        transactions.forEach(spendList::add);
        grid.setItems(spendList);
    }

    @PostConstruct
    protected void completeUI() {
        form = new SpendForm(this, service);
    }

    private void setColumnFiltering() {
        if (filteringHeader == null) {
            filteringHeader = grid.appendHeaderRow();

            // Add new TextFields to each column which filters the data from
            // that column
            TextField catFilteringField = getColumnFilterField();
            catFilteringField.addValueChangeListener(event -> {
                catFilter = catFilteringField.getValue();
                updateTransList();
            });
            filteringHeader.getCell("Category")
                    .setComponent(catFilteringField);

            TextField subCatFilteringField = getColumnFilterField();
            subCatFilteringField.addValueChangeListener(event -> {
                subCatFilter = subCatFilteringField.getValue();
                updateTransList();
            });
            filteringHeader.getCell("Subcategory")
                    .setComponent(subCatFilteringField);

            TextField vendorFilteringField = getColumnFilterField();
            vendorFilteringField.addValueChangeListener(event -> {
                vendorFilter = vendorFilteringField.getValue();
                updateTransList();
            });
            filteringHeader.getCell("Vendor")
                    .setComponent(vendorFilteringField);

            TextField noteFilteringField = getColumnFilterField();
            noteFilteringField.addValueChangeListener(event -> {
                noteFilter = noteFilteringField.getValue();
                updateTransList();
            });
            filteringHeader.getCell("Notes")
                    .setComponent(noteFilteringField);
        }
    }

    private void initActions() {
        actionNew = new ShortcutAction("Ctrl+N",
                ShortcutAction.KeyCode.N,
                new int[]{ShortcutAction.ModifierKey.CTRL});

        actionDup = new ShortcutAction("Ctrl+D",
                ShortcutAction.KeyCode.D,
                new int[]{ShortcutAction.ModifierKey.CTRL});
    }


    public Action[] getActions(Object target, Object sender) {
        return new Action[]{actionNew, actionDup};
    }

    public void handleAction(Action action, Object sender, Object target) {
        if (action == actionNew) {
            addSpendAction();
        } else if (action == actionDup) {
            duplicateCurrentAction();
        }

    }

    private TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setPlaceholder("Filter");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        return filter;
    }

}
