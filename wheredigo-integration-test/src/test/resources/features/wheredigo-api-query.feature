Feature: Queries
  # Asserts the following:
  # - Queries with zero results return success and empty result sets
  # - Queries on category, subcategory, and vendor:
  #     - Keyword based.  Meaning that query must match entire value
  #     - Case sensitive.
  # - Queries on notes:
  #     - Token based. Meaning that query can match any word within value
  #     - Case insensitive.
  # - Queries allow wildcard values.

  Background: Add data for query scenarios
    Given the following spend transactions recorded on <date> for <amount> at <vendor> and is categorized as <category>/<subcategory> with a <note>:
      | date                 | amount | category   | subcategory | vendor   | note        |
      | 2018-07-01T12:10:00Z | 10.00  | category 1 | subcat 1    | vendor 1 | test note 1 |
      | 2018-07-01T14:10:00Z | 11.00  | category 2 | subcat 1    | vendor 1 | test note 2 |
      | 2018-07-01T15:10:00Z | 12.00  | category 2 | subcat 2    | vendor 2 | test note 3 |
      | 2018-07-01T16:10:00Z | 13.00  | category 3 | subcat 1    | vendor 1 | test note 4 |
      | 2018-07-01T16:10:00Z | 14.00  | category 3 | subcat 2    | vendor 2 | test note 5 |
      | 2018-07-01T16:10:00Z | 15.00  | category 3 | subcat 3    | vendor 3 | test note 6 |
      | 2018-07-01T16:10:00Z | 16.00  | Category 4 | Subcat 4    | Vendor 4 | test note 7 |
      | 2018-07-02T17:30:00Z | 16.45  |            |             |          | TEST NOTE 8 |

  Scenario: Client queries spend transactions by category
    When the client request specifies a category of "category 1"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 1

  Scenario: Client queries spend transactions by unknown category
    When the client request specifies a category of "unknown category"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 0

  Scenario: Client queries spend transactions by wildcard category
    When the client request specifies a category of "category*"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 6

  Scenario: Client queries spend transactions by partial category
    When the client request specifies a category of "category"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 0

  Scenario: Client queries spend transactions by category with case sensitivity
    When the client request specifies a category of "Category*"
     And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
     And the number of results is 1

  Scenario: Client queries spend transactions by category with multiple results
    When the client request specifies a category of "category 3"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 3

  Scenario: Client queries spend transactions by category and subcategory
    When the client request specifies a category of "category 1"
    When the client request specifies a subcategory of "subcat 1"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 1

  Scenario: Client queries spend transactions by category and unknown subcategory
    When the client request specifies a category of "category 1"
    When the client request specifies a subcategory of "unknown subcat"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 0

  Scenario: Client queries spend transactions by specific vendor
    When the client request specifies a vendor of "vendor 1"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 3

  Scenario: Client queries spend transactions by partial vendor
    When the client request specifies a vendor of "vendor"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 0

  Scenario: Client queries spend transactions by wildcard vendor
    When the client request specifies a vendor of "vendor*"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 6

  Scenario: Client queries spend transactions by vendor with case sensitivity
    When the client request specifies a vendor of "Vendor*"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 1

  Scenario: Client queries spend transactions with complete note
    When the client request specifies a note of "test note 1"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 1

  Scenario: Client queries spend transactions with partial note
    When the client request specifies a note of "note"
    And the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And the number of results is 8
