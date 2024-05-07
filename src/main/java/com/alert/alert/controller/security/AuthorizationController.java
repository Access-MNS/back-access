package com.alert.alert.controller.security;

import com.alert.alert.validation.IsAdmin;
import com.alert.alert.validation.IsUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@IsUser
public class AuthorizationController {

    @GetMapping("/admin/resource")
    @IsAdmin
    public ResponseEntity<String> sayHelloWithRoleAdminAndReadAuthority() {
        return ResponseEntity.ok("Hello, you have access to a protected resource that requires admin role.");
    }

    @PostMapping("/user/resource")
    public ResponseEntity<String> sayHelloWithRoleUserAndCreateAuthority() {
        return ResponseEntity.ok("Hello, you have access to a protected resource that requires user role.");
    }
}
