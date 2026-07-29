package laboratorioxyz.com.ZoneControl.modulo_publico.repository;

import laboratorioxyz.com.ZoneControl.model.entity.Office;
import laboratorioxyz.com.ZoneControl.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfficeRepository extends JpaRepository<Office, UUID> {
    List<Office> findByStatus(Status status);
}
