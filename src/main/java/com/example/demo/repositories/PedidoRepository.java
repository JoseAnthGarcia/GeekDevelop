package com.example.demo.repositories;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


    @Query(value="select * from (select r.nombre as `nombre`, r.idrestaurante as 'idrestaurante',p.fechapedido as 'fechapedido',\n" +
            "p.tiempoentrega as 'tiempoentrega', p.estado as 'estado', p.codigo as 'codigo' ,r.foto as 'foto', \n" +
            "p.idcliente as 'idcliente' from pedido p \n" +
            "inner join restaurante r on p.idrestaurante = r.idrestaurante\n" +
            "where p.idcliente=?1 and (p.estado=1 || p.estado=3 || p.estado=4 || p.estado=5) )\n" +
            " as pedidosT\n" +
            "having lower(`nombre`) like %?2% and (estado > ?3 and estado <=?4) ", nativeQuery = true, countQuery = "select count(*) from pedido p \n" +
            "inner join restaurante r on p.idrestaurante = r.idrestaurante\n" +
            "where p.idcliente= ?1 and (p.estado=1 || p.estado=3 || p.estado=4 || p.estado=5) ")
    Page<PedidoDTO> pedidosTotales(int idCliente, String texto, int estado1, int estado2, Pageable pageable);

    @Query(value="select * from (select r.nombre as `nombre`, r.idrestaurante as 'idrestaurante',p.fechapedido as 'fechapedido',\n" +
            "p.tiempoentrega as 'tiempoentrega', p.estado as 'estado', p.codigo as 'codigo' ,r.foto as 'foto', \n" +
            "p.idcliente as 'idcliente' , p.valoracionrestaurante as `valoracionrestaurante` , \n" +
            " p.comentariorestaurante as `comentariorestaurante` , \n" +
            " p.valoracionrepartidor as  `valoracionrepartidor`,\n" +
            " p.comentariorepartidor as `comentariorepartidor` from pedido p \n" +
            "inner join restaurante r on p.idrestaurante = r.idrestaurante\n" +
            "where p.idcliente=?1 and (p.estado=2 || p.estado=6 ) )\n" +
            " as pedidosT\n" +
            "having lower(`nombre`) like %?2% and (estado > ?3 and estado <=?4) ", nativeQuery = true, countQuery = "select count(*) from pedido p \n" +
            "inner join restaurante r on p.idrestaurante = r.idrestaurante\n" +
            "where p.idcliente= ?1 and (p.estado=2 || p.estado=6 ) ")

    Page<PedidoValoracionDTO> pedidosTotales2(int idCliente, String texto, int estado1, int estado2,Pageable pageable);


    Page<Pedido> findByEstadoAndUbicacion_DistritoOrderByFechapedidoAsc(int estado, Distrito distrito, Pageable pageable);

    Pedido findByEstadoAndRepartidor(int estado, Usuario repartidor);


    Page<Pedido> findByRestaurante_IdrestauranteAndCliente_NombresIsContainingAndEstadoGreaterThanEqualAndEstadoLessThanEqualAndPreciototalGreaterThanEqualAndPreciototalLessThanEqualAndFechapedidoBetween(int idrestaurante, String nombre, int inputEstadoMin, int inputEstadoMax, double inputPMin, double inputPMax, String fechainicio, String fechafin, Pageable pageable);

    @Query(value = "select *from pedido where idrestaurante=?1 and codigo=?2 ", nativeQuery = true)
    Pedido pedidosXrestauranteXcodigo (int idrestaurante, String codigo);

    @Query(value = "SELECT pe.codigo, concat(u.nombres,' ',u.apellidos) as cliente, concat(ubi.direccion,'-',dis.nombre) as direccion, pe.fechapedido, cu.nombre as cupon,\n" +
            "cu.descuento as descuento, pe.estado as estado, pago.tipo as metodopago, pe.comentariorestaurante as comentario,\n" +
            " pe.preciototal FROM pedido pe\n" +
            "inner join usuario u on pe.idcliente = u.idusuario \n" +
            "left join cupon cu on pe.idcupon=cu.idcupon \n" +
            "inner join metodopago pago on pe.idmetodopago=pago.idmetodopago\n" +
            "inner join restaurante res on res.idrestaurante=pe.idrestaurante\n" +
            "inner join ubicacion ubi on pe.idubicacion=ubi.idubicacion\n" +
            "inner join distrito dis on dis.iddistrito=ubi.iddistrito\n" +
            "where res.idrestaurante=?1 and pe.codigo=?2",nativeQuery = true)
    List<DetallePedidoDTO> detallePedido(int idrestaurante, String codigopedido);

    @Query(value = "SELECT p.nombre,php.preciounitario, php.cantidad, php.preciounitario*php.cantidad as preciototal FROM plato_has_pedido php\n" +
            "inner join pedido pe on pe.codigo=php.codigo\n" +
            "inner join restaurante res on res.idrestaurante=pe.idrestaurante\n" +
            "inner join plato p on p.idplato=php.idplato\n" +
            "where pe.idrestaurante=?1 and php.codigo=?2",nativeQuery = true)
    List<PlatoPorPedidoDTO> platosPorPedido(int idrestaurante, String codigopedido);

    @Query(value = "SELECT e.nombre, ehp.preciounitario,ehp.cantidad,ehp.preciounitario*ehp.cantidad as preciototal  FROM extra_has_pedido ehp\n" +
            "inner join extra e on e.idextra=ehp.idextra\n" +
            "inner join pedido pe on pe.codigo=ehp.codigo\n" +
            "where ehp.codigo=?1",nativeQuery = true)
    List<ExtraPorPedidoDTO> extrasPorPedido(String codigopedido);
    @Query(value="SELECT php.codigo,pl.idplato,pl.nombre as 'nombreplato' , php.cantidad, php.preciounitario\n" +
            ", php.observacionplatillo\n" +
            "  FROM geekdevelop.plato_has_pedido  php\n" +
            "inner join plato pl on pl.idplato= php.idplato\n" +
            "where php.codigo = ?1", nativeQuery = true)
    Page<Plato_has_PedidoDTO> detalle2(String codigo, Pageable pageable);


    @Query(value ="select DISTINCT(ped.codigo), r.nombre as 'nombrerest',\n" +
            "clhp.utilizado ,ped.preciototal,ped.mismodistrito,ped.estado,ped.idmetodopago,\n" +
            "ped.fechapedido, ped.tiempoentrega , c.nombre as 'nombrecupon', c.descuento \n" +
            "from pedido ped \n" +
            "left join restaurante r on ped.idrestaurante=r.idrestaurante\n" +
            "left join cliente_has_cupon clhp on ped.idcupon = clhp.idcupon\n" +
            "left join cupon c on ped.idcupon = c.idcupon\n"+
            "where ped.codigo = ?1", nativeQuery = true)

    List<Pedido1DTO> detalle1(String codigo);

    @Query(value="select * from pedido where codigo=?1",nativeQuery = true)
    Pedido encontrarporId(String id);

    @Query(value="SELECT codigo FROM pedido where idrestaurante=?1 and estado=?2 ", nativeQuery = true)
    List<String> listarPedidosXestadoXrestaurante(int codigo, int estado);


    @Query(value="select distinct php.codigo as codigo, date_format(p.fechapedido,'%Y-%m-%d')  as fecha, p.preciototal as preciototal from plato_has_pedido php\n" +

            "\t\tinner join pedido p on php.codigo=p.codigo\n" +
            "\t\twhere p.idrestaurante = ?1 and p.estado = ?2 and (date_format(p.fechapedido,'%Y-%m-%d') between ?3 and ?4) \n" +
            "and php.codigo like %?5% and p.preciototal >= ?6 and p.preciototal <= ?7", countQuery = "select distinct count(*) from plato_has_pedido php \n" +
            "            inner join pedido p on php.codigo=p.codigo \n" +
            "            where p.idrestaurante = ?1 and p.estado = ?2 and (date_format(p.fechapedido,'%Y-%m-%d') between ?3 and ?4) \n" +
            "            and php.codigo like %?5% and p.preciototal >= ?6 and p.preciototal <= ?7", nativeQuery = true)
    Page<PedidoReporteDTO> pedidoReporte(int idrestaurante, int estado, String fechainicio, String fechafin, String codigo, double inputPrecioMin, double inputPrecioMax, Pageable pageable);

    @Query(value ="select r.nombre , count(p.idrestaurante) as 'cantidad'\n" +
            ",EXTRACT(MONTH from p.fechapedido) as 'mes' , round(avg(p.tiempoentrega)) as \"tiempoentrega\"\n" +
            "from pedido p \n" +
            "left join restaurante r on p.idrestaurante=r.idrestaurante \n" +
            "where p.idcliente= ?1 and ((EXTRACT(MONTH from p.fechapedido))> ?2 and EXTRACT(MONTH from p.fechapedido) <= ?3 )\n" +
            "and lower(r.nombre) like %?4% \n" +
            " group by p.idrestaurante having count(r.idrestaurante) like %?5%" ,nativeQuery = true)

    Page<ReportePedidoCDTO> reportetiempo(int idcliente, int limit1mes, int limit2mes,String texto,String limitcant,Pageable pageable );

    @Query(value = "select  r.nombre as \"nombrerest\"\n" +
            "            ,EXTRACT(MONTH from p.fechapedido) as \"mes\" ,p.fechapedido\n" +
            "            ,c.nombre as \"nombrecupon\", c.descuento\n" +
            "\t\tfrom pedido p \n" +
            "            inner join restaurante r on p.idrestaurante=r.idrestaurante \n" +
            "            left join cliente_has_cupon clhp on p.idcupon = clhp.idcupon\n" +
            "            inner join cupon c on c.idcupon = clhp.idcupon\n" +
            "            where p.idcliente=?1 and clhp.utilizado=1 and\n" +
            "              (EXTRACT(MONTH from p.fechapedido) > ?2  and  EXTRACT(MONTH from p.fechapedido)<=?3)\n" +
            "              and lower(r.nombre) like %?4% and lower(c.nombre) like %?5%\n" +
            "\t\tgroup by r.idrestaurante\n" , nativeQuery = true)

    Page<ReporteDineroDTO> reportedinero(int idcliente, int limit1mes, int limit2mes,String nombre,String nombrec,Pageable pageable );


    @Query(value="select pe.codigo as 'codigo', pe.valoracionrestaurante as 'valoracion', date_format(pe.fechapedido,'%Y-%m-%d') as 'fecha', pe.comentariorestaurante as 'comentario' \n" +
            "from pedido pe\n" +
            "where pe.idrestaurante=?1 and pe.estado=?2 and pe.valoracionrestaurante like %?3% and date_format(pe.fechapedido,'%Y-%m-%d') between ?4 and ?5", countQuery = "select count(*) from pedido pe \n" +
            "where pe.idrestaurante=?1 and pe.estado=?2 and pe.valoracionrestaurante like %?3% and date_format(pe.fechapedido,'%Y-%m-%d') between ?4 and ?5", nativeQuery = true)
    Page<ValoracionReporteDTO> valoracionReporte(int id, int estado, String valoracion, String fechainicio, String fechafin, Pageable pageable);

    @Query(value="select * from (select pl.idplato as id,pl.nombre as nombre, c.nombre as nombrecat, sum(php.cantidad) as suma from plato_has_pedido php \n" +
            "inner join pedido p on php.codigo=p.codigo\n" +
            "inner join plato pl on pl.idplato=php.idplato \n" +
            "inner join categoriarestaurante c on pl.idcategoriarestaurante=c.idcategoria \n" +
            "where p.idrestaurante=?1 and p.estado=?2 and (pl.nombre like %?3%) and (c.idcategoria like %?4%)\n" +
            "group by php.idplato) as T2 having suma >= ?5 and suma <=?6", nativeQuery = true)
    Page<PlatoReporteDTO> reportePlato(int id, int estado, String nombre, String idcategoria, int cantMin, int cantMax, Pageable pageable);

    @Query(value ="select r.nombre as 'nombrerest' , count(r.idrestaurante) as \"numpedidos\"\n" +
            ",EXTRACT(MONTH from p.fechapedido) as 'mes' , sum(p.preciototal) as'total'\n" +
            "from pedido p \n" +
            "inner join restaurante r on p.idrestaurante=r.idrestaurante \n" +
            "where p.idcliente= ?1 and (EXTRACT(MONTH from p.fechapedido) >?2 and EXTRACT(MONTH from p.fechapedido) <=?3 )\n" +
            "and lower(r.nombre) like %?4%\n" +
            " group by p.idrestaurante having count(r.idrestaurante) like %?5%",nativeQuery = true)

    Page<ReportePedido> reportexmes(int idcliente, int limit1mes, int limit2mes,String texto,String limitcant,Pageable pageable);





    @Query(value = "select pe.codigo,dis.nombre as lugar, date_format(pe.fechapedido, '%H:%i') as hora, u.nombres as cliente  from pedido pe\n" +
            "    inner join ubicacion ubi on pe.idubicacion=ubi.idubicacion\n" +
            "    inner join usuario u on pe.idcliente = u.idusuario\n" +
            "    inner join distrito dis on dis.iddistrito=ubi.iddistrito\n" +
            "where pe.estado=0 and pe.idrestaurante=?1 and date_format(pe.fechapedido,'%d-%m-%y')=date_format(now(),'%d-%m-%y') ORDER BY hora ASC limit ?2",nativeQuery = true)
    List<NotifiRestDTO> notificacionPeidosRestaurante(int idRestaurante, int cantidad);

    @Query(value="select r.nombre as 'nombrerest' , count(p.idrestaurante) as `numpedidos` ,\n" +
            "EXTRACT(MONTH from p.fechapedido) as `mes` from pedido p \n" +
            "inner join restaurante r on p.idrestaurante=r.idrestaurante \n" +
            "where p.idcliente=?1 and EXTRACT(MONTH from p.fechapedido) = ?2  group by p.idrestaurante limit 1,3",nativeQuery = true)
    List<ReporteTop3> reporteTop3Rest(int idcliente, int mes);

    @Query(value="select  pl.nombre ,sum(cantidad) as 'totalplato' from pedido p \n" +
            "inner join plato_has_pedido php on p.codigo=php.codigo\n" +
            "inner join plato pl on php.idplato=pl.idplato\n" +
            "where  p.idcliente=?1 and EXTRACT(MONTH from p.fechapedido) = ?2  \n" +
            "group by pl.idplato order by sum(cantidad) desc  limit 0,3 ", nativeQuery = true)

    List<ReporteTop3P> reporteTop3Pl(int idcliente, int mes);

    @Query(value = "select m.nombre, `cantmd`, `cantdd` from mes m\n" +
            "left join (select month(fechapedido) as `mes`, count(codigo) as `cantmd`\n" +
            "from pedido where year(fechapedido)=?1 and mismodistrito=1 and estado=6 and idrepartidor=?2 group by month(fechapedido)) md\n" +
            "on md.`mes`=m.idmes\n" +
            "left join (select month(fechapedido) as `mes`, count(codigo) as `cantdd`\n" +
            "from pedido where year(fechapedido)=?1 and mismodistrito=0 and estado=6 and idrepartidor=?2 group by month(fechapedido)) dd\n" +
            "on dd.`mes`=m.idmes", nativeQuery = true)
    List<ReporteIngresosDTO> reporteIngresos(int anio, int idRepartidor);

    @Query(value = "select year(max(fechapedido)) from pedido", nativeQuery = true)
    int hallarMaxAnioPedido();

    @Query(value = "select year(min(fechapedido)) from pedido", nativeQuery = true)
    int hallarMinAnioPedido();

    Pedido findByCodigo(String codigo);

    @Query(value="select c.idcupon, c.nombre as 'nombrecupon',\n" +
            "c.descuento,c.fechafin, c.politica,r.nombre as 'nombrerestaurante', \n" +
            "chp.utilizado, cl.nombres as 'nombrescliente', cl.idusuario as 'idcliente'\n" +
            "from cupon c\n" +
            "left join restaurante r on r.idrestaurante=c.idrestaurante\n" +
            "left join cliente_has_cupon chp on chp.idcupon = c.idcupon\n" +
            "left join usuario cl on cl.idusuario = chp.idcliente\n" +
            "where c.estado=2 and (chp.utilizado is null  || chp.utilizado = 0) \n" +
            "and (cl.idusuario is null || cl.idusuario = ?1) and \n" +
            "r.nombre like %?2%  and (c.descuento >=?3 and c.descuento<= ?4)",nativeQuery = true)
    Page<CuponClienteDTO> listaCupones(int idCliente,String texto, int limitInf, int limitSup, Pageable pageable);




}
