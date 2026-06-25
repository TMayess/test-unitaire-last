Feature: Room reservation management

  Scenario: Accepting a reservation when the room exists and the slot is free
    Given a room named "Salle A" with capacity 10 exists
    When I reserve this room from "2026-07-01T09:00:00" to "2026-07-01T10:00:00" for "Alice"
    Then the reservation is confirmed

  Scenario: Refusing a reservation when the room does not exist
    Given no room exists in the system
    When I try to reserve room "non-existent-id" from "2026-07-01T09:00:00" to "2026-07-01T10:00:00" for "Bob"
    Then a 404 error is returned

  Scenario: Refusing a reservation that overlaps an existing confirmed reservation
    Given a room named "Salle B" with capacity 5 exists
    And the room is reserved from "2026-07-01T10:00:00" to "2026-07-01T11:00:00" by "Alice"
    When I reserve this room from "2026-07-01T10:30:00" to "2026-07-01T11:30:00" for "Bob"
    Then a conflict error is returned
