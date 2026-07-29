package laboratorioxyz.com.ZoneControl.modulo_autenticacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import laboratorioxyz.com.ZoneControl.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @Size(min = 2, max = 35)
    private String firstName;

    @Size(min = 2, max = 35)
    private String lastName;

    @Email
    @Size(max = 100)
    private String email;

    private Role role;
}
