package com.alert.alert.payload.request;

import com.alert.alert.entities.User;
import com.alert.alert.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    @StrongPassword
    private String password;

    public User toUser() {
        return new User()
                .setLastName(lastName)
                .setFirstName(firstName)
                .setMail(email)
                .setPassword(password);
    }
}
