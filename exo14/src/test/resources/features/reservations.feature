Feature: Réservation d'ouvrages à la médiathèque

  Scenario: Réservation d'un ouvrage indisponible
    Given l'ouvrage "Clean Code" avec l'identifiant "B001" est indisponible
    And l'adhérent "Alice" avec l'identifiant "M001" est actif
    When l'adhérent "M001" réserve l'ouvrage "B001"
    Then la réservation est enregistrée pour l'ouvrage "B001"

  Scenario: Plusieurs réservations sur le même ouvrage
    Given l'ouvrage "Refactoring" avec l'identifiant "B002" est indisponible
    And l'adhérent "Bob" avec l'identifiant "M002" est actif
    And l'adhérent "Charlie" avec l'identifiant "M003" est actif
    When l'adhérent "M002" réserve l'ouvrage "B002"
    And l'adhérent "M003" réserve l'ouvrage "B002"
    Then il y a 2 réservations pour l'ouvrage "B002"

  Scenario: Restitution d'un ouvrage réservé notifie le prochain adhérent
    Given l'ouvrage "Design Patterns" avec l'identifiant "B003" est indisponible
    And l'adhérent "Dana" avec l'identifiant "M004" est actif
    And l'adhérent "M004" a réservé l'ouvrage "B003"
    When l'ouvrage "B003" est restitué
    Then la prochaine réservation de l'ouvrage "B003" est notifiée

  Scenario: Refus d'une réservation pour un adhérent suspendu
    Given l'ouvrage "Clean Architecture" avec l'identifiant "B004" est indisponible
    And l'adhérent "Eve" avec l'identifiant "M005" est suspendu
    When l'adhérent "M005" tente de réserver l'ouvrage "B004"
    Then la réservation est refusée pour cause de suspension

  Scenario: Un adhérent ne peut pas réserver un ouvrage disponible
    Given l'ouvrage "The Pragmatic Programmer" avec l'identifiant "B005" est disponible
    And l'adhérent "Frank" avec l'identifiant "M006" est actif
    When l'adhérent "M006" tente de réserver l'ouvrage "B005"
    Then la réservation est refusée car l'ouvrage est disponible
