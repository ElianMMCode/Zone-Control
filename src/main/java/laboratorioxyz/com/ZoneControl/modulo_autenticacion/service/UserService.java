package laboratorioxyz.com.ZoneControl.modulo_autenticacion.service;

import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.CreateUserRequest;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.UpdateUserRequest;
import laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto.UserResponse;
import laboratorioxyz.com.ZoneControl.model.enums.Status;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    UserResponse update(UUID id, UpdateUserRequest request);
    UserResponse updateStatus(UUID id, Status newStatus);
    Map<String, String> resetPassword(UUID id);
    void deactivateByEmployeeId(UUID employeeId);
}
