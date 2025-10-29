package com.ameerdev.model;

import lombok.extern.jackson.Jacksonized;

public enum BookType {
    MANGA(ReadDirection.RIGHT_TO_LEFT),
    COMIC(ReadDirection.LEFT_TO_RIGHT);

    public final ReadDirection defaultReadDirection;

    BookType(ReadDirection readDirection) {
        this.defaultReadDirection = readDirection;
    }
}
