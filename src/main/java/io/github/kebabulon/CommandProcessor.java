package io.github.kebabulon;

import java.io.PrintStream;
import java.util.Optional;

public class CommandProcessor {
    private final AccountRepository repository;
    private final PrintStream out;

    public CommandProcessor(AccountRepository repository, PrintStream out) {
        this.repository = repository;
        this.out = out;
    }

    public boolean execute(String input) {
        String line = input.trim();
        if(line.isEmpty()) {
            return false;
        }
        String[] parts = line.split("\\s+", 2);
        String command = parts[0];
        String argument = (parts.length > 1) ? parts[1] : "";

        switch(command) {
            case "create" -> create(argument);
            case "list" -> list();
            case "show" -> show(argument);
            case "exit" -> {
                return false;
            }
            // default -> throw new IllegalArgumentException("Invalid command");
            default -> out.println("Invalid command");
        }
        return true;
    }

    private void create(String owner) {
        if(owner.isEmpty()) {
            out.println("Error, no owner argument");
        }

        try {
            Account account = repository.create(owner);
            out.println("Created account: " + AccountUtils.toLine(account));
        }
        catch(IllegalArgumentException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void list() {
        var accounts = repository.findAll();
        if(accounts.isEmpty()) {
            out.println("No accounts");
            return;
        }
        for(Account account : accounts) {
            out.println(AccountUtils.toLine(account));
        }
    }

    private void show(String id) {
        if(id.isEmpty()) {
            out.println("Error, no id argument");
            return;
        }

        Optional<Account> account = repository.findById(Long.parseLong(id));
        if(account.isEmpty()) {
            out.println("Error, no account with id: " + id);
        }
        else {
            out.println(AccountUtils.toLine(account.get()));
        }
    }
}
