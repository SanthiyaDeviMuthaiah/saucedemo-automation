package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BookingApiTest {

    private String token;
    private int bookingId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        token = given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"admin\",\"password\":\"password123\"}")
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().path("token");
    }

    @Test(priority = 1, groups = {"api"})
    public void createBooking() {
        String body = """
            {
              "firstname": "Santhiya",
              "lastname": "Devi",
              "totalprice": 1500,
              "depositpaid": true,
              "bookingdates": {"checkin": "2026-11-01", "checkout": "2026-11-05"},
              "additionalneeds": "Breakfast"
            }
            """;

        bookingId = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo("Santhiya"))
                .body("booking.totalprice", equalTo(1500))
                .extract().path("bookingid");
    }

    @Test(priority = 2, groups = {"api"})
    public void getBooking() {
        given()
                .header("Accept","application/json")
                .header("User-Agent","Mozila/5.0")
                .when()
                .get("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo("Santhiya"))
                .body("bookingdates.checkin", equalTo("2026-11-01"));
    }

    @Test(priority = 3, groups = {"api"})
    public void updateBooking() {
        String body = """
            {
              "firstname": "Santhiya",
              "lastname": "Muthaiah",
              "totalprice": 2000,
              "depositpaid": false,
              "bookingdates": {"checkin": "2026-11-01", "checkout": "2026-11-07"
              },
              "additionalneeds": "Dinner"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .accept("application/json")
                .header("Cookie", "token=" + token)
                .body(body)
                .log().all()
                .when()
                .put("/booking/" + bookingId)
                .then()
                .log().all()
                .statusCode(200)
                .body("lastname", equalTo("Muthaiah"))
                .body("totalprice", equalTo(2000));
    }

    @Test(priority = 4, groups = {"api"})
    public void deleteBooking() {
        given()
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(201);
    }

    @Test(priority = 5, groups = {"api"})
    public void getDeletedBookingShouldReturn404() {
        given()
                .when()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(404);
    }
}