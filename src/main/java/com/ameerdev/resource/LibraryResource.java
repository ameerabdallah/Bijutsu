package com.ameerdev.resource;

import com.ameerdev.model.BookType;
import com.ameerdev.model.ReadDirection;
import com.ameerdev.service.LibraryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/library")
public class LibraryResource {
    @Inject
    LibraryService service;

    @POST
    @Path("/createLibrary")
    public Response createNewLibrary(CreateLibrary library) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @DELETE
    @Path("/deleteLibrary/{libraryId}")
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
