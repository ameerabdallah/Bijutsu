package com.ameerdev.resource;

import com.ameerdev.jooq.enums.BookType;
import com.ameerdev.jooq.enums.ReadDirection;
import com.ameerdev.service.LibraryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/v1/library")
public class LibraryResourceV1 {
    public static final int MAX_PAGE_SIZE = 10000;
    @Inject
    LibraryService service;

    @POST
    public Response createNewLibrary(CreateLibrary library) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
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

    public record CreateLibrary(
            String name,
            String description,
            ReadDirection readDirection,
            BookType libraryType
    ){}
}
