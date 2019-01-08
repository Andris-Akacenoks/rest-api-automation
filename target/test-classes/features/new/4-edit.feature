Feature: Blog Post publishing feature
    As a user, I should be able to publish blog posts and perform actions
    with them.

    Background:
        Given a new account has been created
        And I have logged in with the newly created user
        When I create a new log post with title "Here in my garage"
        Then the newly added post is not published
        When I publish the newly added post
        When I create a non-authorised profile
        And I have logged in with the non-authorised profile
        When I fetch other users published blog post

      Scenario: Edit other users blog post
        When I try to edit title of blog post as non-authorised user
        And I try to edit content of blog post as non-authorised user
        Then Content is not changed