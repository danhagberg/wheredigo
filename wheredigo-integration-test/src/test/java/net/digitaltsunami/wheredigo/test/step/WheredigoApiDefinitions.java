package net.digitaltsunami.wheredigo.test.step;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import net.digitaltsunami.wheredigo.test.SpendEntry;
import net.digitaltsunami.wheredigo.test.SpringBootIntegrationTest;
import net.digitaltsunami.wheredigo.test.World;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WheredigoApiDefinitions extends SpringBootIntegrationTest {

    @After
    public void afterScenario() {
        for (URI uri : getWorld().getNewSpendEntryUris()) {
            given(getRequestSpec())
                .when()
                    .delete(uri)
                .then()
                    .statusCode(anyOf(is(204), is(404)));
        }
        getWorld().clearWorld();
    }

    @When("^the client makes a call to GET /api/v1/wheredigo$")
    public void the_client_makes_a_call_to_get_api_v1_wheredigo() throws Exception {
        getWorld().setResponse(
                given(getRequestSpec())
                .when()
                    .port(getPort())
                    .queryParams(getWorld().getQueryParameters())
                    .get(BASE_PATH));
    }


    @When("^the client makes a call to GET /api/v1/wheredigo with unknown ID$")
    public void the_client_makes_a_call_to_get_api_v1_wheredigo_with_unknown_id() throws Throwable {
        getWorld().setResponse(
                given(getRequestSpec())
                .when()
                    .port(getPort())
                    .get("api/v1/wheredigo/{id}", "bad ID"));
    }

    @When("^the client makes a call to DELETE /api/v1/wheredigo with unknown ID$")
    public void the_client_makes_a_call_to_delete_api_v1_wheredigo_with_unknown_id() throws Throwable {
        getWorld().setResponse(
                given(getRequestSpec())
                .when()
                    .port(getPort())
                    .delete("api/v1/wheredigo/{id}", "bad ID"));
    }

    @When("^the client makes a call to DELETE /api/v1/wheredigo with the ID for that spend$")
    public void the_client_makes_a_call_to_delete_api_v1_wheredigo_with_the_id_for_that_spend() throws Throwable {
        String newId = getWorld().getCurrentId();
        getWorld().setResponse(
                given(getRequestSpec())
                .when()
                    .port(getPort())
                    .delete("api/v1/wheredigo/{id}", newId));
    }

    @When("^the client makes a call to GET /api/v1/wheredigo with the new ID$")
    public void the_client_makes_a_call_to_get_api_v1_wheredigo_with_the_new_id() throws Throwable {
        String newId = getWorld().getCurrentId();
        getWorld().setResponse(
                given(getRequestSpec())
                .when()
                    .port(getPort())
                    .get("api/v1/wheredigo/{id}", newId));
    }

    @Then("the client receives status code of {int}")
    public void the_client_receives_status_code_of(int statusCode) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        getWorld().getResponse()
                .then()
                .statusCode(statusCode);
    }

    @Then("the number of results is {int}")
    public void the_number_of_results_is(int count) throws Throwable {
        ResponseBody body = getWorld().getResponse().getBody();
        getWorld().getResponse()
                .then()
                .body("totalElements", is(count));
    }

    @Given("the following spend transactions recorded on <date> for <amount> at <vendor> and is categorized as <category>\\/<subcategory>:")
    public void the_following_spend_transactions_recorded_on_date_for_amount_at_vendor_and_is_categorized_as_category_subcategory(List<SpendEntry> entries) throws JSONException {
        for (SpendEntry entry : entries) {
            JSONObject body = spendEntryToJson(entry);
            postSpend(body);
        }
    }
    @Given("the following spend transactions recorded on <date> for <amount> at <vendor> and is categorized as <category>\\/<subcategory> with a <note>:")
    public void the_following_spend_transactions_recorded_on_date_for_amount_at_vendor_and_is_categorized_as_category_subcategory_with_a_note(List<SpendEntry> entries) throws JSONException {
        for (SpendEntry entry : entries) {
            System.out.println(entry);
            JSONObject body = spendEntryToJson(entry);
            postSpend(body);
        }
    }



    @When("the client posts a spend transaction on {string} for {double} at {string} and is categorized as {string}\\/{string}")
    public void the_client_posts_a_transaction_for_amount_at_cat_by(String date, double amount, String vendor, String category, String subcategory) throws Throwable {

        JSONObject body = spendEntryToJson(
                SpendEntry.builder()
                        .date(ZonedDateTime.parse(date))
                        .amount(new BigDecimal(amount))
                        .vendor(vendor)
                        .category(category)
                        .subcategory(subcategory)
                        .build());

        postSpend(body);
    }

    @Then("the amount is {double}")
    public void the_amount_is(Double expectedAmount) {
        Double recordedAmount = getWorld().getResponse().then().extract().jsonPath().getDouble("amount");
        assertThat(recordedAmount, closeTo(expectedAmount, 0.01));
    }

    @Then("the vendor is {string}")
    public void the_vendor_is(String vendor) {
        getWorld().getResponse()
                .then()
                .assertThat().body("vendor", is(vendor));
    }

    @Then("is categorized as {string}")
    public void is_categorized_as(String category) {
        getWorld().getResponse()
                .then()
                .assertThat().body("category", is(category));
    }

    @Then("is subcategorized as {string}")
    public void is_subcategorized_as(String subcategory) {
        getWorld().getResponse()
                .then()
                .assertThat().body("subcategory", is(subcategory));
    }

    @Then("is has a transaction date of {string}")
    public void is_has_a_transaction_date_of(String dateString) {
        getWorld().getResponse()
                .then()
                .assertThat().body("transDate", is(dateString));
    }

    @When("the client makes a call to PUT \\/api\\/v1\\/wheredigo with the ID and changes {string} {double} {string} {string} {string}")
    public void the_client_makes_a_call_to_put_api_v1_wheredigo_with_the_id_and_changes_date_amount_vendor_category_subcategory(
            String date, double amount, String vendor, String category, String subcategory) throws Throwable {
        String newId = getWorld().getCurrentId();
        SpendEntry update = SpendEntry.builder()
                .id(newId)
                .date(ZonedDateTime.parse(date))
                .amount(new BigDecimal(amount))
                .vendor(vendor)
                .category(category)
                .subcategory(subcategory)
                .build();

        JSONObject body = spendEntryToJson(update);

        getWorld().setResponse(
                given(getRequestSpec())
                        .when()
                        .port(getPort())
                        .contentType(ContentType.JSON)
                        .body(body.toString())
                        .put(BASE_PATH + "/{id}", newId));
    }

    @When("the client request specifies a category of {string}")
    public void the_client_request_specifies_a_category_of(String category) {
        getWorld().getQueryParameters()
                .put("category", category);
    }

    @When("the client request specifies a subcategory of {string}")
    public void the_client_request_specifies_a_subcategory_of(String subcategory) {
        getWorld().getQueryParameters()
                .put("subcategory", subcategory);
    }

    @When("the client request specifies a vendor of {string}")
    public void the_client_request_specifies_a_vendor_of(String vendor) {
        getWorld().getQueryParameters()
                .put("vendor", vendor);
    }

    @When("the client request specifies a note of {string}")
    public void the_client_request_specifies_a_note_of(String note) {
        getWorld().getQueryParameters()
                .put("note", note);
    }

    private JSONObject spendEntryToJson(SpendEntry entry) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("transDate", entry.getDate());
        body.put("amount", entry.getAmount());
        String value = entry.getId();
        if(isNotEmpty(value)) {
            body.put("id", value);
        }
        value = entry.getCategory();
        if(isNotEmpty(value)) {
            body.put("category", value);
        }
        value = entry.getSubcategory();
        if(isNotEmpty(value)) {
            body.put("subcategory", value);
        }
        value = entry.getVendor();
        if(isNotEmpty(value)) {
            body.put("vendor", value);
        }
        value = entry.getNote();
        if(isNotEmpty(value)) {
            body.put("note", value);
        }
        return body;
    }

    private void postSpend(JSONObject body) {
        Response response =
                given(getRequestSpec())
                        .when()
                        .port(getPort())
                        .contentType(ContentType.JSON)
                        .body(body.toString())
                        .post(BASE_PATH);
        World world = getWorld();
        world.setResponse(response);
        world.getNewSpendEntryUris()
                .add(URI.create(getWorld().getResponse().getHeader("location")));
        String newId = response.getBody().jsonPath().getString("id");
        if (newId != null) {
            world.setCurrentId(newId);
        }
    }

}
