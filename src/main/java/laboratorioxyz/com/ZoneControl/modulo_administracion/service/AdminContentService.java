package laboratorioxyz.com.ZoneControl.modulo_administracion.service;

import laboratorioxyz.com.ZoneControl.model.entity.Office;
import laboratorioxyz.com.ZoneControl.model.enums.ContentSection;
import laboratorioxyz.com.ZoneControl.model.enums.Status;
import laboratorioxyz.com.ZoneControl.modulo_administracion.dto.CatalogRequest;
import laboratorioxyz.com.ZoneControl.modulo_administracion.dto.ContentSectionRequest;
import laboratorioxyz.com.ZoneControl.modulo_administracion.dto.OfficeRequest;
import laboratorioxyz.com.ZoneControl.modulo_publico.dto.CatalogResponse;
import laboratorioxyz.com.ZoneControl.modulo_publico.dto.OfficeResponse;
import laboratorioxyz.com.ZoneControl.modulo_publico.model.ProductCatalog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AdminContentService {
    Map<String, String> updateContentSection(ContentSection section, ContentSectionRequest request);
    Map<String, String> getContentSection(ContentSection section);

    OfficeResponse createOffice(OfficeRequest request);
    OfficeResponse updateOffice(UUID id, OfficeRequest request);
    void updateOfficeStatus(UUID id, Status status);
    List<OfficeResponse> listOffices();

    CatalogResponse createProduct(CatalogRequest request);
    CatalogResponse updateProduct(UUID id, CatalogRequest request);
    void updateProductStatus(UUID id, Status status);
    List<CatalogResponse> listProducts();

    void uploadBrochure(MultipartFile file);
    void deleteBrochure();
    boolean hasBrochure();
}
