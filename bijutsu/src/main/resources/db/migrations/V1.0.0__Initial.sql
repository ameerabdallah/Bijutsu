CREATE TYPE read_direction AS ENUM ('DEFAULT', 'LTR', 'RTL');
CREATE TYPE book_type AS ENUM ('MANGA', 'COMIC');
COMMIT;

CREATE TABLE library
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255)                     NOT NULL,
    description    TEXT           DEFAULT ''        NOT NULL,
    read_direction read_direction DEFAULT 'DEFAULT' NOT NULL,
    book_type      book_type                        NOT NULL
);

CREATE TABLE library_directory
(
    id         BIGSERIAL PRIMARY KEY,
    library_id BIGINT NOT NULL REFERENCES library (id) ON DELETE CASCADE,
    path       TEXT   NOT NULL
);

CREATE TABLE series
(
    id                 BIGSERIAL PRIMARY KEY,
    library_id         BIGINT                  NOT NULL REFERENCES library (id) ON DELETE CASCADE,
    path               TEXT                    NOT NULL,
    title              VARCHAR(255)            NOT NULL,
    author             VARCHAR(255) DEFAULT '' NOT NULL,
    description        TEXT         DEFAULT '' NOT NULL,
    release_year       INTEGER,
    metadata_source_id VARCHAR(255)
);

CREATE TABLE release
(
    id                 BIGSERIAL PRIMARY KEY,
    series_id          BIGINT       NOT NULL REFERENCES series (id) ON DELETE CASCADE,
    file_path          TEXT         NOT NULL,
    hash               BYTEA,
    title              VARCHAR(255) NOT NULL,
    index              INTEGER      NOT NULL,
    release_date       DATE,
    metadata_source_id VARCHAR(255)
);