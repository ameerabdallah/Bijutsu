package com.ameerdev.resource;

import com.ameerdev.service.SeriesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/v1/series")
public class SeriesResourceV1 {
    @Inject
    SeriesService seriesService;

    @GET
    @Path("/getAllSeries")
    public Response getAllSeries(
            @QueryParam("libraryId") int libraryId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize,
            @QueryParam("sortBy") @DefaultValue("title") String sortBy
    ) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/refreshMetadata/{seriesId}")
    public Response refreshMetadata(@PathParam("seriesId") int seriesId) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }


}
