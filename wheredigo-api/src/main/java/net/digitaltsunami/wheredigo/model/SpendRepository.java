package net.digitaltsunami.wheredigo.model;

/**
 * Created by dhagberg on 6/10/17.
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.ZonedDateTime;

/**
 * Defines repository operations on the Spend repo.  In addition to default Elasticsearch CRUD and query operations,
 * queries for attributes are defined.
 */
public interface SpendRepository extends ElasticsearchRepository<Spend, String> {

    /**
     * Return all spend entries matching a given category.
     * @param category Case sensitive category.  Must match entire keyword. May contain wildcard.
     * @return Iterable of 0 to n spend entries matching the category.
     */
    Iterable<Spend> findAllByCategory(String category);

    /**
     * Return a page of spend entries matching a given category.
     * @param pageable Paging information
     * @param category Case sensitive category.  Must match entire keyword. May contain wildcard
     * @return a page of 0 to page size spend entries matching the category.
     */
    Page<Spend> findAllByCategory(Pageable pageable, String category);

    /**
     * Return all spend entries matching a given subcategory.
     * @param subcategory Case sensitive subcategory.  Must match entire keyword. May contain wildcard
     * @return Iterable of 0 to n spend entries matching the subcategory.
     */
    Iterable<Spend> findAllBySubcategory(String subcategory);

    /**
     * Return all spend entries matching both the category and subcategory.
     * @param category Case sensitive category.  Must match entire keyword. May contain wildcard
     * @param subcategory Case sensitive subcategory.  Must match entire keyword. May contain wildcard
     * @return Iterable of 0 to n spend entries matching the category and subcategory
     */
    Iterable<Spend> findAllByCategoryAndSubcategory(String category, String subcategory);

    /**
     * Return a page of spend entries matching a given category.
     * @param pageable Paging information
     * @param category Case sensitive category.  Must match entire keyword. May contain wildcard
     * @param subcategory Case sensitive subcategory.  Must match entire keyword. May contain wildcard
     * @return a page of 0 to page size spend entries matching the category and subcategory.
     */
    Page<Spend> findAllByCategoryAndSubcategory(Pageable pageable, String category, String subcategory);

    /**
     * Return all spend entries matching all provided fields. Null values will not be used in the query.
     *
     * @param category Case sensitive category.  Must match entire keyword. May contain wildcard
     * @param subcategory Case sensitive subcategory.  Must match entire keyword. May contain wildcard
     * @param vendor Case sensitive vendor.  Must match entire keyword. May contain wildcard
     * @param note Case insensitive query for note. Query can match individual tokens. May contain wildcard.
     * @param tags Case insensitive query for tag. Query can match individual tokens. May not contain wildcard.
     * @return Iterable of 0 to n spend entries matching the provided fields.
     */
    Iterable<Spend> findAllByCategoryAndSubcategoryAndVendorAndNoteAndTags(String category, String subcategory,
                                                                          String vendor, String note, String tags);

    /**
     * Return a page of spend entries matching all provided fields. Null values will not be used in the query.
     *
     * @param pageable Paging information
     * @param category Case sensitive category.  Must match entire keyword. May contain wildcard
     * @param subcategory Case sensitive subcategory.  Must match entire keyword. May contain wildcard
     * @param vendor Case sensitive vendor.  Must match entire keyword. May contain wildcard
     * @param note Case insensitive query for note. Query can match individual tokens. May contain wildcard.
     * @param tags Case insensitive query for tag. Query can match individual tokens. May not contain wildcard.
     * @return a page of 0 to page size spend entries matching the provided fields.
     */
    Page<Spend> findAllByCategoryAndSubcategoryAndVendorAndNoteAndTags(Pageable pageable, String category, String subcategory,
                                                                String vendor, String note, String tags);
    /**
     * Return all spend entries matching between the provided dates.  Dates are inclusive.  Transactions occurring
     * on the either the start date or stop date will be included.
     * @param startDate Earliest date of transaction to return.
     * @param stopDate Latest date of transaction to return.
     * @return Iterable of 0 to n spend entries occurring within the provided date range.
     */
    Iterable<Spend> findAllByTransDateBetween(ZonedDateTime startDate, ZonedDateTime stopDate);

    /**
     * Return all spend entries matching a given tag.
     * @param tags Case sensitive tag.  Must match entire tag. May not contain wildcard.
     * @return Iterable of 0 to n spend entries matching the tag.
     */
    Iterable<Spend> findAllByTags(String tags);

    /**
     * Return a page of spend entries matching a given tag.
     * @param pageable Paging information
     * @param tags Case sensitive tag.  Must match entire tag. May not contain wildcard
     * @return a page of 0 to page size spend entries matching the tag.
     */
    Page<Spend> findAllByTags(Pageable pageable, String tags);
}
