package Filter;

import Filter.ContentFilter;
import Message.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContentFilterTest {
    public final String[] EMPTY_LIST = new String[0];
    String[] recipientTo = new String[] { "rep1" };

    @Test
    public void testMatches() {
        // Arrange
        Message email1 = new Message("sender1", recipientTo, EMPTY_LIST, EMPTY_LIST, "this is the subject",
                "This is the content of the first email");
        Message email2 = new Message("sender1", recipientTo, EMPTY_LIST, EMPTY_LIST, "this is the subject",
                "This is the content of the first email");
        ContentFilter filter = new ContentFilter("first", "second");

        // Act
        boolean matches1 = filter.matches(email1);
        boolean matches2 = filter.matches(email2);

        // Assert
        assertTrue(matches1, "Expected email1 to match the filter");
        assertTrue(matches2, "Expected email2 to match the filter");
    }

    @Test
    public void testDoesNotMatch() {
        // Arrange
        Message email = new Message("sender1", recipientTo, EMPTY_LIST, EMPTY_LIST, "this is the subject",
                "This is the content of the first email");
        ContentFilter filter = new ContentFilter("nonexistent");

        // Act
        boolean matches = filter.matches(email);

        // Assert
        assertFalse(matches, "Expected email to not match the filter");
    }
}