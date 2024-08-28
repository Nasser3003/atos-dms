package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.UserReadDto;
import net.atos.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users/all")
    public ResponseEntity<List<UserReadDto>> findAll() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserReadDto> loginEndpoint(@PathVariable String email) {
        return ResponseEntity.ok(adminService.getUserByEmail(email));
    }
}
