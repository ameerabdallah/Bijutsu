package com.ameerdev.model.media;

import com.ameerdev.model.BookType;
import com.ameerdev.model.ReadDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Library {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private BookType bookType;

    @Setter
    private ReadDirection readDirection = ReadDirection.DEFAULT;

    public ReadDirection getReadDirection() {
        return readDirection == ReadDirection.DEFAULT ? bookType.defaultReadDirection : readDirection;
    }
}
