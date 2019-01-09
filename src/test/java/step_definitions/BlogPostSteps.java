package step_definitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import domain.BlogPost;
import domain.User;
import helpers.TestCaseContext;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static helpers.TestCaseContext.BLOG_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;

public class BlogPostSteps {

    private final Logger LOGGER = LoggerFactory.getLogger(BlogPostSteps.class);

    @Then("^I create a new log post with title \"([^\"]*)\"$")
    public void createNewBlogPost(String postTitle) {
        BlogPost post = new BlogPost(postTitle, "Lorem ipsum" + ZonedDateTime.now().toString());
        Response response = BLOG_CLIENT.createBlogPost(post, TestCaseContext.get().getTestUser());

        LOGGER.info(response.getBody().asString());
        assertThat(response.getBody().jsonPath().getString("title"))
                .as("Check if title received from the server is correct.")
                .isEqualTo(post.getTitle());

        assertThat(response.getBody().jsonPath().getString("content"))
                .as("Check if content received from the server is correct.")
                .isEqualTo(post.getContent());

        assertThat(response.getBody().jsonPath().getBoolean("published"))
                .as("Check if post is not published by default.")
                .isEqualTo(false);

        assertThat(response.getBody().jsonPath().getString("updatedAt"))
                .as("Check if updated at timestamp is assigned.")
                .isNotEqualTo(null);

        assertThat(response.getBody().jsonPath().getInt("id"))
                .as("Check if post id is assigned")
                .isNotEqualTo(null);

        assertThat(response.getBody().jsonPath().getString("author.id"))
                .as("Check if author user is correct")
                .isEqualTo(TestCaseContext.get().getTestUser().getId());

        post = response.getBody().as(BlogPost.class);

        TestCaseContext.get().getTestUser().addBlogPost(post);
    }

    @And("^the newly added post is not published$")
    public void checkAddedPost() {
        Response response = BLOG_CLIENT.getAllPublishedPosts(TestCaseContext.get().getTestUser());
        BlogPost[] posts = response.getBody().as(BlogPost[].class);

        Arrays.stream(posts).forEach((blogPost -> {
            if (blogPost.getId().equals(TestCaseContext.get().getTestUser().fetchFirstPost().getId())){
                throw new RuntimeException("Blog Post " + TestCaseContext.get().getTestUser().fetchFirstPost().getId()
                        + " was published after creation.");
            }
        }));
    }

    @When("I publish the newly added post")
    public void iPublishTheNewlyAddedPost() {
        BlogPost post = TestCaseContext.get().getTestUser().fetchFirstPost();
        Response response = BLOG_CLIENT.publishBlogPost(post, TestCaseContext.get().getTestUser());

        assertThat(response.getBody().jsonPath().getString("published"))
                .as("Check blog post is published.")
                .isEqualTo("true");

    }

    @Then("the new blog post is published")
    public void itIsAvailableToThePublishedPostList() {
        int id = Integer.valueOf(TestCaseContext.get().getTestUser().fetchFirstPost().getId());
        Response response = BLOG_CLIENT.getPostById(id, TestCaseContext.get().getTestUser());

        assertThat(response.getBody().jsonPath().getString("published"))
                .as("Check blog post is published.")
                .isEqualTo("true");

        assertThat(response.getBody().jsonPath().getString("createdAt"))
                .as("Check if created at timestamp is assigned.")
                .isNotEqualTo(null);
    }


    @When("I delete the newly published blog post")
    public void iDeleteTheNewlyPublishedBlogPost() {
        int id = Integer.valueOf(TestCaseContext.get().getTestUser().fetchFirstPost().getId());
        Response response = BLOG_CLIENT.deletePostById(id, TestCaseContext.get().getTestUser());

        assertThat(response.getStatusCode())
                .as("Check if blog post is deleted.")
                .isEqualTo(204);
    }

    @Then("the post is deleted and no longer available")
    public void thePostIsDeletedAndNoLongerAvailable() {
        List<BlogPost> posts = TestCaseContext.get().getTestUser().getAllBlogPosts();
        if (posts.isEmpty()){
            LOGGER.info("No blog posts are available.");
        } else{
            for(BlogPost post : posts){
                try{
                    Response response = BLOG_CLIENT.getPostById(Integer.valueOf(post.getId()), TestCaseContext.get().getTestUser());
                    throw new RuntimeException("Blog post is still available");
                }
                catch(java.lang.AssertionError e){
                    LOGGER.info("Blog post is no longer available.");
                }
            }
        }

    }

