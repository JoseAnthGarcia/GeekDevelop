package com.example.demo.service;

import com.example.demo.entities.Pedido;
import com.example.demo.entities.Ubicacion;
import com.example.demo.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Override
    public Page<Pedido> findPaginated(int pageNo, int pageSize, int idrestaurante, String nombre, String direccion, int inputEstadoMin, int inputEstadoMax, double inputPMin, double inputPMax, String fechainicio, String fechafin) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.pedidoRepository.findByRestaurante_IdrestauranteAndCliente_NombresIsContainingAndCliente_DireccionactualIsContainingAndEstadoGreaterThanEqualAndEstadoLessThanEqualAndPreciototalGreaterThanEqualAndPreciototalLessThanEqualAndFechapedidoBetweenOrderByEstadoAsc(idrestaurante,
                nombre, direccion, inputEstadoMin, inputEstadoMax, inputPMin, inputPMax, fechainicio, fechafin, pageable);
    }

    public Page<Pedido> pedidosPaginacion(int numeroPag, int tamPag, HttpSession session){
        Pageable pageable = PageRequest.of(numeroPag - 1, tamPag);
        Ubicacion ubicacionActual = (Ubicacion) session.getAttribute("ubicacionActual");

        return pedidoRepository.findByEstadoAndUbicacion_DistritoOrderByFechapedidoAsc(4, ubicacionActual.getDistrito(), pageable);
    }
}
