package com.ameerdev.tests_for_libraries;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ApacheLevenshteinDistanceTest {

    @Test
    public void testLevenshteinDistanceSame() {
        String left = "Chainsaw_Man";
        String right = "Chainsaw_Man";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(0, result);
    }

    @Test
    public void testLevenshteinDistanceOneOff() {
        String left = "ChainsawMan";
        String right = "Chainsaw_Man";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(1, result);
    }

    @Test
    public void testLevenshteinDistanceEmptyStrings() {
        String left = "";
        String right = "";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(0, result);
    }

    @Test
    public void testLevenshteinDistanceOneEmpty() {
        String left = "Chainsaw Man";
        String right = "";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(left.length(), result);
    }

    @Test
    public void testLevenshteinDistanceCompletelyDifferent() {
        String left = "Chainsaw Man";
        String right = "One Piece";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(11, result);
    }

    @Test
    public void testLevenshteinDistanceCaseSensitive() {
        String left = "chainsaw man";
        String right = "Chainsaw Man";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(2, result);
    }

    @Test
    public void testLevenshteinDistanceSubstring() {
        String left = "Chainsaw";
        String right = "Chainsaw Man";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(4, result);
    }

    @Test
    public void testLevenshteinDistanceTransposition() {
        String left = "Cahinsaw Man";
        String right = "Chainsaw Man";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(2, result);
    }

    @Test
    public void testLevenshteinDistanceMultipleChanges() {
        String left = "Chainsaw Man";
        String right = "Chainsaw-Man!";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(2, result);
    }

    @Test
    public void testLevenshteinDistanceUnicodeCharacters() {
        String left = "チェンソーマン";
        String right = "チェンソー・マン";

        Integer result = LevenshteinDistance.getDefaultInstance().apply(left, right);

        assertEquals(1, result);
    }

    @Test
    public void testLevenshteinDistanceWithThreshold() {
        String left = "Chainsaw Man";
        String right = "Chainsaw-Man";
        LevenshteinDistance distance = new LevenshteinDistance(5);

        Integer result = distance.apply(left, right);

        assertEquals(1, result);
    }

    @Test
    public void testLevenshteinDistanceExceedsThreshold() {
        String left = "Chainsaw Man";
        String right = "Completely Different";
        LevenshteinDistance distance = new LevenshteinDistance(5);

        Integer result = distance.apply(left, right);

        assertEquals(-1, result);
    }
}
