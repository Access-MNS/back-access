package com.alert.alert.payload.request;

import com.alert.alert.entity.User;
import com.alert.alert.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Invalid Name: Empty name")
    @NotNull(message = "Invalid Name: Name is NULL")
    private String lastName;

    @NotBlank(message = "Invalid Firstname: Empty name")
    @NotNull(message = "Invalid Firstname: Name is NULL")
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
