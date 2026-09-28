package com.bankingapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class RoleTestController {

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerOnly() {
        return "Hello CUSTOMER";
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public String employeeOrAdmin() {
        return "Hello EMPLOYEE or ADMIN";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Hello ADMIN";
    }
}