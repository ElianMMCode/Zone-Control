package laboratorioxyz.com.ZoneControl.modulo_administracion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogRequest {

    @NotBlank
    @Size(max = 80)
    private String name;

    private String description;

    @Size(max = 40)
    private String activeIngredient;

    @Size(max = 40)
    private String presentation;

    @Size(max = 30)
    private String productionArea;
}
