package helpers;

import domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class UserHelper {

    public static User generateUser(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 8 + 1);
        String timeNow = DateTimeFormatter.ofPattern("ddMMyyyyhhmmss").format(LocalDateTime.now());
        String email = "totallynotfake" + timeNow + randomNum + "@fakemail.com";
        return new User(email, "FakeFirstName", "lastName", "parole");
    }
}
