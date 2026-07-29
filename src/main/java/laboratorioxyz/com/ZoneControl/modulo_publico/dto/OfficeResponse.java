package laboratorioxyz.com.ZoneControl.modulo_publico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class OfficeResponse {
    private UUID id;
    private String name;
    private String address;
    private String openingHours;
    private Double latitude;
    private Double longitude;
}
