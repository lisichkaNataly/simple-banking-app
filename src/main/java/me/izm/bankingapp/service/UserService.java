package me.izm.bankingapp.service;

import me.izm.bankingapp.exception.InvalidPasswordException;
import me.izm.bankingapp.exception.UserAlreadyExistsException;
import me.izm.bankingapp.exception.UserNotFoundException;
import me.izm.bankingapp.model.Account;
import me.izm.bankingapp.model.Currency;
import me.izm.bankingapp.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public User addUser(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        users.put(user.getUsername(), user);
        return createNewUserAccounts(user);
    }

    public User updateUser(String username, String firstName, String lastName) {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException();
        }
        User user = users.get(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    public void updatePassword(String username, String password, String newPassword) {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException();
        }
        User user = users.get(username);
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException();
        }
        user.setPassword(newPassword);
    }

    public User removeUser(String username) {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException();
        }
        return users.remove(username);
    }

    public User getUser(String username) {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException();
        }
        return users.get(username);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    private User createNewUserAccounts(User user) {
        user.addAccount(new Account(UUID.randomUUID().toString(),0.0, Currency.RUB));
        user.addAccount(new Account(UUID.randomUUID().toString(),0.0, Currency.EUR));
        user.addAccount(new Account(UUID.randomUUID().toString(),0.0, Currency.USD));
        return user;
    }
}
