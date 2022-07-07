package com.epam.esm.gifts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrderDto {
    @JsonProperty("user-id")
    private Long userId;
    @JsonProperty("certificates-id")
    private List<Long> certificateIdList;
}
