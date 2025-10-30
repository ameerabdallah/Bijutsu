package com.ameerdev.resource;

import com.ameerdev.metadata_agent.MetadataAgent;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ReleaseResourceTest {

    @Inject
    @InjectMock
    MetadataAgent metadataAgent;

    @Test
    void refreshMetadata() {
        int metadataId = 0;
        // TODO: Push release metadata to mock db that is intended to be overwritten and store in metadataId

        given()
                .when().post("/release/refreshMetadata/" + metadataId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }
}