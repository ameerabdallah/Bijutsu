package com.ameerdev.service;

import com.ameerdev.models.SeriesIdentifier;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class SeriesService {
    public SeriesIdentifier parseSeriesName(String name) {
        log.info("Parsing series name: {}", name);
        // grab series name and year
        int seriesYear;
        if (name.endsWith(")")) {
            int openParenIndex = name.lastIndexOf('(');
        }
        return null;
    }
}
