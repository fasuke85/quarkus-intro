package no.kantega;

import io.quarkus.test.junit.QuarkusTest;
import no.kantega.data.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class UserEndpointTest {

    /**
     * Vi starter med å lage et endepunkt (RestEasy) på localhost:9000/users, som returnerer html (med Qute-templater)
     * <p>
     * Vi skal i denne testen rendre <h1>User</h1>.
     * <p>
     * Man kan ta en titt på SomePage og GreetingResource for inspirasjon, eller sjekke https://quarkus.io/guides/qute
     * for en kjapp intro.
     * <p>
     * Tips + Greit å vite:
     * - Navnet på template-feltet i java-klassen må matche html-filen (sånn som i SomePage), og for å gjøre ting enkelt bør den ligge rett under /templates
     * - Hvis noe er feil er det gjerne enklere å åpne localhost:9000/users i browseren
     * - Denne test-suiten bruker rest-assured til å teste mot http-endepunktene, @QuarkusTest sørger for at vi har en app som svarer
     * - Annotasjonene som brukes av RestEasy kalles for JAX-RS.
     */
    @Test
    @Disabled
    public void userEndpointReturnsHtmlWithH1() {
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .contentType("text/html")
                .body(containsString("<h1>User</h1>"));
    }

    /**
     * Qute gir oss muligheten til å mikse java-objekter med html, i denne testen skal vi lage en liste av User.java og rendre det
     * som en html-tabell med en rad per User.
     * - Tabellen må innholde en kolonne for hver felt i User.java
     * - Vi bruker dummy data, men tabellen må inneholde en bruker med id 1, og en annen med id 2
     * <p>
     * Tips:
     * - Hvordan lage løkker i Qute? https://quarkus.io/guides/qute#rendering-periodic-reports
     * - Dersom man legger til {@java.util.List<no.kantega.data.User> users} i toppen av html-filen vil users-listen type-sjekkes
     *   Test ut hva som blir forskjellen med og uten denne sjekken.
     */
    @Test
    @Disabled
    public void userEndpointReturnsTable() {
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .contentType("text/html")
                .body(allOf(
                        containsString("<th>Id</th>"),
                        containsString("<th>Name</th>"),
                        containsString("<td>1</td>"),
                        containsString("<td>2</td>")
                ));
    }

    /**
     * Istedenfor vår egen dummy-data skal vi nå hente data fra https://jsonplaceholder.typicode.com/users.
     * Dette gjør vi med å lage en rest-klient: https://quarkus.io/guides/rest-client
     * <p>
     * Tips + Greit å vite:
     * - Restklienten er et interface
     * - Restklienten bruker samme type annotasjoner som RestEasy.
     * - User.java og listen vi lagde i forrige test matcher det som kommer fra jsonplaceholder.
     * - Restklienten må konfigureres opp i application.properties
     * - Restklienten kommer fra et bibliotek som heter MicroPofile
     */
    @Test
    @Disabled
    public void userEndpointReturnsTableWithRealUsers() {
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .contentType("text/html")
                .body(allOf(
                        containsString("<td>Julianne.OConner@kory.org</td>"),
                        containsString("<td>Patricia Lebsack</td>"),
                        containsString("<td>Chelsey Dietrich</td>"),
                        containsString("<td>Lucio_Hettinger@annie.ca</td>")
                ));
    }

    /**
     * Gi støtte for et query-parameter hideContactInfo=true som gjør det mulig å skjule kontaktinformasjon
     * <p>
     * Tips:
     * - Query-parametre: Se SomePage.java
     * - Hvordan bruke if i Qute? https://quarkus.io/guides/qute#rendering-periodic-reports
     */
    @Test
    @Disabled
    public void hideContactInfoQueryParamDoesWhatItsSupposedToDo() {
        given()
                .when().get("/users?hideContactInfo=true")
                .then()
                .statusCode(200)
                .contentType("text/html")
                .body(allOf(
                        not(containsString("Shanna@melissa.tv")),
                        not(containsString("010-692-6593 x09125")),
                        not(containsString("Email")),
                        not(containsString("Phone"))
                ));
    }

    /**
     * Fram til nå har endepunktetene returnert html, nå skal vi lage et som returnerer json. Lag et endepunkt som støtter
     * kall på formatet: /users/{id} og som returnerer den gitte brukeren fra json-placeholder i json-format
     * <p>
     * Tips:
     * - Restklienten må utvides med en metode som kaller på https://jsonplaceholder.typicode.com/users/{id}
     * - Path-parametre: https://quarkus.io/guides/qute#hello-world-with-jax-rs
     */
    @Test
    @Disabled
    public void usersByIdReturnsGivenUser() {
        given()
                .when().get("/users/3")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(
                        "name", is("Clementine Bauch"),
                        "id", is(3),
                        "username", is("Samantha")
                );
    }

    /**
     * Lag et endepunkt som gjør det mulig å poste inn en ny user
     *
     * Tips:
     * - Et post-kall til https://jsonplaceholder.typicode.com/users vil alltid returnere det du sender inn, men med id lik 11
     * - Du trenger  @Consumes i tillegg til @Produces
     * - Hvis man vil lese mer om post-annotasjonen: https://quarkus.io/guides/rest-json
     *
     */
    @Test
    @Disabled
    public void createsUser() {
        User user = new User();
        user.name = "John Doe";
        user.username = "Doeman";
        user.email = "jd@mail.com";

        given()
                .contentType("application/json")
                .body(user)
                .when().post("/users")
                .then()
                .statusCode(200)
                .body(
                        "id", is(11),
                        "name", is("John Doe"),
                        "username", is("Doeman"),
                        "email", is("jd@mail.com")
                );
    }
}
