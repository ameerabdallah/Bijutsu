package com.ameerdev.resource;

import com.ameerdev.resource.dto.request.CreateLibraryDTO;
import com.ameerdev.service.LibraryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/v1/library")
public class LibraryResourceV1 {
    public static final int MAX_PAGE_SIZE = 10000;

    @Inject
    LibraryService service;

    @POST
    public Response createNewLibrary(CreateLibraryDTO library) {
        Optional<CreateLibraryDTO> result  = service.createNewLibrary(library);
        if (result.isPresent()) {
            return Response.status(Response.Status.CREATED).entity(result).build();
        } else {
            return Response.serverError().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{libraryId}")
    public Response deleteLibrary(@PathParam("libraryId") int libraryId) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/scanLibrary/{libraryId}")
    public Response scanLibrary(@PathParam("libraryId") int libraryId) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
