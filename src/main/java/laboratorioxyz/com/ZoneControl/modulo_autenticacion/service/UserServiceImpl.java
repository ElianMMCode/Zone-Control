package laboratorioxyz.com.ZoneControl.modulo_autenticacion.service;

import laboratorioxyz.com.ZoneControl.common.util.PasswordGenerator;
import laboratorioxyz.com.ZoneControl.model.enums.Status;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.CreateUserRequest;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.UpdateUserRequest;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.UserResponse;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.model.User;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.repository.UserRepository;
import laboratorioxyz.com.ZoneControl.modulo_gestion_personal.model.Employee;
import laboratorioxyz.com.ZoneControl.modulo_gestion_personal.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Empleado no encontrado"));
        if (userRepository.findByEmployee_Id(request.getEmployeeId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El empleado ya tiene un usuario de sistema asociado");
        }
        String tempPassword = PasswordGenerator.generateTemporaryPassword();
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(tempPassword))
                .role(request.getRole())
                .status(Status.ACTIVO)
                .requirePasswordChange(true)
                .employee(employee)
                .build();
        user = userRepository.save(user);
        log.info("User created: id={}, email={}, role={}", user.getId(), user.getEmail(), user.getRole());
        UserResponse response = toResponse(user);
        response.setTemporaryPassword(tempPassword);
        return response;
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "El email ya está registrado");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        user = userRepository.save(user);
        log.info("User updated: id={}", user.getId());
        return toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateStatus(UUID id, Status newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));
        user.setStatus(newStatus);
        user = userRepository.save(user);
        log.info("User status changed: id={}, newStatus={}", id, newStatus);
        return toResponse(user);
    }

    @Override
    @Transactional
    public Map<String, String> resetPassword(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));
        String tempPassword = PasswordGenerator.generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setRequirePasswordChange(true);
        userRepository.save(user);
        log.info("Password reset for user id={}", id);
        return Map.of("temporaryPassword", tempPassword);
    }

    @Override
    @Transactional
    public void deactivateByEmployeeId(UUID employeeId) {
        userRepository.findByEmployee_Id(employeeId).ifPresent(user -> {
            user.setStatus(Status.INACTIVO);
            userRepository.save(user);
            log.info("User {} deactivated due to employee {} status change", user.getId(), employeeId);
        });
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .requirePasswordChange(user.isRequirePasswordChange())
                .build();
    }
}
