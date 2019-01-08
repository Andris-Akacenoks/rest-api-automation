package helpers;

import clients.InterestingBlogClient;
import domain.User;

public class TestCaseContext {
    private static final ThreadLocal<TestCaseContext> THREAD_LOCAL = new ThreadLocal<>();
    public static InterestingBlogClient BLOG_CLIENT;
    private User testUser;
    private User copyUser;
    private User nonAuthenticatedUser;

    private TestCaseContext() {
        BLOG_CLIENT = new InterestingBlogClient();
    }

    public void setCopyUser(User user){
        this.copyUser = new User(user);
    }

    public void setNonAuthenticatedUser(User user){
        this.nonAuthenticatedUser = user;
    }

    public User getNonAuthenticatedUser(){
        return this.nonAuthenticatedUser;
    }

    public User getCopyUser(){
        return this.copyUser;
    }

    public static TestCaseContext get() {
        return THREAD_LOCAL.get();
    }

    public static void init() {
        THREAD_LOCAL.set(new TestCaseContext());
    }

    public User getTestUser() {
        return testUser;
    }

    public void setTestUser(User testUser) {
        this.testUser = testUser;
    }
}
