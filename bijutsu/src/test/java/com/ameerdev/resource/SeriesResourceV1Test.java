package com.ameerdev.resource;

import com.ameerdev.metadata_agent.MetadataAgent;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SeriesResourceV1Test {

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
                .thenReturn()
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
                .thenReturn()
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
                .thenReturn()
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
                .thenReturn()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Inject
    @InjectMock
    MetadataAgent metadataAgent;

    @Test
    void refreshMetadata() {
        int metadataId = 0;
        // TODO: Push series metadata to mock db that is intended to be overwritten and store in metadataId

        given()
                .when().post("/v1/series/refreshMetadata/" + metadataId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }
}