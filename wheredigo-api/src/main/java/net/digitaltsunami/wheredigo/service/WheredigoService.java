package net.digitaltsunami.wheredigo.service;

import net.digitaltsunami.wheredigo.exception.ResourceNotFoundException;
import net.digitaltsunami.wheredigo.model.Spend;

/**
 * Service for interacting with the Spend repository.
 * Created by dhagberg on 6/11/17.
 */
public interface WheredigoService {
    /**
     * Record a transaction entry for the provided Spend object.  An instance of Spend will be returned with the ID
     * value populated.  Other fields may be modified.
     *
     * @param spend Transaction data to record.
     * @return Recorded transaction data with populated ID of persisted entry.
     */
    Spend recordSpend(Spend spend);

    /**
     * Update an transaction entry with the provided Spend object.  The instance of Spend will overwrite the
     * current values; therefore, it should have all fields populated that are relevant to the transaction.
     * The ID value must be populated so the entry to update can be found.
     * The entry will be returned.  As a side effect of the updates, other fields may be modified.
     *
     * @param spend Transaction data to record.  Must include ID of entry to modify.
     * @return Recorded transaction data with current values.
     * @throws ResourceNotFoundException if no entry for for the provided ID
     */
    Spend updateSpend(Spend spend) throws ResourceNotFoundException;

    /**
     * Return all spend entries.
     *
     * @return Iterable of 0 to n spend entries.
     */
    Iterable<Spend> findAll();

    /**
     * @return
     * @TODO This is used by the ES repo. Figure this out
     */
    Iterable<Spend> findLatest();

    /**
     * Return the spend transaction for the provided ID.
     *
     * @param id String value for the ID
     * @return Fully populated instance of Spend with all currently persisted data.
     * @throws ResourceNotFoundException if no entry for the provided ID.
     */
    Spend findById(String id) throws ResourceNotFoundException;

    /**
     * Return all spend entries matching a given category.
     *
     * @param category
     * @return Iterable of 0 to n spend entries matching the category.
     */
    Iterable<Spend> findAllByCategory(String category);

    /**
     * @TODO Comment this
     * @param category
     * @param subcategory
     * @param vendor
     * @param noteFilter
     * @param tag
     * @return
     */
    Iterable<Spend> findAllByFilter(String category, String subcategory, String vendor, String noteFilter, String tag);

    /**
     * @param id
     * @throws ResourceNotFoundException
     */
    void deleteById(String id) throws ResourceNotFoundException;

    /**
     * @param spend
     * @throws ResourceNotFoundException
     */
    void delete(Spend spend) throws ResourceNotFoundException;
}
