package helpers;

import clients.InterestingBlogClient;
import domain.User;

public class TestCaseContext {
    private static final ThreadLocal<TestCaseContext> THREAD_LOCAL = new ThreadLocal<>();
    public static InterestingBlogClient BLOG_CLIENT;
    private User testUser;

    private TestCaseContext() {
        BLOG_CLIENT = new InterestingBlogClient();
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
