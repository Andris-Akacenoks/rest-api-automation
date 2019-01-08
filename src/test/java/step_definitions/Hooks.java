package step_definitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import helpers.TestCaseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static helpers.TestCaseContext.BLOG_CLIENT;

public class Hooks {

    private final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void beforeAll() {
        TestCaseContext.init();
    }

    @After
    public void afterAll(Scenario scenario) {
        if (scenario.isFailed()) {
            LOGGER.info("Scenario failed! Keeping test account {} for further investigation",
                    TestCaseContext.get().getTestUser().getEmail());
        } else {
            LOGGER.info("Starting cleanup of test data");
            BLOG_CLIENT.deleteAccount(TestCaseContext.get().getTestUser());
            try{
                BLOG_CLIENT.deleteAccount(TestCaseContext.get().getNonAuthenticatedUser());
                BLOG_CLIENT.deleteAccount(TestCaseContext.get().getCopyUser());
            }catch (java.lang.NullPointerException e){
                LOGGER.info("No helper test users were created and deleted");
            }
        }
    }
}
