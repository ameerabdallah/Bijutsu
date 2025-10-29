package com.ameerdev.resource;

import com.ameerdev.service.SeriesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/series")
public class SeriesResource {
    @Inject
    SeriesService seriesService;

    @GET
    @Path("/getAllSeries")
    public Response getAllSeries(
            @QueryParam("libraryId") int libraryId,
            @QueryParam("page") int page,
            @QueryParam("pageSize") int pageSize
    ) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/refreshMetadata/{seriesId}")
    public Response refreshMetadata(@PathParam("seriesId") int seriesId) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }


}
