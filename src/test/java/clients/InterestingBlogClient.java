package clients;

import config.Config;
import domain.BlogPost;
import domain.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response.Status;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;



public class InterestingBlogClient {

    private final Logger LOGGER = LoggerFactory.getLogger(InterestingBlogClient.class);
    private final Config CONFIG = Config.getInstance();

    public InterestingBlogClient() {
    }

    public Response createAccount(User user){
        String endpoint = "http://195.13.194.180:8090/api/sign-up";
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

    private void logCallResposeTime(String endpoint, Response response) {
        LOGGER.info("Response time for {}: {} ms",endpoint, response.getTime());
    }


}
