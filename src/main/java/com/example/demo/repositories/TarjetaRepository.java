package com.example.demo.repositories;

import com.example.demo.entities.Tarjeta;
import com.example.demo.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, String> {

    List<Tarjeta> findByUsuario(Usuario usuario);

}
