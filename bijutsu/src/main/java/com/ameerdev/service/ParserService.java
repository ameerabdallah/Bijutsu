package com.ameerdev.service;

import com.ameerdev.models.SeriesIdentifier;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ApplicationScoped
public class ParserService {
    private static final Pattern BRACE_PATTERN = Pattern.compile("\\{([^}]+)}");
    private static final Pattern YEAR_PATTERN = Pattern.compile("\\((\\d{4})\\)");

    public SeriesIdentifier parseSeriesName(@Nullable String name) {
        // grab series name and year
        log.info("Parsing series name: {}", name);

        if (name == null || name.isBlank()) {
            return SeriesIdentifier.builder().build();
        }

        String nameNormalized = name.strip();

        Map<String, String> metadata = new HashMap<>();

        Matcher braceMatcher = BRACE_PATTERN.matcher(nameNormalized);
        StringBuilder afterBraces = new StringBuilder();

        while (braceMatcher.find()) {
            String content = braceMatcher.group(1);
            String[] parts = content.split("-", 2);
            if (parts.length == 2) {
                metadata.put(parts[0].toLowerCase(), parts[1]);
                braceMatcher.appendReplacement(afterBraces, "");
            }
        }
        braceMatcher.appendTail(afterBraces);

        String nameRemaining = afterBraces.toString().strip();

        Integer year = null;
        int lastYearStartIndex = -1;
        int lastYearEndIndex = -1;
        Matcher yearMatcher = YEAR_PATTERN.matcher(nameRemaining);

        while (yearMatcher.find()) {
            year = Integer.parseInt(yearMatcher.group(1));
            lastYearStartIndex = yearMatcher.start();
            lastYearEndIndex = yearMatcher.end();
        }

        if (year != null) {
            nameRemaining = nameRemaining.substring(0, lastYearStartIndex) + nameRemaining.substring(lastYearEndIndex);
            nameRemaining = nameRemaining.strip();
        }

        String title = nameRemaining.isBlank() ? null : nameRemaining.replace('_', ' ').strip();

        return SeriesIdentifier.builder()
                .title(title)
                .year(year)
                .metadataSourceId(metadata.get("id"))
                .build();
    }
}
