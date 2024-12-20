package es.codeurjc.cosmic_news;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CosmicNewsApiTests {
    
    @LocalServerPort
    int port;

    private String accessToken;
    private String refreshToken;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://localhost"; 
        RestAssured.port = port; 
        RestAssured.useRelaxedHTTPSValidation(); 

        io.restassured.response.Response response = given()
            .body("{ \"username\": \"xd\", \"password\": \"xd\" }")
            .contentType(ContentType.JSON)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .extract()
            .response();
        
        String accessToken = response.getCookie("AuthToken");
        String refreshToken = response.getCookie("RefreshToken");

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // USER TESTS

    @Test
    public void createNewUserTest(){
        given().
            request()
                .body("{ \"name\" : \"Test\", \"surname\": \"User\", \"pass\" : \"test\", \"description\" : \"api test description\", \"nick\" : \"apiTest\", \"mail\" : \"apiTest\"}")
                .contentType(ContentType.JSON).
        when().
            post("/api/users").
        then().
            statusCode(200).
            body("nick", equalTo("apiTest"));
    }

    @Test
    public void getUserTest(){
        given().
            contentType(ContentType.JSON).
        when().
            get("/api/users/1").
        then().
            statusCode(200).
            body("id", equalTo(1));
    }

    @Test
    public void deleteUserTest(){
    given()
        .cookie("AuthToken", accessToken)
    .when()
        .delete("/api/users/1")
    .then()
        .statusCode(200);
    }

    @Test
    public void editUserTest(){
        given().
            request()
                .cookie("AuthToken", accessToken) 
                .cookie("RefreshToken", refreshToken)
                .body("{ \"name\" : \"TestEdit\", \"surname\": \"UserEdit\", \"description\" : \"api test edit description\"}")
                .contentType(ContentType.JSON).
        when().
            put("/api/users/1").
        then().
            statusCode(200).
            body("description", equalTo("api test edit description"));
    }

    // NEWS TESTS

    @Test
    public void createNewsTest(){
    given()
        .body("{ \"title\" : \"Test\", \"subtitle\": \"News\", \"bodyText\" : \"Api test news\"}")
        .contentType(ContentType.JSON)
    .when()
        .post("/api/news")
    .then()
        .statusCode(401);
    }

    @Test
    public void createNewsAuthTest(){
    given()
        .cookie("AuthToken", accessToken) 
        .cookie("RefreshToken", refreshToken)
        .body("{ \"title\" : \"Test\", \"subtitle\": \"News\", \"bodyText\" : \"Api test news\"}")
        .contentType(ContentType.JSON)
    .when()
        .post("/api/news")
    .then()
        .statusCode(200)
        .body("bodyText", equalTo("Api test news"));
    }

    @Test
    public void getNewsTest(){
        given().
            contentType(ContentType.JSON).
        when().
            get("/api/news/1").
        then().
            statusCode(200).
            body("id", equalTo(1));
    }

    @Test
    public void deleteNewsTest(){
    given()
        .cookie("AuthToken", accessToken)
    .when()
        .delete("/api/news/2")
    .then()
        .statusCode(200);
    }

    @Test
    public void editNewsTest(){
        given().
            request()
                .cookie("AuthToken", accessToken) 
                .cookie("RefreshToken", refreshToken)
                .body("{ \"title\" : \"TestEdit\", \"author\": \"TESTEDIT\"}")
                .contentType(ContentType.JSON).
        when().
            put("/api/news/3").
        then().
            statusCode(200).
            body("title", equalTo("TestEdit"));
    }

    // PICTURE TESTS

    @Test
    public void createPictureAuthTest(){
    given()
        .cookie("AuthToken", accessToken)
        .cookie("RefreshToken", refreshToken)
        .body("{ \"title\" : \"Test\", \"author\": \"API REST\", \"description\" : \"Api test picture\"}")
        .contentType(ContentType.JSON)
    .when()
        .post("/api/pictures")
    .then()
        .statusCode(200)
        .body("description", equalTo("Api test picture"));
    }

    @Test
    public void getPictureTest(){
        given().
            contentType(ContentType.JSON).
        when().
            get("/api/pictures/1").
        then().
            statusCode(200).
            body("id", equalTo(1));
    }

    @Test
    public void deletePictureTest(){
    given()
        .cookie("AuthToken", accessToken)
    .when()
        .delete("/api/pictures/3")
    .then()
        .statusCode(200);
    }

    @Test
    public void editPictureTest(){
        given().
            request()
                .cookie("AuthToken", accessToken) 
                .cookie("RefreshToken", refreshToken)
                .body("{ \"title\" : \"TestEdit\", \"author\": \"TESTEDIT\"}")
                .contentType(ContentType.JSON).
        when().
            put("/api/pictures/4").
        then().
            statusCode(200).
            body("title", equalTo("TestEdit"));
    }

    // VIDEO TESTS

    @Test
    public void createVideoAuthTest(){
    given()
        .cookie("AuthToken", accessToken) 
        .cookie("RefreshToken", refreshToken)
        .body("{ \"title\" : \"Test\", \"duration\": \"0:01\", \"description\" : \"Api test video\"} , \"videoUrl\" : \"dMEho2ZcVtE\"}")
        .contentType(ContentType.JSON)
    .when()
        .post("/api/videos")
    .then()
        .statusCode(200)
        .body("description", equalTo("Api test video"));
    }

    @Test
    public void getVideoTest(){
        given().
            contentType(ContentType.JSON).
        when().
            get("/api/videos/1").
        then().
            statusCode(200).
            body("id", equalTo(1));
    }

    @Test
    public void deleteVideoTest(){
    given()
        .cookie("AuthToken", accessToken)
    .when()
        .delete("/api/videos/1")
    .then()
        .statusCode(200);
    }

    @Test
    public void editVideoTest(){
        given().
            request()
                .cookie("AuthToken", accessToken) 
                .cookie("RefreshToken", refreshToken)
                .body("{ \"title\" : \"TestEdit\", \"description\" : \"api test edit description\"}")
                .contentType(ContentType.JSON).
        when().
            put("/api/videos/2").
        then().
            statusCode(200).
            body("description", equalTo("api test edit description"));
    }

    // EVENT TESTS

    @Test
    public void createEventAuthTest(){
    given()
        .cookie("AuthToken", accessToken) 
        .cookie("RefreshToken", refreshToken)
        .body("{ \"date\" : \"2024-12-12\", \"icon\": \"bi bi-moon-fill\", \"description\" : \"Api test event\"}")
        .contentType(ContentType.JSON)
    .when()
        .post("/api/events")
    .then()
        .statusCode(200)
        .body("description", equalTo("Api test event"));
    }

    @Test
    public void getEventTest(){
        given().
            contentType(ContentType.JSON).
        when().
            get("/api/events/1").
        then().
            statusCode(200).
            body("id", equalTo(1));
    }

    @Test
    public void deleteEventTest(){
    given()
        .cookie("AuthToken", accessToken)
    .when()
        .delete("/api/events/1")
    .then()
        .statusCode(200);
    }

    @Test
    public void editEventTest(){
        given().
            request()
                .cookie("AuthToken", accessToken) 
                .cookie("RefreshToken", refreshToken)
                .body("{ \"date\" : \"2024-12-12\", \"description\" : \"api test edit description\"}")
                .contentType(ContentType.JSON).
        when().
            put("/api/events/2").
        then().
            statusCode(200).
            body("description", equalTo("api test edit description"));
    }

    // QUIZZ TESTS

    @Test
    public void createQuizAuthTest() {
        given()
            .cookie("AuthToken", accessToken)
            .cookie("RefreshToken", refreshToken)
            .body("{ " +
                "\"name\": \"API TEST Quizz\"," +
                "\"difficulty\": \"Difícil\"," +
                "\"questions\": [" +
                "    {" +
                "        \"question\": \"¿Cómo se llama la página web?\"," +
                "        \"option1\": \"Astro News\"," +
                "        \"option2\": \"Cosmic News\"," +
                "        \"option3\": \"Astronomy For All\"," +
                "        \"option4\": \"Hidden World\"," +
                "        \"answer\": \"Cosmic News\"," +
                "        \"correct1\": false," +
                "        \"correct2\": true," +
                "        \"correct3\": false," +
                "        \"correct4\": false" +
                "    }," +
                "    {" +
                "        \"question\": \"¿Si quieres informarte acerca del Sistema Solar dónde debes acudir?\"," +
                "        \"option1\": \"Sistema Solar\"," +
                "        \"option2\": \"Perfil\"," +
                "        \"option3\": \"Fotos\"," +
                "        \"option4\": \"Calendario\"," +
                "        \"answer\": \"Sistema Solar\"," +
                "        \"correct1\": false," +
                "        \"correct2\": false," +
                "        \"correct3\": false," +
                "        \"correct4\": true" +
                "    }" +
                "]" +
            "}")
            .contentType(ContentType.JSON)
        .when()
            .post("/api/quizzes")
        .then()
            .statusCode(200)
            .body("name", equalTo("API TEST Quizz"))
            .body("questions.size()", equalTo(2));
    }

    @Test
    public void getQuizzTest(){
        given().
            contentType(ContentType.JSON).
        when().
            get("/api/quizzes/2").
        then().
            statusCode(200).
            body("id", equalTo(2));
    }

    @Test
    public void deleteQuizzTest(){
    given()
        .cookie("AuthToken", accessToken)
    .when()
        .delete("/api/quizzes/1")
    .then()
        .statusCode(200);
    }

    @Test
    public void editQuizzTest() {
        given()
            .cookie("AuthToken", accessToken)
            .cookie("RefreshToken", refreshToken)
            .body("{ " +
                "\"name\": \"API TEST EDIT Quizz\"," +
                "\"difficulty\": \"Difícil\"," +
                "\"questions\": [" +
                "    {" +
                "        \"question\": \"¿Cómo se llama la página web?\"," +
                "        \"option1\": \"Astro News\"," +
                "        \"option2\": \"Cosmic News\"," +
                "        \"option3\": \"Astronomy For All\"," +
                "        \"option4\": \"Hidden World\"," +
                "        \"answer\": \"Cosmic News\"," +
                "        \"correct1\": false," +
                "        \"correct2\": true," +
                "        \"correct3\": false," +
                "        \"correct4\": false" +
                "    }," +
                "    {" +
                "        \"question\": \"¿Si quieres informarte acerca del Sistema Solar dónde debes acudir?\"," +
                "        \"option1\": \"Sistema Solar\"," +
                "        \"option2\": \"Perfil\"," +
                "        \"option3\": \"Fotos\"," +
                "        \"option4\": \"Calendario\"," +
                "        \"answer\": \"Sistema Solar\"," +
                "        \"correct1\": false," +
                "        \"correct2\": false," +
                "        \"correct3\": false," +
                "        \"correct4\": true" +
                "    }" +
                "]" +
            "}")
            .contentType(ContentType.JSON)
        .when()
            .put("/api/quizzes/2")
        .then()
            .statusCode(200)
            .body("name", equalTo("API TEST EDIT Quizz"))
            .body("questions.size()", equalTo(2));
    }
    
}
