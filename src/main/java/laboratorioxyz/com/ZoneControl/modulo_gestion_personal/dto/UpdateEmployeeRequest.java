package laboratorioxyz.com.ZoneControl.modulo_gestion_personal.dto;

import laboratorioxyz.com.ZoneControl.model.enums.DocumentType;
import laboratorioxyz.com.ZoneControl.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String position;
    private DocumentType documentType;
    private String documentNumber;
    private UUID departmentId;
    private Status status;
}
