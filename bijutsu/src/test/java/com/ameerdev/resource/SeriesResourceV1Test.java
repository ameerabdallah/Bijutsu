package com.ameerdev.resource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Disabled
class SeriesResourceV1Test {
    // TODO: Add mock data setup and teardown
    private static final int libraryId = 1;

    @BeforeAll
    static void setUp() {
        // TODO: push data to mock db
    }

    @Test
    void getAllSeriesPageOutOfBounds() {
        given()
                .queryParam("libraryId", libraryId)
                .queryParam("page", 4)
                .queryParam("pageSize", 20)
                .queryParam("sortBy", "title")
                .when().get("/v1/series/getAllSeries")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void getAllSeriesPageUnderBounds() {
        given()
                .queryParam("libraryId", libraryId)
                .queryParam("page", -1)
                .queryParam("pageSize", 20)
                .queryParam("sortBy", "title")
                .when().get("/v1/series/getAllSeries")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void getAllSeriesPageSizeTooLarge() {
        given()
                .queryParam("libraryId", libraryId)
                .queryParam("page", 1)
                .queryParam("pageSize", LibraryResourceV1.MAX_PAGE_SIZE + 1)
                .queryParam("sortBy", "title")
                .when().get("/v1/series/getAllSeries")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void getAllSeriesSuccess() {
        given()
                .queryParam("libraryId", libraryId)
                .queryParam("page", 1)
                .queryParam("pageSize", 20)
                .queryParam("sortBy", "title")
                .when().get("/v1/series/getAllSeries")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Disabled
    void refreshMetadata() {
        int metadataId = 0;
        // TODO: Push series metadata to mock db that is intended to be overwritten and store in metadataId

        given()
                .when().post("/v1/series/refreshMetadata/" + metadataId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }
}