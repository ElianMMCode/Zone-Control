package laboratorioxyz.com.ZoneControl.modulo_administracion.controller;

import jakarta.validation.Valid;
import laboratorioxyz.com.ZoneControl.model.enums.Status;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.CreateUserRequest;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.UpdateUserRequest;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.UserResponse;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, Status> body) {
        UserResponse response = userService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable UUID id) {
        Map<String, String> response = userService.resetPassword(id);
        return ResponseEntity.ok(response);
    }
}
