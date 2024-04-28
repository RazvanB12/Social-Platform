package com.disi.social_platform_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ClientDtoResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean active;
}
