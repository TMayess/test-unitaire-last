package com.example.exo13.bdd;

import com.example.exo13.dto.AmountRequest;
import com.example.exo13.dto.CreateAccountRequest;
import com.example.exo13.dto.TransferRequest;
import com.example.exo13.exception.InsufficientFundsException;
import com.example.exo13.model.Account;
import com.example.exo13.service.AccountService;
import io.cucumber.java.fr.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountStepDefinitions {

    @Autowired
    private AccountService accountService;

    private Exception caughtException;

    @Soit("aucun compte avec le numéro {string}")
    public void aucun_compte_avec_le_numero(String number) {
        // état par défaut : aucun compte n'existe
    }

    @Quand("je crée un compte avec le numéro {string} et le propriétaire {string}")
    public void je_cree_un_compte(String number, String owner) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setNumber(number);
        request.setOwner(owner);
        accountService.createAccount(request);
    }

    @Alors("le compte {string} existe avec un solde de {double}")
    public void le_compte_existe_avec_un_solde(String number, double balance) {
        Account account = accountService.getByNumber(number);
        assertThat(account.getBalance()).isEqualTo(balance);
    }

    @Soit("un compte {string} appartenant à {string} avec un solde de {double}")
    public void un_compte_appartenant_a_avec_un_solde(String number, String owner, double balance) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setNumber(number);
        request.setOwner(owner);
        accountService.createAccount(request);
        accountService.deposit(number, new AmountRequest(balance));
    }

    @Quand("je dépose {double} sur le compte {string}")
    public void je_depose_sur_le_compte(double amount, String number) {
        accountService.deposit(number, new AmountRequest(amount));
    }

    @Alors("le solde du compte {string} est de {double}")
    public void le_solde_du_compte_est_de(String number, double balance) {
        Account account = accountService.getByNumber(number);
        assertThat(account.getBalance()).isEqualTo(balance);
    }

    @Quand("je retire {double} du compte {string}")
    public void je_retire_du_compte(double amount, String number) {
        accountService.withdraw(number, new AmountRequest(amount));
    }

    @Quand("je tente de retirer {double} du compte {string}")
    public void je_tente_de_retirer_du_compte(double amount, String number) {
        try {
            accountService.withdraw(number, new AmountRequest(amount));
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Alors("le retrait est refusé avec une erreur de fonds insuffisants")
    public void le_retrait_est_refuse() {
        assertThat(caughtException).isInstanceOf(InsufficientFundsException.class);
    }

    @Quand("je vire {double} du compte {string} vers le compte {string}")
    public void je_vire_du_compte_vers(double amount, String from, String to) {
        accountService.transfer(new TransferRequest(from, to, amount));
    }

    @Quand("je tente de virer {double} du compte {string} vers le compte {string}")
    public void je_tente_de_virer_du_compte_vers(double amount, String from, String to) {
        try {
            accountService.transfer(new TransferRequest(from, to, amount));
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Alors("le virement est refusé avec une erreur de fonds insuffisants")
    public void le_virement_est_refuse() {
        assertThat(caughtException).isInstanceOf(InsufficientFundsException.class);
    }
}
