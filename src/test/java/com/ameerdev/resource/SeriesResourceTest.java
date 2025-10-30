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
class SeriesResourceTest {

    @BeforeAll
    static void setUp() {
        // TODO: push data to mock db
    }

    @Test
    void getAllSeriesPageOutOfBounds() {
        given()
                .when().get("/series/getAllSeries", libraryId, 4, 20)
                .thenReturn()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void getAllSeriesPageUnderBounds() {
        given()
                .when().get("/series/getAllSeries", libraryId, -1, 20)
                .thenReturn()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void getAllSeriesPageSizeTooLarge() {
        given()
                .when().get("/series/getAllSeries", libraryId, 1, 10000)
                .thenReturn()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void getAllSeriesSuccess() {
        given()
                .when().get("/series/getAllSeries", libraryId, 1, 20)
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
                .when().post("/series/refreshMetadata/" + metadataId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }
}