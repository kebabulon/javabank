package io.github.kebabulon;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> findAll();
    Optional<Account> findById();
    Account create(String owner);
    default Optional<Account> findById(long id) {
        return findAll().stream().filter(a -> a.id() == id).findFirst();
    }
}
