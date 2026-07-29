package laboratorioxyz.com.ZoneControl.modulo_publico.repository;

import laboratorioxyz.com.ZoneControl.model.enums.Status;
import laboratorioxyz.com.ZoneControl.modulo_publico.model.ProductCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalog, UUID> {
    List<ProductCatalog> findByStatus(Status status);
}
