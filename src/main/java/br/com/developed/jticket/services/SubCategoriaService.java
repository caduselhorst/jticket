/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.SubCategoria;
import br.com.developed.jticket.repositories.SubCategoriaRepository;
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
public class SubCategoriaService {
    
    @Autowired
    private SubCategoriaRepository repository;
    
    public List<SubCategoria> carregaTodos() {
        return repository.findAll();
    }
    
    public List<SubCategoria> carregaPorCategoria(List<Categoria> categorias) {
        List<Long> codcats = categorias
                .stream()
                .map(c -> c.getCodcategoria()).collect(Collectors.toList());
        return repository.findByCodcategoriaIn(codcats.toArray(new Long[]{}));
    }
    
}
