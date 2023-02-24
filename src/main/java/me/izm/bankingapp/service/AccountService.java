package me.izm.bankingapp.service;

import me.izm.bankingapp.exception.AccountNotFoundException;
import me.izm.bankingapp.exception.InsufficientFundsException;
import me.izm.bankingapp.exception.InvalidChangeAmountException;
import me.izm.bankingapp.exception.UserNotFoundException;
import me.izm.bankingapp.model.Account;
import me.izm.bankingapp.model.User;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final UserService userService;

    public AccountService(UserService userService) {
        this.userService = userService;
    }

    public Account changeBalance(
            String username, String accountNumber, Operation operation, double amount) {
        if (amount <= 0) {
            throw new InvalidChangeAmountException();
        }
        User user = userService.getUser(username);
        Account account = user.getAccount().stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(AccountNotFoundException::new);
        if (operation.equals(Operation.DEPOSIT)) {
            return depositOnAccount(account, amount);
        } else {
            return withdrawFromAccount(account, amount);
        }
    }

    private Account withdrawFromAccount(Account account, double amount) {
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - amount);
        return account;
    }

    private Account depositOnAccount(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        return account;
    }

}
