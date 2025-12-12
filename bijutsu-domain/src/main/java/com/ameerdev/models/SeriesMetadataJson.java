package com.ameerdev.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesMetadataJson {
    private String version;
    private Metadata metadata;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        private String type;
        private String publisher;
        private String imprint;
        private String name;
        private Integer comicid;
        private Integer year;

        @JsonProperty("description_text")
        private String descriptionText;

        @JsonProperty("description_formatted")
        private String descriptionFormatted;

        private Integer volume;
        private String booktype;

        @JsonProperty("age_rating")
        private String ageRating;

        private List<CollectedItem> collects;

        @JsonProperty("comic_image")
        private String comicImage;

        @JsonProperty("total_issues")
        private Integer totalIssues;

        @JsonProperty("publication_run")
        private String publicationRun;

        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CollectedItem {
        private String series;
        private String comicid;
        private String issueid;
        private String issues;
    }
}