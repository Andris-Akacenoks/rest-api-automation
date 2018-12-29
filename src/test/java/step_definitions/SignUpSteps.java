package step_definitions;

import cucumber.api.java.en.Given;
import domain.User;
import helpers.TestCaseContext;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static helpers.TestCaseContext.BLOG_CLIENT;
import static helpers.UserHelper.generateUser;
import static org.assertj.core.api.Assertions.assertThat;


public class SignUpSteps {

    private final Logger LOGGER = LoggerFactory.getLogger(SignUpSteps.class);

    @Given("^I have created a new user$")
    public void newUserIsCreated() {
        User testUser = generateUser();
        System.out.println(testUser.toString());

        Response response = BLOG_CLIENT.createAccount(testUser);

        System.out.println(response.toString());

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

        TestCaseContext.get().setTestUser(testUser);


    }
}
