package com.musinsa.model.dto;

import com.musinsa.model.IdGettable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonIdResponse {
    private Long id;

    public static CommonIdResponse of(IdGettable idGettable) {
        return new CommonIdResponse(idGettable.getId());
    }
}
