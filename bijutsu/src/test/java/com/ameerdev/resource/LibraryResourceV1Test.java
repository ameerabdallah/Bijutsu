package com.ameerdev.resource;

import com.ameerdev.model.BookType;
import com.ameerdev.model.ReadDirection;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.any;
import static io.restassured.RestAssured.given;

@QuarkusTest
class LibraryResourceV1Test {

    public static int libraryId;
    @BeforeAll
    static void setUp() {
        // TODO: Generate new Library row in Mock DB and store the id in libraryId
//        libraryId = ...
    }

    @Test
    void createNewLibrary() {
        LibraryResourceV1.CreateLibrary createLibraryBody = new LibraryResourceV1.CreateLibrary("Manga", "Manga Library", ReadDirection.DEFAULT, BookType.MANGA);

        given()
                .when().post("/v1/library/", createLibraryBody)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body(any(Integer.class)); // return the new library id

        // ensure that the mock db has the new library
    }

    @Test
    void deleteLibrary() {
        given()
                .when().delete("/v1/library/", libraryId)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        // TODO: ensure that the mock db has the new library
    }

    @Test
    void scanLibrary() {
        // TODO: Dynamically create a new series folder with a fake release

        given()
                .when().post("/v1/library/scanLibrary" + libraryId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
        // TODO: check the mock db to make sure that the library picked up the new series
    }
}