package laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import laboratorioxyz.com.ZoneControl.model.enums.Role;
import laboratorioxyz.com.ZoneControl.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Status status;
    private boolean requirePasswordChange;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String temporaryPassword;
}
