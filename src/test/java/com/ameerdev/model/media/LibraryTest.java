package com.ameerdev.model.media;

import com.ameerdev.model.BookType;
import com.ameerdev.model.ReadDirection;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class LibraryTest {

    @Test
    void overrideDefaultReadDirection() {
        Library library = new Library();
        library.setReadDirection(ReadDirection.LEFT_TO_RIGHT);
        library.setBookType(BookType.MANGA);

        ReadDirection readDirection = library.getReadDirection();

        assertNotEquals(BookType.MANGA.defaultReadDirection, readDirection);
        assertEquals(ReadDirection.LEFT_TO_RIGHT, readDirection);
    }

    @Test
    void useDefaultReadDirection() {
        Library library = new Library();
        library.setBookType(BookType.MANGA);

        ReadDirection readDirection = library.getReadDirection();

        assertEquals(BookType.MANGA.defaultReadDirection, readDirection);
    }
}