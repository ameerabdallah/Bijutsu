package com.ameerdev.resource;

import com.ameerdev.model.BookType;
import com.ameerdev.model.ReadDirection;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;

@QuarkusTest
class LibraryResourceTest {

    public static int libraryId;
    @BeforeAll
    static void setUp() {
        // TODO: Generate new Library row in Mock DB and store the id in libraryId
//        libraryId = ...
    }

    @Test
    void createNewLibrary() {
        LibraryResource.CreateLibrary createLibraryBody = new LibraryResource.CreateLibrary("Manga", "Manga Library", ReadDirection.DEFAULT, BookType.MANGA);

        given()
                .when().post("/library/createLibrary", createLibraryBody)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(any(Integer.class)); // return the new library id

        // ensure that the mock db has the new library
    }

    @Test
    void deleteLibrary() {
        given()
                .when().delete("/library/deleteLibrary/" + libraryId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        // TODO: ensure that the mock db has the new library
    }

    @Test
    void scanLibrary() {
        // TODO: Dynamically create a new series folder with a fake release

        given()
                .when().post("/library/scanLibrary" + libraryId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
        // TODO: check the mock db to make sure that the library picked up the new series
    }
}