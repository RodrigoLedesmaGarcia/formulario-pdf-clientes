package com.spring.com.clientes.controller;

import com.spring.com.clientes.entity.Cliente;
import com.spring.com.clientes.services.ClienteServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteServiceImpl service;

    public ClienteController(ClienteServiceImpl service) {
        this.service = service;
    }

    // Listar clientes
    @GetMapping("/listar")
    public ResponseEntity<?> listado(Pageable pageable) {
        Page<Cliente> paginar = service.paginar(pageable);
        if (paginar.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(paginar);
        }
    }

    // buscar cliente por id
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarClienteId (@PathVariable Long id){
        Optional<Cliente> clienteId = service.buscarClientePorId(id);
        if (clienteId.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(clienteId);
        }
    }

    // buscar cliente por correo
    @GetMapping("/correo")
    public ResponseEntity<?> buscarClientePorCorreo (@RequestParam String correo){
        Optional<Cliente> correoCliente = service.buscarPorCorreo(correo);
        if (correoCliente.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(correoCliente);
        }
    }

    // buscar cliente por nombre
    @GetMapping("/nombre")
    public ResponseEntity<?> nombreCliente (@RequestParam String nombre, @RequestParam String app, @RequestParam String apm){
        List<Cliente> nombreCompleto = service.buscarPorNombre(nombre, app, apm);
        if (nombreCompleto.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(nombreCompleto);
        }
    }

    // crear cliente
    @PostMapping("/crear")
    public ResponseEntity<?> crear (@Valid @RequestBody Cliente cliente, BindingResult result){
        if (result.hasErrors()){
            Map<String, Object> errors = new HashMap<>();
            result.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                service.crear(cliente);
                return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
            } catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }
        }
    }

    // editar cliente
    @PostMapping("/editar/{id}")
    public ResponseEntity<?> editar (@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id){
        if (result.hasErrors()){
            Map<String, Object> errors = new HashMap<>();
            result.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        } else {
            Optional<Cliente> opt = service.buscarClientePorId(id);
            if (opt.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            Cliente entity = opt.get();
            entity.setNombre(cliente.getNombre());
            entity.setApp(cliente.getApp());
            entity.setApm(cliente.getApm());
            entity.setCorreo(cliente.getCorreo());
            try {
                Cliente edited = service.editar(entity);
                return ResponseEntity.ok(edited);
            } catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id){
        Optional<Cliente> borrar = service.buscarClientePorId(id);
        if (borrar.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            try {
                service.eliminarCliente(id);
                return ResponseEntity.ok(id);
            } catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }
        }
    }

}