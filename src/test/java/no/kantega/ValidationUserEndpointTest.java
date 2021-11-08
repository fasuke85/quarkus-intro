package no.kantega;

import io.quarkus.test.junit.QuarkusTest;
import no.kantega.data.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ValidationUserEndpointTest {

    /**
     * Lag en klasse UserValidator (med en fornuftig levetid) som sjekker at den nye brukeren har både name og username.
     * UserValidator skal injectes, og brukes i klassen der vi har implementert endepunktet.
     *
     * Tips:
     * - En intro til Quarkus sin DI: https://quarkus.io/guides/cdi
     * - Vi kan kaste en WebApplicationException for å få korrekt statuskode.
     */
    @Test
    @Disabled
    public void newUserMustHaveNameAndUsername() {
        User user = new User();

        given()
                .contentType("application/json")
                .body(user)
                .when().post("/users")
                .then()
                .statusCode(400)
                ;
    }

    /**
     * Utvid UserValidator til å bruke rest-klienten for å sjekke om det nye brukernavnet er unikt
     */
    @Test
    @Disabled
    public void newUserMustHaveUniqueUsername() {
        User user = new User();
        user.name = "John Doe";
        user.username = "Samantha";
        given()
                .contentType("application/json")
                .body(user)
                .when().post("/users")
                .then()
                .statusCode(400)
                ;
    }
}
