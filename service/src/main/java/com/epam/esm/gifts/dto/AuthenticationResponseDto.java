package com.epam.esm.gifts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseDto {
    @JsonProperty("username")
    private String username;
    @JsonProperty("token")
    private String token;
}