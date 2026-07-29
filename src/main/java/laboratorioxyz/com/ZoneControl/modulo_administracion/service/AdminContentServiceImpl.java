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
import laboratorioxyz.com.ZoneControl.modulo_publico.model.PublicContent;
import laboratorioxyz.com.ZoneControl.modulo_publico.repository.OfficeRepository;
import laboratorioxyz.com.ZoneControl.modulo_publico.repository.ProductCatalogRepository;
import laboratorioxyz.com.ZoneControl.modulo_publico.repository.PublicContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminContentServiceImpl implements AdminContentService {

    private final PublicContentRepository publicContentRepository;
    private final OfficeRepository officeRepository;
    private final ProductCatalogRepository productCatalogRepository;

    @Value("${app.brochure.path:uploads/folleto}")
    private String brochurePath;

    @Override
    @Transactional
    @CacheEvict(value = {"institutional", "contact"}, allEntries = true)
    public Map<String, String> updateContentSection(ContentSection section, ContentSectionRequest request) {
        publicContentRepository.deleteBySection(section);
        List<PublicContent> contents = request.getContent().entrySet().stream()
                .map(entry -> PublicContent.builder()
                        .section(section)
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .build())
                .collect(Collectors.toList());
        publicContentRepository.saveAll(contents);
        log.info("Content section {} updated with {} entries", section, contents.size());
        return request.getContent();
    }

    @Override
    public Map<String, String> getContentSection(ContentSection section) {
        return publicContentRepository.findBySection(section).stream()
                .collect(Collectors.toMap(PublicContent::getKey, PublicContent::getValue));
    }

    @Override
    @Transactional
    @CacheEvict(value = "offices", allEntries = true)
    public OfficeResponse createOffice(OfficeRequest request) {
        Office office = Office.builder()
                .name(request.getName())
                .address(request.getAddress())
                .openingHours(request.getOpeningHours())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .status(Status.ACTIVO)
                .build();
        office = officeRepository.save(office);
        return toOfficeResponse(office);
    }

    @Override
    @Transactional
    @CacheEvict(value = "offices", allEntries = true)
    public OfficeResponse updateOffice(UUID id, OfficeRequest request) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sede no encontrada"));
        office.setName(request.getName());
        office.setAddress(request.getAddress());
        office.setOpeningHours(request.getOpeningHours());
        office.setLatitude(request.getLatitude());
        office.setLongitude(request.getLongitude());
        office = officeRepository.save(office);
        return toOfficeResponse(office);
    }

    @Override
    @Transactional
    @CacheEvict(value = "offices", allEntries = true)
    public void updateOfficeStatus(UUID id, Status status) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sede no encontrada"));
        office.setStatus(status);
        officeRepository.save(office);
        log.info("Office {} status changed to {}", id, status);
    }

    @Override
    public List<OfficeResponse> listOffices() {
        return officeRepository.findAll().stream()
                .map(this::toOfficeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "catalog", allEntries = true)
    public CatalogResponse createProduct(CatalogRequest request) {
        ProductCatalog product = ProductCatalog.builder()
                .name(request.getName())
                .description(request.getDescription())
                .activeIngredient(request.getActiveIngredient())
                .presentation(request.getPresentation())
                .productionArea(request.getProductionArea())
                .status(Status.ACTIVO)
                .build();
        product = productCatalogRepository.save(product);
        return toCatalogResponse(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = "catalog", allEntries = true)
    public CatalogResponse updateProduct(UUID id, CatalogRequest request) {
        ProductCatalog product = productCatalogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setActiveIngredient(request.getActiveIngredient());
        product.setPresentation(request.getPresentation());
        product.setProductionArea(request.getProductionArea());
        product = productCatalogRepository.save(product);
        return toCatalogResponse(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = "catalog", allEntries = true)
    public void updateProductStatus(UUID id, Status status) {
        ProductCatalog product = productCatalogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado"));
        product.setStatus(status);
        productCatalogRepository.save(product);
        log.info("Product {} status changed to {}", id, status);
    }

    @Override
    public List<CatalogResponse> listProducts() {
        return productCatalogRepository.findAll().stream()
                .map(this::toCatalogResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"institutional", "contact", "offices", "catalog"}, allEntries = true)
    public void uploadBrochure(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Formato no permitido. Solo se aceptan archivos PDF");
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El archivo excede el tamaño máximo permitido de 10MB");
        }
        try {
            File dir = new File(brochurePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Path targetPath = new File(dir, "Folleto_Laboratorio_XYZ.pdf").toPath();
            Files.write(targetPath, file.getBytes());
            log.info("Brochure uploaded: {} bytes", file.getSize());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al guardar el archivo");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"institutional", "contact", "offices", "catalog"}, allEntries = true)
    public void deleteBrochure() {
        File file = new File(brochurePath, "Folleto_Laboratorio_XYZ.pdf");
        if (file.exists() && file.delete()) {
            log.info("Brochure deleted");
        }
    }

    @Override
    public boolean hasBrochure() {
        return new File(brochurePath, "Folleto_Laboratorio_XYZ.pdf").exists();
    }

    private OfficeResponse toOfficeResponse(Office office) {
        return OfficeResponse.builder()
                .id(office.getId())
                .name(office.getName())
                .address(office.getAddress())
                .openingHours(office.getOpeningHours())
                .latitude(office.getLatitude())
                .longitude(office.getLongitude())
                .build();
    }

    private CatalogResponse toCatalogResponse(ProductCatalog product) {
        return CatalogResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .activeIngredient(product.getActiveIngredient())
                .presentation(product.getPresentation())
                .productionArea(product.getProductionArea())
                .build();
    }
}
