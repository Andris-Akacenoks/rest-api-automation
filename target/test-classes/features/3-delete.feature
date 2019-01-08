Feature: Other users Blog Post deleting feature
  As a user, I should be able to try deleting other user blog posts.

    Background:
        Given a new account has been created
        And I have logged in with the newly created user
        When I create a new log post with title "Here in my garage"
        Then the newly added post is not published
        When I publish the newly added post

      Scenario: Delete other users blog post
        When I create a non-authorised profile
        And I have logged in with the non-authorised profile
        When I fetch other users published blog post
        And I delete the blog post as non-author
        Then the post is not deleted