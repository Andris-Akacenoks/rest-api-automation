package domain;

import io.cucumber.datatable.dependency.com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String firstName;
    private String lastName;
    private String password;
    private String id;
    private String email;
    private String authToken;
    private List<BlogPost> blogPosts;


    public User(String email, String firstName, String lastName, String password) {
        this.blogPosts = new ArrayList<>();
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(User another) {
        this.blogPosts = another.blogPosts;
        this.email = another.email;
        this.firstName = another.firstName;
        this.lastName = another.lastName;
        this.password = another.password;
        this.id = another.id;
        this.email = another.email;
        this.authToken = another.authToken;
    }

    public BlogPost fetchFirstPost(){
        return this.blogPosts.get(0);
    }

    public List<BlogPost> getAllBlogPosts(){
        return this.blogPosts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void addBlogPost(BlogPost blogPost){
        this.blogPosts.add(blogPost);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
