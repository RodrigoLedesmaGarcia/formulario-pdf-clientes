package com.spring.com.clientes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "este campo no puede estar vacio")
    private String nombre;

    @NotBlank(message = "este campo no puede estar vacio")
    private String apm;

    @NotBlank(message = "este campo no puede estar vacio")
    private String app;

    @Email(message = "ingrese un correo valido")
    @NotBlank(message = "este campo no puede estar vacio")
    private String correo;

    private Date createAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = new Date();
    }

    // ---- PDF único por cliente ----
    private String pdfNombre;
    private String pdfTipo;
    private Long   pdfTamano;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pdfDatos;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApm() {
        return apm;
    }

    public void setApm(String apm) {
        this.apm = apm;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPdfNombre() {
        return pdfNombre;
    }

    public void setPdfNombre(String pdfNombre) {
        this.pdfNombre = pdfNombre;
    }

    public String getPdfTipo() {
        return pdfTipo;
    }

    public void setPdfTipo(String pdfTipo) {
        this.pdfTipo = pdfTipo;
    }

    public Long getPdfTamano() {
        return pdfTamano;
    }

    public void setPdfTamano(Long pdfTamano) {
        this.pdfTamano = pdfTamano;
    }

    public byte[] getPdfDatos() {
        return pdfDatos;
    }

    public void setPdfDatos(byte[] pdfDatos) {
        this.pdfDatos = pdfDatos;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
