package com.tripagent.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Login email is required.")
        @Email(message = "Login email must be valid.")
        String email,
        @NotBlank(message = "Login password is required.")
        String password
) {
}
