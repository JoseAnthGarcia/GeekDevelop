package com.example.demo.repositories;

import com.example.demo.dtos.PedidoDTO;
import com.example.demo.entities.Distrito;
import com.example.demo.entities.Pedido;
import com.example.demo.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.print.attribute.standard.MediaSize;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {

    @Query(value = "(select datediff(now(),min(fechapedido)) from pedido) ", nativeQuery = true)
    int fechaPedidoMinimo();

    @Query(value = "select * from pedido p \n" +
            "inner join usuario u on u.idusuario = ?1 and p.idcliente = u.idusuario\n" +
            "inner join usuario r on r.idusuario = p.idrepartidor\n" +
            "inner join restaurante rest on p.idrestaurante = rest.idrestaurante\n" +
            "where concat(lower(rest.nombre),lower(r.nombres)) like %?2% \n" +
            "and concat(p.valoracionrepartidor,p.valoracionrestaurante) like %?3% \n" +
            "and (p.fechapedido >= DATE_ADD(now(), INTERVAL ?4 DAY)) ", nativeQuery = true)
    List<Pedido> pedidosPorCliente(int idCliente, String texto, int valoracion, int fechaPedido);

    @Query(value="select r.nombre, p.fechapedido, p.tiempoentrega, p.estado, p.codigo  from pedido p\n" +
            "            inner join usuario u on u.idusuario = ?1 and p.idcliente = u.idusuario\n" +
            "            inner join restaurante r on p.idrestaurante = r.idrestaurante\n" +
            "            where concat(lower(r.nombre),lower(r.nombre)) like %?2%\n" +
            "            and p.estado >=?3 and  p.estado <=?4 ;", nativeQuery = true)

    List<PedidoDTO> pedidosTotales(int idCliente, String texto, int estado1, int estado2);


    List<Pedido> findByEstadoAndUbicacion_Distrito(int estado, Distrito distrito);

    Pedido findByEstadoAndRepartidor(int estado, Usuario repartidor);

    @Query(value = "select*from pedido where (idrestaurante=?1) order by estado", nativeQuery = true)
    List<Pedido> pedidosXrestaurante (int id);

    @Query(value = "select *from pedido where idrestaurante=?1 and codigo=?2 ", nativeQuery = true)
    Pedido pedidosXrestauranteXcodigo (int idrestaurante, String codigo);

}
