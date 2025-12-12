package com.ameerdev.resource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ReleaseResourceV1Test {

//    @Inject
//    @InjectMock
//    MetadataProvider metadataProvider;

    @Test
    @Disabled
    void refreshMetadata() {
        int metadataId = 0;
        // TODO: Push release metadata to mock db that is intended to be overwritten and store in metadataId

        given()
                .when().post("/release/refreshMetadata/" + metadataId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }
}