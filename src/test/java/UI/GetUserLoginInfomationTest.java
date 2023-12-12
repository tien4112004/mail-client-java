package UI;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GetUserLoginInfomationTest {
    @Test
    public void test() {
        GetUserLoginInfomation userInfor = new GetUserLoginInfomation();

        String usernameActual = userInfor.getUsername();
        String passwordActual = userInfor.getPassword();

        String usernameExpected = userInfor.getUsername();
        String passwordExpected = userInfor.getPassword();

        assertEquals(usernameActual, usernameExpected);
        assertEquals(passwordActual, passwordExpected);
    }
}
