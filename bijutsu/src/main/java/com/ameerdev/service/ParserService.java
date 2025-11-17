package com.ameerdev.service;

import com.ameerdev.models.Series;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ParserService {
    public Series parseSeriesName(String name) {
        log.info("Parsing series name: {}", name);
        // grab series name and year
        int seriesYear;
        if (name.endsWith(")")) {
            int openParenIndex = name.lastIndexOf('(');
        }
    }
}
