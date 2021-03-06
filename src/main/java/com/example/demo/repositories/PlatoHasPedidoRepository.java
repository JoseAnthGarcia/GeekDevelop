package com.example.demo.repositories;

import com.example.demo.entities.Pedido;
import com.example.demo.entities.Plato_has_pedido;
import com.example.demo.entities.Plato_has_pedidoKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatoHasPedidoRepository extends JpaRepository<Plato_has_pedido, Plato_has_pedidoKey> {
    List<Plato_has_pedido> findByPedido(Pedido pedido);
}
