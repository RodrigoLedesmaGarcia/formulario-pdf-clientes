package com.spring.com.clientes.controller;

import com.spring.com.clientes.entity.Cliente;
import com.spring.com.clientes.services.ClienteServiceImpl;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/clientes/pdf")
public class ClientePdfController {

    private final ClienteServiceImpl service;

    ClientePdfController(ClienteServiceImpl service) {
        this.service = service;
    }

    // Subir PDF
    @PostMapping(path = "/{id}/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirPdf(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        try {
            service.subirPdf(id, file);
            return ResponseEntity.ok("PDF subido exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir PDF: " + e.getMessage());
        }
    }

    // Descargar PDF -
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargar(@PathVariable Long id) {
        try {
            byte[] data = service.descargarPdf(id);
            Optional<Cliente> c = service.buscarClientePorId(id);

            if (c.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String filename = c.get().getPdfNombre() != null ?
                    c.get().getPdfNombre() : "cliente-" + id + ".pdf";

            ContentDisposition cd = ContentDisposition.attachment()
                    .filename(filename)
                    .build();
            headers.setContentDisposition(cd);

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar PDF
    @DeleteMapping("/{id}/pdf")
    public ResponseEntity<?> eliminarPdf(@PathVariable Long id) {
        try {
            service.eliminarPdf(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
