#Feature: User registration feature
#    As a user I should be able to create a new account and perform action with it
#
#
#    Background:
#        Given a new account has been created
#        And I have logged in with the newly created user
#
#    Scenario: Altering profile information
#        When I edit users Firstname as "EditedName" and Lastname to "EditedLastName"
#        Then Firstname and Lastname are updated to "EditedName" and "EditedLastName" on the server
#        When I revert Firstname and Lastname
#        Then Firstname and Lastname are reverted