package step_definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import domain.User;
import helpers.TestCaseContext;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static helpers.TestCaseContext.BLOG_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfileSteps {

    private final Logger LOGGER = LoggerFactory.getLogger(ProfileSteps.class);

    @And("^I have logged in with the newly created user")
    public void logIn() {
        Response response = BLOG_CLIENT.logIn(TestCaseContext.get().getTestUser());

        assertThat(response.getBody().jsonPath().getString("email"))
                .as("Check if email received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getTestUser().getEmail());

        assertThat(response.getBody().jsonPath().getString("firstName"))
                .as("Check if firstName received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getTestUser().getFirstName());

        assertThat(response.getBody().jsonPath().getString("lastName"))
                .as("Check if lastName received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getTestUser().getLastName());

        assertThat(response.getBody().jsonPath().getString("id"))
                .as("Check if id received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getTestUser().getId());

        TestCaseContext.get().getTestUser().setAuthToken(response.getHeader("Authorization"));

    }

    @When("^I fetch my profile information and validate it$")
    public void iFetchProfileInformation() {
        User user = TestCaseContext.get().getTestUser();
        Response response = BLOG_CLIENT.getProfileInfo(user);

        assertThat(response.getBody().jsonPath().getString("email"))
                .as("Check if email received from the server is correct.")
                .isEqualTo(user.getEmail());

        assertThat(response.getBody().jsonPath().getString("firstName"))
                .as("Check if firstName received from the server is correct.")
                .isEqualTo(user.getFirstName());

        assertThat(response.getBody().jsonPath().getString("lastName"))
                .as("Check if lastName received from the server is correct.")
                .isEqualTo(user.getLastName());

        assertThat(response.getBody().jsonPath().getString("id"))
                .as("Check if id received from the server is correct.")
                .isNotEqualTo(null);
    }

    @When("I edit users Firstname as {string} and Lastname to {string}")
    public void iEditUsersFirstnameAsAndLastnameTo(String firstName, String lastName) {
        User originalUser = TestCaseContext.get().getTestUser();
        TestCaseContext.get().setCopyUser(originalUser);
        User copyUser = TestCaseContext.get().getCopyUser();
        Response response = BLOG_CLIENT.editProfileInfo(copyUser, new User (firstName, lastName));
        originalUser.setFirstName(firstName);
        originalUser.setFirstName(lastName);

    }

    @Then("Firstname and Lastname are updated to {string} and {string} on the server")
    public void firstnameAndLastnameAreUpdatedToAndOnTheServer(String firstName, String lastName) {
        User originalUser = TestCaseContext.get().getTestUser();
        Response response = BLOG_CLIENT.getProfileInfo(originalUser);

        assertThat(response.getBody().jsonPath().getString("firstName"))
                .as("Check if firstName received from the server is correct.")
                .isEqualTo(firstName);

        assertThat(response.getBody().jsonPath().getString("lastName"))
                .as("Check if lastName received from the server is correct.")
                .isEqualTo(lastName);


    }

    @When("I revert Firstname and Lastname")
    public void iRevertFirstnameAndLastname() {
        User originalUser = TestCaseContext.get().getTestUser();
        User copyUser = TestCaseContext.get().getCopyUser();

        String initialName = copyUser.getFirstName();
        String initialLastName = copyUser.getLastName();

        Response response = BLOG_CLIENT.editProfileInfo(originalUser, new User (initialName, initialLastName));

    }

    @Then("Firstname and Lastname are reverted")
    public void firstnameAndLastnameAreReverted() {
        User originalUser = TestCaseContext.get().getTestUser();
        User copyUser = TestCaseContext.get().getCopyUser();

        Response response = BLOG_CLIENT.getProfileInfo(originalUser);

        assertThat(response.getBody().jsonPath().getString("firstName"))
                .as("Check if firstName received from the server is correct.")
                .isEqualTo(copyUser.getFirstName());

        assertThat(response.getBody().jsonPath().getString("lastName"))
                .as("Check if lastName received from the server is correct.")
                .isEqualTo(copyUser.getLastName());
    }

    @And("I have logged in with the non-authorised profile")
    public void iHaveLoggedInWithTheNonAuthorisedProfile() {
        Response response = BLOG_CLIENT.logIn(TestCaseContext.get().getNonAuthenticatedUser());

        assertThat(response.getBody().jsonPath().getString("email"))
                .as("Check if email received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getNonAuthenticatedUser().getEmail());

        assertThat(response.getBody().jsonPath().getString("firstName"))
                .as("Check if firstName received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getNonAuthenticatedUser().getFirstName());

        assertThat(response.getBody().jsonPath().getString("lastName"))
                .as("Check if lastName received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getNonAuthenticatedUser().getLastName());

        assertThat(response.getBody().jsonPath().getString("id"))
                .as("Check if id received from the server is correct.")
                .isEqualTo(TestCaseContext.get().getNonAuthenticatedUser().getId());

        TestCaseContext.get().getNonAuthenticatedUser().setAuthToken(response.getHeader("Authorization"));
    }
}