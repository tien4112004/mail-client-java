package Filter;

import Message.Message;

public interface Filter {
    public abstract boolean matches(Message email);
}