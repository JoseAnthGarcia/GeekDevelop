package com.example.demo.service;

import com.example.demo.dtos.RestauranteDTO;
import com.example.demo.repositories.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RestauranteClienteServiceImpl implements RestauranteClienteService{

    @Autowired
    RestauranteRepository restauranteRepository;

    @Override
    public Page<RestauranteDTO> listaRestaurantePaginada(String texto, Integer limitInfP, Integer limitSupP,Integer limitInfVal, Integer limitSupVal, String id1,String id2, String id3,Integer iddistrito, Pageable pageable) {
        return restauranteRepository.listaRestaurante(texto,limitInfP,limitSupP,limitInfVal,limitSupVal,id1,id2,id3,iddistrito, pageable);
    }
}
