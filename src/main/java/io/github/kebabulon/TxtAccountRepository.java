package io.github.kebabulon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TxtAccountRepository implements AccountRepository {
    public static final Logger log = LoggerFactory.getLogger(TxtAccountRepository.class);


    private final Path file;

    public TxtAccountRepository(Path file) {
        this.file = file;
    }

    @Override
    public List<Account> findAll() {
        log.debug("Find all accounts");
        List<Account> accounts = new ArrayList<Account>();
        if(!Files.exists(file)) {
            return accounts;
        }
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.isBlank()) {
                    accounts.add(parse(line));
                }
            }
            reader.close();
        }
        catch(IOException e) {
            log.error("Error while reading accounts file : ", e.getCause());
            throw new RuntimeException(e);
        }
        log.debug("Found {} accounts", accounts.size());
        return accounts;
    }

    @Override
    public Account create(String owner) {
        if(owner == null || owner.contains(AccountUtils.SEPARTOR)) {
            throw new IllegalArgumentException("Invalid owner: " + owner);
        }
        var accounts = findAll();
        if(accounts.stream().anyMatch(a -> a.owner().equals(owner))) {
            throw new IllegalArgumentException("Owner not unique: " + owner);
        }
        long nextId = accounts.isEmpty() ? 1 : accounts.get(accounts.size() - 1).id() + 1;

        var account = new Account(
                nextId,
                owner,
                BigDecimal.ZERO
                );

        try(BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(AccountUtils.toLine(account));
            writer.newLine();
        }
        catch(IOException e) {
            log.error("Error while writing accounts file : ", e.getCause());
            throw new RuntimeException(e);
        }

        log.info("Created new account: " + AccountUtils.toLine(account));
        return account;
    }

    private static Account parse(String line) {
        String[] parts = line.split(AccountUtils.SEPARTOR);
        if(parts.length != 3) {
            throw new IllegalArgumentException("Incorrect string: " + line);
        }
        return new Account(
                Long.parseLong(parts[0].trim()),
                parts[1],
                new BigDecimal(parts[2].trim())
                );
    }

}
