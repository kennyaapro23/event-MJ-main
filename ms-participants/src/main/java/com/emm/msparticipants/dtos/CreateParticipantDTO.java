package com.emm.msparticipants.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateParticipantDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 150)
    private String firstName;

    @Size(max = 150)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String phone;
}
