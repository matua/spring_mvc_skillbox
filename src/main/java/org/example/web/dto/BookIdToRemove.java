package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class BookIdToRemove {
    @NotNull
    @Digits(integer = 30, fraction = 0)
    @Min(1)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
