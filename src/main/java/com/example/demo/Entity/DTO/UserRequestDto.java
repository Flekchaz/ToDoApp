package com.example.demo.Entity.DTO;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    private String firstName;
    private String lastName;
    //@Email(message = "Not a valid format")
    //@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "Invalid email format")
    private String email;

    public boolean isValid(){
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        return email.matches(emailRegex);
    }
}
