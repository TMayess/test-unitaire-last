Feature: Support ticket management

  Scenario: Creating a valid ticket
    Given no ticket exists in the system
    When I create a ticket with title "Network issue" and priority "HIGH"
    Then the ticket is created with status "OPEN"
    And the ticket title is "Network issue"

  Scenario: Resolving an open ticket
    Given an open ticket with title "Login bug" and priority "MEDIUM"
    When I update the ticket status to "RESOLVED"
    Then the ticket status is "RESOLVED"

  Scenario: Rejecting status change on an already resolved ticket
    Given an open ticket with title "Old bug" and priority "LOW"
    And the ticket status has been changed to "RESOLVED"
    When I attempt to change the ticket status to "IN_PROGRESS"
    Then a conflict error is returned

  Scenario: Looking up a non-existent ticket
    Given no ticket exists in the system
    When I look up the ticket with id "non-existent-ticket"
    Then a 404 error is returned