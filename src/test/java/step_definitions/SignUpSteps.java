package step_definitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import domain.User;
import helpers.TestCaseContext;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static helpers.TestCaseContext.BLOG_CLIENT;
import static helpers.UserHelper.generateUser;
import static org.assertj.core.api.Assertions.assertThat;

public class SignUpSteps {

    private final Logger LOGGER = LoggerFactory.getLogger(ProfileSteps.class);

    @Given("^a new account has been created$")
    public void newAccountIsCreated() throws Throwable {
        User testUser = generateUser();
        Response response = BLOG_CLIENT.createAccount(testUser);

        assertThat(response.getBody().jsonPath().getString("email"))
            .as("Check if email received from the server is correct.")
            .isEqualTo(testUser.getEmail());

        assertThat(response.getBody().jsonPath().getString("firstName"))
            .as("Check if firstName received from the server is correct.")
            .isEqualTo(testUser.getFirstName());

        assertThat(response.getBody().jsonPath().getString("lastName"))
            .as("Check if lastName received from the server is correct.")
            .isEqualTo(testUser.getLastName());

        assertThat(response.getBody().jsonPath().getString("id"))
            .as("Check if id received from the server is correct.")
            .isNotEqualTo(null);

        testUser.setId(response.getBody().jsonPath().getString("id"));

        TestCaseContext.get().setTestUser(testUser);
    }

    @When("I create a non-authorised profile")
    public void iCreateANonAuthorisedProfile() {

        User testUser = generateUser();
        Response response = BLOG_CLIENT.createAccount(testUser);

        assertThat(response.getBody().jsonPath().getString("email"))
                .as("Check if email received from the server is correct.")
                .isEqualTo(testUser.getEmail());

        assertThat(response.getBody().jsonPath().getString("firstName"))
                .as("Check if firstName received from the server is correct.")
                .isEqualTo(testUser.getFirstName());

        assertThat(response.getBody().jsonPath().getString("lastName"))
                .as("Check if lastName received from the server is correct.")
                .isEqualTo(testUser.getLastName());

        assertThat(response.getBody().jsonPath().getString("id"))
                .as("Check if id received from the server is correct.")
                .isNotEqualTo(null);

        testUser.setId(response.getBody().jsonPath().getString("id"));

        TestCaseContext.get().setNonAuthenticatedUser(testUser);
    }
}