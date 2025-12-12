package com.ameerdev.resource;

import com.ameerdev.service.ReleaseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.nio.file.Files;

@Path("/v1/release")
public class ReleaseResourceV1 {
    @Inject
    ReleaseService releaseService;

    @POST
    @Path("/refreshMetadata/{releaseId}")
    public Response refreshMetadata(@PathParam("releaseId") long releaseId) {
        return Response.serverError().status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("/series/{seriesId}")
    public Response getAllReleases(@PathParam("seriesId") long seriesId) {
        return Response.ok(releaseService.getReleasesBySeriesId(seriesId)).build();
    }

    // TODO: Revise
    @GET
    @Path("/download/{releaseId}")
    public Response downloadReleaseFile(@PathParam("releaseId") long releaseId) {
        return releaseService.getReleaseFilePath(releaseId)
                .map(filePath -> {
                    try {
                        // Validate file exists and is readable
                        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                            return Response.status(Response.Status.NOT_FOUND).build();
                        }

                        // Sanitize filename to prevent header injection
                        String sanitizedFilename = filePath.getFileName().toString()
                                .replaceAll("[^a-zA-Z0-9._-]", "_");

                        // Probe content type, fallback to binary stream
                        String contentType = Files.probeContentType(filePath);
                        if (contentType == null) {
                            contentType = "application/octet-stream";
                        }

                        return Response.ok(filePath.toFile())
                                .type(contentType)
                                .header(
                                        "Content-Disposition",
                                        "attachment; filename=\"" + sanitizedFilename + "\""
                                )
                                .build();

                    } catch (IOException e) {
                        // Log the error for ops visibility
                        // logger.error("Failed to access file for release {}", releaseId, e);
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

}
