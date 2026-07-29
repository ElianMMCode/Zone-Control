package laboratorioxyz.com.ZoneControl.modulo_administracion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentSectionRequest {
    private Map<String, String> content;
}
