package com.example.demo.repositories;

import com.example.demo.entities.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuponRepository extends JpaRepository<Cupon,Integer> {

    @Query(value = "SELECT * FROM cupon where nombre = ?1",
            nativeQuery = true)
    Cupon buscarPorNombre(String nombre);
}
