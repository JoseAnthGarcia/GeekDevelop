package com.example.demo.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idusuario;

    @Column(unique = true)
    @NotBlank(message = "Complete sus datos")
    @Pattern(regexp = "[0-9]{8}",message = "Ingrese 8 dígitos")
    private String dni;

    @Column(nullable = false)
    @NotBlank(message = "Complete sus datos")
    @NotNull
    @Pattern(regexp = "[/^[A-Za-záéíñóúüÁÉÍÑÓÚÜ_.\\s]+$/g]{2,254}",message = "Solo puede ingresar letras")
    private String nombres;

    @Column(nullable = false)
    @NotBlank(message = "Complete sus datos")
    @Pattern(regexp = "[/^[A-Za-záéíñóúüÁÉÍÑÓÚÜ_.\\s]+$/g]{2,254}",message = "Solo puede ingresar letras")
    private String apellidos;

    @Column(nullable = false)
    @NotBlank(message = "Complete sus datos")
    @Pattern(regexp = "[a-zA-Z ]{2,254}",message = "Seleccione una de las opciones")
    private String sexo;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Complete sus datos")
    @Pattern(regexp = "[0-9]{9}",message = "Ingrese 9 dígitos")
    private String telefono;

    private int estado;

    @Column(nullable = false)
    private String fecharegistro;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "Ingrese una fecha")
    @Column(nullable = false)
    private String fechanacimiento;


    private byte[] foto;
    private String fotonombre;
    private String fotocontenttype;

    public void setFotonombre(String fotonombre) {
        this.fotonombre = fotonombre;
    }

    public String getFotocontenttype() {
        return fotocontenttype;
    }

    public void setFotocontenttype(String fotocontenttype) {
        this.fotocontenttype = fotocontenttype;
    }

    private String fechaadmitido;

    private String ultimoingreso;

    //@NotBlank(message = "ingrese su dirección")
    //TODO: (melanie) una mejor validacion pls
    //@Pattern(regexp = "[a-zA-Z ]{2,254}",message = "Solo puede ingresar letras")
    @Column(name = "direccionactual")
    private String direccionactual;

    @ManyToOne
    @JoinColumn(name = "idrol", nullable = false)
    private Rol rol;

    //---credenciales-----
    @Column(nullable = false)
    @NotBlank(message = "Complete sus datos")
    @Pattern(regexp = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$",message = "Debe tener el formato nombre@correo.com")
    private String correo;

    @Column(nullable = false)
    @NotBlank(message = "Complete su contraseña")
    @Size(min = 8, message = "Mínimo 8 caracteres")
    //@Pattern(regexp = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$",message = "Ingrese una contraseña válida.")
    private String contrasenia;
    //---------------------

    @OneToOne
    @JoinColumn(name = "idmovilidad")
    private Movilidad movilidad;

    @OneToOne(mappedBy = "administrador")
    private Restaurante restaurante;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> listaPedidosPorUsuario;

    @OneToMany(mappedBy = "repartidor")
    private List<Pedido> listaPedidosPorRepartidor;



    public String getDireccionactual() {
        return direccionactual;
    }

    public void setDireccionactual(String direccionactual) {
        this.direccionactual = direccionactual;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }


    public List<Pedido> getListaPedidosPorUsuario() { return listaPedidosPorUsuario; }

    public void setListaPedidosPorUsuario(List<Pedido> listaPedidosPorUsuario) { this.listaPedidosPorUsuario = listaPedidosPorUsuario; }

    public List<Pedido> getListaPedidosPorRepartidor() {
        return listaPedidosPorRepartidor;
    }

    public void setListaPedidosPorRepartidor(List<Pedido> listaPedidosPorRepartidor) {
        this.listaPedidosPorRepartidor = listaPedidosPorRepartidor;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public String getFechaadmitido() {
        return fechaadmitido;
    }

    public void setFechaadmitido(String fechaadmitido) {
        this.fechaadmitido = fechaadmitido;
    }

    public String getUltimoingreso() {
        return ultimoingreso;
    }

    public void setUltimoingreso(String ultimoingreso) {
        this.ultimoingreso = ultimoingreso;
    }

    public Movilidad getMovilidad() {
        return movilidad;
    }

    public void setMovilidad(Movilidad movilidad) {
        this.movilidad = movilidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getFotonombre() {
        return fotonombre;
    }


}