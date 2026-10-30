package io.github.kebabulon;

public class AccountUtils {
    public static final String SEPARTOR = ";";

    public static String toLine(Account account) {
        return account.id() + SEPARTOR
            + account.owner() + SEPARTOR
            + account.balance().toPlainString();
    }
}
