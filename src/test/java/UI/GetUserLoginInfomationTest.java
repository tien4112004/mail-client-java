package UI;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GetUserLoginInfomationTest {
    @Test
    public void test() {
        GetUserLoginInfomation userInfor = new GetUserLoginInfomation();

        String usernameActual = userInfor.getUsername();
        String passwordActual = userInfor.getPassword();

        String usernameExpected = "example@localhost";
        String passwordExpected = "123456";

        assertEquals(usernameActual, usernameExpected);
        assertEquals(passwordActual, passwordExpected);
    }
}
