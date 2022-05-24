/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.Secao;
import br.com.developed.jticket.repositories.CategoriaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository repository;
    
    public List<Categoria> carregaTodas() {
        return repository.findAll();
    }
    
    public List<Categoria> carregaPelasSecoes(List<Secao> secoes) {        
        List<Long> codsecs = secoes
                .stream()
                .map(s -> s.getCodsec()).collect(Collectors.toList());
        return repository.findByCodsecIn(codsecs.toArray(new Long[]{}));
        
    }
    
}
