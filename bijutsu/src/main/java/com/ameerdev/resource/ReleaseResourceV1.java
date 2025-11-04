package com.ameerdev.resource;

import com.ameerdev.service.ReleaseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/v1/release")
public class ReleaseResourceV1 {
    @Inject
    ReleaseService releaseService;

    @POST
    @Path("/refreshMetadata/{releaseId}")
    public Response refreshMetadata(@PathParam("releaseId") int releaseId) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
