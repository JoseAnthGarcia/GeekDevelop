package com.example.demo.service;

import com.example.demo.dtos.ExtraDTO;
import com.example.demo.entities.Extra;
import com.example.demo.entities.Plato;
import com.example.demo.repositories.ExtraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExtraServiceImpl implements ExtraService{


    @Autowired
    private ExtraRepository extraRepository;


    @Override
    public Page<Extra> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.extraRepository.findByIdrestauranteAndDisponible(1,true, pageable);
        //return this.extraRepository.listarExtra(id);
    }

    @Override
    public Page<Extra> findPaginated2(int pageNo, int pageSize,int idrestaurante, int idcategoria, String nombre, double inputPMin, double inputPMax) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.extraRepository.findByIdrestauranteAndIdcategoriaextraAndDisponibleAndNombreIsContainingAndPreciounitarioGreaterThanEqualAndPreciounitarioLessThanEqual(idrestaurante,idcategoria, true, nombre, pageable, inputPMin, inputPMax);
    }


}
