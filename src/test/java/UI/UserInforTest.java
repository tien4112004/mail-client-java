package UI;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UserInforTest {
    @Test
    public void test() {
        UserInfor userInfor = new UserInfor();

        String usernameActual = userInfor.getUsername();
        String passwordActual = userInfor.getPassword();

        String usernameExpected = userInfor.getUsername();
        String passwordExpected = userInfor.getPassword();

        assertEquals(usernameActual, usernameExpected);
        assertEquals(passwordActual, passwordExpected);
    }
}