    @When("I fetch other users published blog post")
    public void iFetchOtherUsersPublishedBlogPost() {
        User localUser = TestCaseContext.get().getTestUser();
        User nonAuthorisedUser = TestCaseContext.get().getNonAuthenticatedUser();
        Response response = BLOG_CLIENT.getAllPostsByDifferentAuthorId(localUser, nonAuthorisedUser);
        BlogPost[] posts = response.getBody().as(BlogPost[].class);
        nonAuthorisedUser.addBlogPost(posts[0]);
    }

    @And("I delete the blog post as non-author")
    public void iDeleteTheBlogPostAsNonAuthor() {
        User nonAuthorisedUser = TestCaseContext.get().getNonAuthenticatedUser();
        BlogPost post = nonAuthorisedUser.fetchFirstPost();
        Response response = BLOG_CLIENT.deletePostAsUnauthorisedUser(Integer.valueOf(post.getId()), nonAuthorisedUser);

        assertThat(response.getStatusCode())
            .as("Check if blog post forbidden.")
            .isEqualTo(403);

        assertThat(response.getBody().jsonPath().getString("message"))
            .as("Check if specific error message is given")
            .isEqualTo("You cannot delete a post that has not been created by you!");
    }

    @Then("the post is not deleted")
    public void thePostIsNotDeleted() {
        int id = Integer.valueOf(TestCaseContext.get().getTestUser().fetchFirstPost().getId());
        Response response = BLOG_CLIENT.getPostById(id, TestCaseContext.get().getTestUser());

        assertThat(response.getBody().jsonPath().getString("published"))
                .as("Check blog post is published.")
                .isEqualTo("true");

        assertThat(response.getBody().jsonPath().getString("createdAt"))
                .as("Check if created at timestamp is assigned.")
                .isNotEqualTo(null);

    }

    @When("I try to edit title of blog post as non-authorised user")
    public void iTryToEditTitleOfBlogPostAsNonAuthorisedUser() {
        User nonAuthorisedUser = TestCaseContext.get().getNonAuthenticatedUser();
        BlogPost post = nonAuthorisedUser.fetchFirstPost();

        post.setTitle("Edited title");
        Response response = BLOG_CLIENT.editPostNotAuthorised(Integer.valueOf(post.getId()), post, nonAuthorisedUser);

        assertThat(response.getStatusCode())
                .as("Check if blog post forbidden.")
                .isEqualTo(403);

        assertThat(response.getBody().jsonPath().getString("message"))
                .as("Check if specific error message is given")
                .isEqualTo("You cannot edit a post that has not been created by you!");

    }

    @And("I try to edit content of blog post as non-authorised user")
    public void iTryToEditContentOfBlogPostAsNonAuthorisedUser() {
        User nonAuthorisedUser = TestCaseContext.get().getNonAuthenticatedUser();
        BlogPost post = nonAuthorisedUser.fetchFirstPost();

        post.setContent("Edited content");
        Response response = BLOG_CLIENT.editPostNotAuthorised(Integer.valueOf(post.getId()), post, nonAuthorisedUser);

        assertThat(response.getStatusCode())
                .as("Check if blog post forbidden.")
                .isEqualTo(403);

        assertThat(response.getBody().jsonPath().getString("message"))
                .as("Check if specific error message is given")
                .isEqualTo("You cannot edit a post that has not been created by you!");
    }

    @Then("Content is not changed")
    public void contentIsNotChanged() {
        int id = Integer.valueOf(TestCaseContext.get().getTestUser().fetchFirstPost().getId());
        Response response = BLOG_CLIENT.getPostById(id, TestCaseContext.get().getTestUser());

        assertThat(response.getBody().jsonPath().getString("content"))
                .as("Check blog post is published.")
                .isEqualTo(TestCaseContext.get().getTestUser().fetchFirstPost().getContent());

        assertThat(response.getBody().jsonPath().getString("title"))
                .as("Check if created at timestamp is assigned.")
                .isEqualTo(TestCaseContext.get().getTestUser().fetchFirstPost().getTitle());

    }
}
