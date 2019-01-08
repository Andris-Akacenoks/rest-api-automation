package clients;

import config.Config;
import domain.BlogPost;
import domain.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.soap.SOAPBinding;
import javax.ws.rs.core.Response.Status;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import sun.nio.cs.US_ASCII;

import static io.restassured.RestAssured.given;

public class InterestingBlogClient {

    private final Logger LOGGER = LoggerFactory.getLogger(InterestingBlogClient.class);
    private final Config CONFIG = Config.getInstance();
    private final String BASE_URL = CONFIG.getApiBaseUrl();

    public InterestingBlogClient() {
    }

    public Response createAccount(User user){
        String endpoint = BASE_URL + "sign-up";
        Response response =
            given().
                log().ifValidationFails().
                contentType(ContentType.JSON).
                body(user).
            when().
                post(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response logIn(User user){
        JSONObject loginPayload = new JSONObject();
        loginPayload.put("email", user.getEmail());
        loginPayload.put("password", user.getPassword());

        String endpoint = BASE_URL + "login";

        Response response =
            given().
                log().ifValidationFails().
                contentType(ContentType.JSON).
                body(loginPayload.toString()).
            when().
                post(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
             extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response getProfileInfo(User user){
        String endpoint = BASE_URL + "profile";
        Response response =
            given().
                header("Authorization", user.getAuthToken()).
            when().
                get(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response editProfileInfo(User user, User editedUser){
        String endpoint = BASE_URL + "profile";
        Response response =
                given().
                    header("Authorization", user.getAuthToken()).
                    contentType(ContentType.JSON).
                    body(editedUser).
                when().
                    put(endpoint).
                then().
                    log().ifValidationFails().
                    statusCode(Status.OK.getStatusCode()).
                extract().
                    response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response deleteAccount(User user){
        String endpoint = BASE_URL + "profile";
        Response response =
            given().
                log().ifValidationFails().
                header("Authorization", user.getAuthToken()).
            when().
                delete(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response createBlogPost(BlogPost post, User user){
        String endpoint = BASE_URL + "post";

        Response response =
            given().
                log().ifValidationFails().
                header("Authorization", user.getAuthToken()).
                contentType(ContentType.JSON).
                body(post).
            when().
                post(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;

    }

    public Response getAllPublishedPosts(User user){
        String endpoint = BASE_URL + "posts";
        Response response =
            given().
                header("Authorization", user.getAuthToken()).
            when().
                get(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response publishBlogPost(BlogPost post, User user){
        String endpoint = BASE_URL + "post/publish?post_id=" + post.getId();
        Response response =
            given().
                log().ifValidationFails().
                header("Authorization", user.getAuthToken()).
            when().
                post(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response getPostById(int id, User user){
        String endpoint = BASE_URL + "post?id=" + id;
        Response response =
            given().
                log().ifValidationFails().
                header("Authorization", user.getAuthToken()).
            when().
                get(endpoint).
            then().
                log().ifValidationFails().
                statusCode(Status.OK.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response getAllPostsByDifferentAuthorId(User user, User nonAuthUser){
        LOGGER.info("Poster: "+ user.toString());
        LOGGER.info("Reader: "+ nonAuthUser.toString());

        String endpoint = BASE_URL + "posts?author_id=" + user.getId();
        Response response =
                given().
                    log().ifValidationFails().
                    header("Authorization", nonAuthUser.getAuthToken()).
                when().
                    get(endpoint).
                then().
                    log().all().
                    statusCode(Status.OK.getStatusCode()).
                extract().
                    response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response deletePostById(int id, User user){
        String endpoint = BASE_URL + "post?post_id=" + id;
        Response response =
            given().
                log().ifValidationFails().
                header("Authorization", user.getAuthToken()).
            when().
                delete(endpoint).
            then().
                log().all().
                statusCode(Status.NO_CONTENT.getStatusCode()).
            extract().
                response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response deletePostAsUnauthorisedUser(int id, User user){
        String endpoint = BASE_URL + "post?post_id=" + id;
        Response response =
                given().
                    log().ifValidationFails().
                    header("Authorization", user.getAuthToken()).
                when().
                    delete(endpoint).
                then().
                    log().ifValidationFails().
                    statusCode(Status.FORBIDDEN.getStatusCode()).
                extract().
                    response();

        logCallResposeTime(endpoint, response);
        return response;
    }

    public Response editPostNotAuthorised(int id, BlogPost postInfo,  User nonAuthUser){
        String endpoint = BASE_URL + "post?post_id=" + id;
        Response response =
                given().
                    header("Authorization", nonAuthUser.getAuthToken()).
                    contentType(ContentType.JSON).
                    body(postInfo).
                when().
                    put(endpoint).
                then().
                    log().ifValidationFails().
                    statusCode(Status.FORBIDDEN.getStatusCode()).
                extract().
                    response();

        logCallResposeTime(endpoint, response);
        return response;
    }


    private void logCallResposeTime(String endpoint, Response response) {
//        LOGGER.info("Response time for {}: {} ms",endpoint, response.getTime());
    }
}
