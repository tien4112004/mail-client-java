package UI;

import static org.junit.Assert.assertEquals;

public class UserInformationTest {
    // @Test
    public void test() {
        UserInformation userInfor = new UserInformation(new InputHandler());

        String usernameActual = userInfor.getUsername();
        String passwordActual = userInfor.getPassword();

        String usernameExpected = "example@localhost";
        String passwordExpected = "123456";

        System.out.print(usernameActual);
        System.out.print(passwordActual);

        assertEquals(usernameActual, usernameExpected);
        assertEquals(passwordActual, passwordExpected);
    }
}
