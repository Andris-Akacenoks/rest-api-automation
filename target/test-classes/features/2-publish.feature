Feature: Blog Post publishing feature
    As a user, I should be able to publish blog posts and perform actions
    with them.

    Background:
        Given a new account has been created
        And I have logged in with the newly created user

    Scenario: Create blog post
        When I create a new log post with title "Here in my garage"
        Then the newly added post is not published
        When I publish the newly added post
        Then the new blog post is published
        When I delete the newly published blog post
        Then the post is deleted and no longer available