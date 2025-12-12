package com.ameerdev.service;

import com.ameerdev.models.SeriesIdentifier;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ParserServiceTest {
    @Inject
    ParserService parserService;

    @Test
    void parseTitleYear() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("One Piece")
                .year(1997)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("One Piece (1997)");

        assertEquals(expected, actual);
    }

    @Test
    void parseTitleYearWithBraces() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Naruto")
                .year(2002)
                .metadataSourceId("12345")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Naruto (2002) {id-12345}");

        assertEquals(expected, actual);
    }

    @Test
    void parseTitleOnly() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Bleach")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Bleach");
        assertEquals(expected, actual);
    }

    @Test
    void parseEmptyName() {
        SeriesIdentifier expected = SeriesIdentifier.builder().build();
        SeriesIdentifier actual = parserService.parseSeriesName("");
        assertEquals(expected, actual);
    }

    @Test
    void idInOtherPosition() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Dragon Ball Z")
                .year(1989)
                .metadataSourceId("67890")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("{id-67890} Dragon Ball Z (1989)");

        assertEquals(expected, actual);
    }

    @Test
    void parseNullName() {
        SeriesIdentifier expected = SeriesIdentifier.builder().build();
        SeriesIdentifier actual = parserService.parseSeriesName(null);
        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithUnderscores() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Fullmetal Alchemist")
                .year(2003)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Fullmetal_Alchemist_(2003)");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithMultipleBraces() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Attack on Titan")
                .year(2013)
                .metadataSourceId("AOT123")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Attack on Titan (2013) {id-AOT123} {extra-info}");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithNoYear() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Death Note")
                .metadataSourceId("DN001")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Death Note {id-DN001}");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithNoTitle() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .year(2006)
                .metadataSourceId("CODEGEASS")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("(2006) {id-CODEGEASS}");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithOnlyBraces() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .metadataSourceId("ONLYID")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("{id-ONLYID}");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithNoRecognizableInfo() {
        SeriesIdentifier expected = SeriesIdentifier.builder().title("!!!@@@###$$$").build();
        SeriesIdentifier actual = parserService.parseSeriesName("!!!@@@###$$$");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithExtraSpaces() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("My Hero Academia")
                .year(2016)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("   My Hero Academia    (2016)   ");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithBracesInBetween() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Sword {info} Art Online")
                .year(2012)
                .metadataSourceId("SAO001")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Sword {info} Art Online (2012) {id-SAO001}");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithMultipleYears() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Code Geass (2006)")
                .year(2008)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Code Geass (2006) (2008)");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithSpecialCharacters() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Evangelion: 3.0+1.0 Thrice Upon a Time")
                .year(2021)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Evangelion: 3.0+1.0 Thrice Upon a Time (2021)");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithNoSpaces() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Pokémon")
                .year(1997)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Pokémon(1997)");

        assertEquals(expected, actual);
    }

    @Test
    void parseMultipleIds() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("Yu-Gi-Oh!")
                .year(2000)
                .metadataSourceId("SECOND")
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("Yu-Gi-Oh! (2000) {id-YG001} {id-SECOND}");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithSingleBrace() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("{ fds Fairy Tail {Cookies")
                .year(2009)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("{ fds Fairy Tail {Cookies (2009)");

        assertEquals(expected, actual);
    }

    @Test
    void parseNameWithRandomBraces() {
        SeriesIdentifier expected = SeriesIdentifier.builder()
                .title("{ fds Fairy Tail {Cookies}{}}")
                .year(2009)
                .build();
        SeriesIdentifier actual = parserService.parseSeriesName("{ fds Fairy Tail {Cookies}{}} (2009)");

        assertEquals(expected, actual);
    }
}