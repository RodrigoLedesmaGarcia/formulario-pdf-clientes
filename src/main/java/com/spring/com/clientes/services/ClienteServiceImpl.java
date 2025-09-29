package com.spring.com.clientes.services;

import com.spring.com.clientes.entity.Cliente;
import com.spring.com.clientes.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    ClienteServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Cliente> paginar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Cliente> buscarClientePorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return repository.findClienteByCorreoIgnoreCase(correo);
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre, String app, String apm) {
        return repository.findUsuarioByFullName(nombre, app, apm);
    }

    @Override
    public Cliente crear(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public Cliente editar(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
          repository.deleteById(id);
    }


    @Transactional
    public void subirPdf(Long id, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Archivo vacío");
        String ct = file.getContentType();
        if (ct == null || !ct.toLowerCase().contains("pdf"))
            throw new IllegalArgumentException("Solo se permiten PDFs");

        Optional<Cliente> c = buscarClientePorId(id);
        if (c.isPresent()) {
            Cliente cliente = c.get();
            cliente.setPdfNombre(file.getOriginalFilename());
            cliente.setPdfTipo(ct);
            cliente.setPdfTamano(file.getSize());
            cliente.setPdfDatos(file.getBytes());
            repository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    @Transactional
    public byte[] descargarPdf(Long id) {
        Optional<Cliente> c = buscarClientePorId(id);
        if (c.isPresent()) {
            Cliente cliente = c.get();
            if (cliente.getPdfDatos() == null) throw new RuntimeException("El cliente no tiene PDF");
            return cliente.getPdfDatos();
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }


    @Transactional
    public void eliminarPdf(Long id) {
        Optional<Cliente> c = buscarClientePorId(id);
        if (c.isPresent()) {
            Cliente cliente = c.get();
            cliente.setPdfDatos(null);
            cliente.setPdfNombre(null);
            cliente.setPdfTipo(null);
            cliente.setPdfTamano(null);
            repository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }
}