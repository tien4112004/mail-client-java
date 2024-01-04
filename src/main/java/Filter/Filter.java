package Filter;

import Email.Email;

public interface Filter {
    public abstract boolean matches(Email email);
}