package com.tripagent.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberCreateRequest(
        @NotBlank(message = "Member email is required.")
        @Email(message = "Member email must be valid.")
        String email,
        @NotBlank(message = "Member password is required.")
        @Size(min = 8, max = 100, message = "Member password must be between 8 and 100 characters.")
        String password,
        @NotBlank(message = "Member nickname is required.")
        @Size(max = 50, message = "Member nickname must be 50 characters or less.")
        String nickname
) {
}
