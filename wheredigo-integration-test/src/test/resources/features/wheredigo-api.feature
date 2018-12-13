Feature: Initial query
  Scenario: Client queries empty dataset
    When the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And  the number of results is 0

  Scenario: Client adds a valid entry
    When the client posts a spend transaction on "2018-01-02T03:04:00Z" for 10.00 at "Jimmy's" and is categorized as "Food"/"Snack"
    Then the client receives status code of 201
    When the client makes a call to GET /api/v1/wheredigo with the new ID
    Then the client receives status code of 200
    And  the amount is 10.00
    And  the vendor is "Jimmy's"
    And  is categorized as "Food"
    And  is subcategorized as "Snack"
    And  is has a transaction date of "2018-01-02T03:04:00Z"

  Scenario Outline: Client updates an existing entry passing the entire body for update
    Given the client posts a spend transaction on <date> for <amount> at <vendor> and is categorized as <category>/<subcategory>
    When the client makes a call to PUT /api/v1/wheredigo with the ID and changes <udate> <uamount> <uvendor> <ucategory> <usubcategory>
    Then the client receives status code of 200
    And  the amount is <uamount>
    And  the vendor is <uvendor>
    And  is categorized as <ucategory>
    And  is subcategorized as <usubcategory>
    And  is has a transaction date of <udate>
    Examples:
      | date                 | amount | category | subcategory | vendor   | udate               | uamount | ucategory | usubcategory | uvendor  |
      | "2018-07-01T12:10:00Z" | 10.00  | "Cat 1"    | "Subcat 1"    | "Vendor 1" | "2018-08-01T12:10:00Z" | 22.22    | "Cat U1"     | "Subcat U1"     | "Vendor U1" |

  Scenario: Client deletes entry
    Given the client posts a spend transaction on "2018-07-01T09:00:00Z" for 12.34 at "Pizza Place" and is categorized as "Food"/"Lunch"
    When the client makes a call to DELETE /api/v1/wheredigo with the ID for that spend
    Then the client receives status code of 204
    When the client makes a call to GET /api/v1/wheredigo with the new ID
    Then the client receives status code of 404

  Scenario: Client deletes entry that does not exist
    When the client makes a call to DELETE /api/v1/wheredigo with unknown ID
    Then the client receives status code of 404

  Scenario: Client requests entry that does not exist
    When the client makes a call to GET /api/v1/wheredigo with unknown ID
    Then the client receives status code of 404

  Scenario: Client queries spend transactions
    Given the following spend transactions recorded on <date> for <amount> at <vendor> and is categorized as <category>/<subcategory>:
      | date                 | amount | category        | subcategory        | vendor        |
      | 2018-07-01T12:10:00Z | 10.00  | Test Category 1 | Test Subcategory 1 | Test Vendor 1 |
      | 2018-07-02T13:30:00Z | 23.45  |                 |                    | Test Vendor 2 |
    When the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    And  the number of results is 2

  Scenario Outline: Client adds a valid entry
    When the client posts a spend transaction on <date> for <amount> at <vendor> and is categorized as <category>/<subcategory>
    Then the client receives status code of 201
    When the client makes a call to GET /api/v1/wheredigo
    Then the client receives status code of 200
    Examples:
      | date                   | amount | category          | subcategory          | vendor          |
      | "2018-07-01T12:10:00Z" | 10.00  | "Test Category 1" | "Test Subcategory 1" | "Test Vendor 1" |
      | "2018-07-02T13:30:00Z" | 20.00  | ""                | ""                   | "Test Vendor 2" |
