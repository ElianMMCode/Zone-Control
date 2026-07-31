package laboratorioxyz.com.ZoneControl.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * Áreas restringidas de producción a las que se controla el acceso.
 * Ej: Sala Blanca A, Sala Blanca B, Laboratorio QC, etc.
 * 
 * El campo active permite desactivar un área en lugar de eliminarla
 * (DELETE duro rompería las FK de AccessPermission y AccessHistory).
 * Las áreas inactivas no aparecen en el selector de permisos ni en la
 * información institucional pública.
 */
@Entity
@Table(name = "production_areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionArea {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(max = 30)
    @Column(length = 30, unique = true, nullable = false)
    private String name;

    @Size(max = 200)
    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
