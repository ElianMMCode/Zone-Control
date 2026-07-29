package laboratorioxyz.com.ZoneControl.modulo_administracion.controller;

import jakarta.validation.Valid;
import laboratorioxyz.com.ZoneControl.model.enums.ContentSection;
import laboratorioxyz.com.ZoneControl.model.enums.Status;
import laboratorioxyz.com.ZoneControl.modulo_administracion.dto.CatalogRequest;
import laboratorioxyz.com.ZoneControl.modulo_administracion.dto.ContentSectionRequest;
import laboratorioxyz.com.ZoneControl.modulo_administracion.dto.OfficeRequest;
import laboratorioxyz.com.ZoneControl.modulo_administracion.service.AdminContentService;
import laboratorioxyz.com.ZoneControl.modulo_publico.dto.CatalogResponse;
import laboratorioxyz.com.ZoneControl.modulo_publico.dto.OfficeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin/contenido-publico")
@RequiredArgsConstructor
public class AdminContentController {

    private final AdminContentService adminContentService;

    @GetMapping("/institucional")
    public ResponseEntity<Map<String, String>> getInstitutional() {
        return ResponseEntity.ok(adminContentService.getContentSection(ContentSection.INSTITUTIONAL));
    }

    @PutMapping("/institucional")
    public ResponseEntity<Map<String, String>> updateInstitutional(
            @RequestBody ContentSectionRequest request) {
        return ResponseEntity.ok(adminContentService.updateContentSection(
                ContentSection.INSTITUTIONAL, request));
    }

    @GetMapping("/contacto")
    public ResponseEntity<Map<String, String>> getContact() {
        return ResponseEntity.ok(adminContentService.getContentSection(ContentSection.CONTACT));
    }

    @PutMapping("/contacto")
    public ResponseEntity<Map<String, String>> updateContact(
            @RequestBody ContentSectionRequest request) {
        return ResponseEntity.ok(adminContentService.updateContentSection(
                ContentSection.CONTACT, request));
    }

    @GetMapping("/sedes")
    public ResponseEntity<List<OfficeResponse>> listOffices() {
        return ResponseEntity.ok(adminContentService.listOffices());
    }

    @PostMapping("/sedes")
    public ResponseEntity<OfficeResponse> createOffice(
            @Valid @RequestBody OfficeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminContentService.createOffice(request));
    }

    @PutMapping("/sedes/{id}")
    public ResponseEntity<OfficeResponse> updateOffice(
            @PathVariable UUID id, @Valid @RequestBody OfficeRequest request) {
        return ResponseEntity.ok(adminContentService.updateOffice(id, request));
    }

    @PatchMapping("/sedes/{id}/status")
    public ResponseEntity<Void> updateOfficeStatus(
            @PathVariable UUID id, @RequestBody Map<String, Status> body) {
        adminContentService.updateOfficeStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/catalogo")
    public ResponseEntity<List<CatalogResponse>> listProducts() {
        return ResponseEntity.ok(adminContentService.listProducts());
    }

    @PostMapping("/catalogo")
    public ResponseEntity<CatalogResponse> createProduct(
            @Valid @RequestBody CatalogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminContentService.createProduct(request));
    }

    @PutMapping("/catalogo/{id}")
    public ResponseEntity<CatalogResponse> updateProduct(
            @PathVariable UUID id, @Valid @RequestBody CatalogRequest request) {
        return ResponseEntity.ok(adminContentService.updateProduct(id, request));
    }

    @PatchMapping("/catalogo/{id}/status")
    public ResponseEntity<Void> updateProductStatus(
            @PathVariable UUID id, @RequestBody Map<String, Status> body) {
        adminContentService.updateProductStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/folleto")
    public ResponseEntity<Void> uploadBrochure(@RequestParam("file") MultipartFile file) {
        adminContentService.uploadBrochure(file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/folleto")
    public ResponseEntity<Void> deleteBrochure() {
        adminContentService.deleteBrochure();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/folleto/info")
    public ResponseEntity<Map<String, Boolean>> getBrochureInfo() {
        return ResponseEntity.ok(Map.of("exists", adminContentService.hasBrochure()));
    }
}
