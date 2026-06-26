Feature: Gestion des comptes bancaires

  Scenario: Création d'un compte bancaire
    Given aucun compte avec le numéro "FR100"
    When je crée un compte avec le numéro "FR100" et le propriétaire "Alice"
    Then le compte "FR100" existe avec un solde de 0.0

  Scenario: Dépôt sur un compte
    Given un compte "FR200" appartenant à "Bob" avec un solde de 100.0
    When je dépose 50.0 sur le compte "FR200"
    Then le solde du compte "FR200" est de 150.0

  Scenario: Retrait réussi sur un compte
    Given un compte "FR300" appartenant à "Charlie" avec un solde de 300.0
    When je retire 100.0 du compte "FR300"
    Then le solde du compte "FR300" est de 200.0

  Scenario: Retrait refusé pour solde insuffisant
    Given un compte "FR400" appartenant à "Dana" avec un solde de 50.0
    When je tente de retirer 200.0 du compte "FR400"
    Then le retrait est refusé avec une erreur de fonds insuffisants

  Scenario: Virement entre deux comptes
    Given un compte "FR500" appartenant à "Eve" avec un solde de 500.0
    And un compte "FR501" appartenant à "Frank" avec un solde de 100.0
    When je vire 200.0 du compte "FR500" vers le compte "FR501"
    Then le solde du compte "FR500" est de 300.0
    And le solde du compte "FR501" est de 300.0

  Scenario: Virement refusé pour solde insuffisant
    Given un compte "FR600" appartenant à "Grace" avec un solde de 50.0
    And un compte "FR601" appartenant à "Henry" avec un solde de 100.0
    When je tente de virer 500.0 du compte "FR600" vers le compte "FR601"
    Then le virement est refusé avec une erreur de fonds insuffisants
