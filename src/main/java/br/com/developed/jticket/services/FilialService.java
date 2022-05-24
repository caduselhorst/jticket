/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Filial;
import br.com.developed.jticket.repositories.FilialRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class FilialService {
    
    @Autowired
    private FilialRepository repository;
    
    public List<Filial> carrega() {
        return repository.findAll();
    }
    
}
