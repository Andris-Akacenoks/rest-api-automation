package helpers;

import domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserHelper {

    public static User generateUser(){
        String timeNow = DateTimeFormatter.ofPattern("ddMMyyyyhhmmss").format(LocalDateTime.now());
        String email = "totallynotfake" + timeNow + "@fakemail.com";
        return new User(email, "FakeFirstName", "lastName", "parole");
    }

}

