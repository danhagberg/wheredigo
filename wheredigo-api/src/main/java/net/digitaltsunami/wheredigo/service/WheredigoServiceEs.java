package net.digitaltsunami.wheredigo.service;

import net.digitaltsunami.wheredigo.exception.ResourceNotFoundException;
import net.digitaltsunami.wheredigo.model.Spend;
import net.digitaltsunami.wheredigo.model.SpendRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
/**
 * Created by dhagberg on 6/11/17.
 */
@Profile("default")
@Service
public class WheredigoServiceEs implements WheredigoService {

    private final SpendRepository spendRepository;
    private Logger logger = LoggerFactory.getLogger(WheredigoServiceEs.class);

    @Autowired
    public WheredigoServiceEs(SpendRepository spendRepository) {
        this.spendRepository = spendRepository;
    }

    @Override
    public Spend recordSpend(Spend spend) {
        Spend newSpend = spendRepository.save(spend);
        return newSpend;
    }

    @Override
    public Spend updateSpend(Spend spend) {
        return spendRepository.save(spend);
    }

    @Override
    public Iterable<Spend> findAll() {
        return spendRepository.findAll();
    }

    @Override
    public Page<Spend> findLatest() {
        return spendRepository.findAll(PageRequest.of(0, 100, Sort.Direction.DESC, "transDate"));
    }

    @Override
    public Spend findById(String id) {
        return spendRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spend entry not found for ID:" + id));
    }

    @Override
    public Iterable<Spend> findAllByCategory(String category) {
        return spendRepository.findAllByCategory(
                PageRequest.of(0, 1000, Sort.Direction.DESC, "transDate"), category);
    }

    @Override
    public Iterable<Spend> findAllByFilter(String category, String subcategory, String vendor, String note, String tag) {
        return spendRepository.findAllByCategoryAndSubcategoryAndVendorAndNoteAndTags(
                PageRequest.of(0, 1000, Sort.Direction.DESC, "transDate"), category, subcategory, vendor, note, tag);
    }

    @Override
    public void deleteById(String id) {
        if (spendRepository.existsById(id)) {
            spendRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Spend entry not found for ID:" + id);
        }
    }

    @Override
    public void delete(Spend spend) {
        if (spendRepository.existsById(spend.getId())) {
            spendRepository.delete(spend);
        } else {
            throw new ResourceNotFoundException("Spend entry not found for ID:" + spend.getId());
        }
    }
}
