package com.ameerdev.resource;

import com.ameerdev.models.Library;
import com.ameerdev.resource.dto.mappers.RequestMappers;
import com.ameerdev.resource.dto.request.CreateLibraryDTO;
import com.ameerdev.service.LibraryScannerService;
import com.ameerdev.service.LibraryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/v1/library")
public class LibraryResourceV1 {
    public static final int MAX_PAGE_SIZE = 10000;

    @Inject
    LibraryService service;
    @Inject
    LibraryScannerService libraryScannerService;
    @Inject
    LibraryService libraryService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewLibrary(CreateLibraryDTO library) {
        Optional<Library> result = service.createNewLibrary(RequestMappers.toLibrary(library));
        if (result.isPresent()) {
            return Response.status(Response.Status.CREATED).entity(result).build();
        } else {
            return Response.serverError().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{libraryId}")
    public Response deleteLibrary(@PathParam("libraryId") long libraryId) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/scanLibrary/{libraryId}")
    public Response scanLibrary(@PathParam("libraryId") long libraryId) {
        libraryScannerService.scanLibrary(libraryId);
        return Response.ok().build();
    }

    @GET
    @Path("/getAllLibraries")
    public Response getAllLibraries() {
        return Response.ok(libraryService.getAllLibraries()).build();
    }
}
