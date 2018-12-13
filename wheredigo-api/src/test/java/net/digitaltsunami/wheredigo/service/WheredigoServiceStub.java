package net.digitaltsunami.wheredigo.service;

import net.digitaltsunami.wheredigo.model.Spend;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by dhagberg on 6/18/17.
 */
@Profile("stub")
@Service
public class WheredigoServiceStub implements WheredigoService {
    private List<Spend> spends = new ArrayList() {{
        add(new Spend("id_1", ZonedDateTime.now(), new BigDecimal(3.14), "cat_1", "sub_cat_1", "Some notes 1", "vendor1"));
        add(new Spend("id_2", ZonedDateTime.now(), new BigDecimal(4.14), "cat_2", "sub_cat_1", "Some notes 2", "vendor1"));
        add(new Spend("id_3", ZonedDateTime.now(), new BigDecimal(5.14), "cat_1", "sub_cat_2", "Some notes 3", "vendor2"));
    }};

    @Override
    public Spend recordSpend(Spend spend) {
        String newId = String.format("id_%d", spends.size() + 1);
        spend.setId(newId);
        spends.add(spend);
        return spend;
    }

    @Override
    public Spend updateSpend(Spend spend) {
        return spend;
    }

    @Override
    public Iterable<Spend> findAll() {
        return spends;
    }

    @Override
    public Iterable<Spend> findLatest() {
        return spends.stream()
                .sorted(Comparator.comparing(Spend::getTransDate))
                .limit(10)
                .collect(toList());
    }

    @Override
    public Spend findById(String id) {
        return spends.stream()
                .filter(s -> Objects.equals(id, s.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Iterable<Spend> findAllByCategory(String category) {
        return spends.stream()
                .filter(s -> Objects.equals(category, s.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Spend> findAllByFilter(String category, String subcategory, String vendor, String noteFilter) {
        return spends.stream()
                .filter(s -> Objects.equals(category, s.getCategory()) && Objects.equals( subcategory, s.getSubcategory()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        spends.removeIf(s -> s.getId().equals(id));
    }

//    @Override
//    public Spend save(Spend spend) {
//        return spend;
//    }

    @Override
    public void delete(Spend spend) {
    }
}
