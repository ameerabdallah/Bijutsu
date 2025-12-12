package com.ameerdev.repositories.jooq.mapper;

import com.ameerdev.models.enums.BookType;
import com.ameerdev.models.enums.ReadDirection;
import com.ameerdev.models.enums.ReleaseType;
import com.ameerdev.models.enums.SeriesType;
import org.jetbrains.annotations.Contract;

public class EnumConverters {
    public static class BookTypeConverter {
        @Contract(pure = true)
        public static BookType toDomainBookType(
                com.ameerdev.jooq.generated.enums.BookType bookType
        ) {
            return switch (bookType) {
                case MANGA -> BookType.MANGA;
                case COMIC -> BookType.COMIC;
            };
        }

        @Contract(pure = true)
        public static com.ameerdev.jooq.generated.enums.BookType toJooqBookType(
                BookType bookType
        ) {
            return switch (bookType) {
                case MANGA -> com.ameerdev.jooq.generated.enums.BookType.MANGA;
                case COMIC -> com.ameerdev.jooq.generated.enums.BookType.COMIC;
            };
        }
    }

    public static class ReadDirectionConverter {
        @Contract(pure = true)
        public static ReadDirection toDomainReadDirection(
                com.ameerdev.jooq.generated.enums.ReadDirection readDirection
        ) {
            return switch (readDirection) {
                case LTR -> ReadDirection.LEFT_TO_RIGHT;
                case RTL -> ReadDirection.RIGHT_TO_LEFT;
                case DEFAULT -> ReadDirection.DEFAULT;
            };
        }

        @Contract(pure = true)
        public static com.ameerdev.jooq.generated.enums.ReadDirection
        toJooqReadDirection(ReadDirection readDirection) {
            return switch (readDirection) {
                case LEFT_TO_RIGHT -> com.ameerdev.jooq.generated.enums.ReadDirection.LTR;
                case RIGHT_TO_LEFT -> com.ameerdev.jooq.generated.enums.ReadDirection.RTL;
                case DEFAULT -> com.ameerdev.jooq.generated.enums.ReadDirection.DEFAULT;
            };
        }
    }

    public static class ReleaseTypeConverter {
        @Contract(pure = true)
        public static ReleaseType toDomainReleaseType(
                com.ameerdev.jooq.generated.enums.ReleaseType releaseType) {
            return switch (releaseType) {
                case BOOK -> ReleaseType.BOOK;
                case VOLUME -> ReleaseType.VOLUME;
                case CHAPTER -> ReleaseType.CHAPTER;
            };
        }

        @Contract(pure = true)
        public static com.ameerdev.jooq.generated.enums.ReleaseType
        toJooqReleaseType(ReleaseType releaseType) {
            return switch (releaseType) {
                case BOOK -> com.ameerdev.jooq.generated.enums.ReleaseType.BOOK;
                case VOLUME -> com.ameerdev.jooq.generated.enums.ReleaseType.VOLUME;
                case CHAPTER -> com.ameerdev.jooq.generated.enums.ReleaseType.CHAPTER;
            };
        }
    }

    public static class SeriesTypeConverter {
        @Contract(pure = true)
        public static SeriesType toDomainSeriesType(
                com.ameerdev.jooq.generated.enums.SeriesType seriesType) {
            return switch (seriesType) {
                case ONE_SHOT -> SeriesType.ONE_SHOT;
                case SERIES -> SeriesType.SERIES;
            };
        }

        @Contract(pure = true)
        public static com.ameerdev.jooq.generated.enums.SeriesType
        toJooqSeriesType(SeriesType seriesType) {
            return switch (seriesType) {
                case ONE_SHOT -> com.ameerdev.jooq.generated.enums.SeriesType.ONE_SHOT;
                case SERIES -> com.ameerdev.jooq.generated.enums.SeriesType.SERIES;
            };
        }
    }
}