package com.ameerdev.models.enums;

public enum SeriesType {
    SERIES("Series"), ONE_SHOT("One Shot");

    public final String displayName;

    SeriesType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
