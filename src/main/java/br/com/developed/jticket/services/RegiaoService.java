/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Regiao;
import br.com.developed.jticket.repositories.RegiaoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class RegiaoService {
    
    @Autowired
    private RegiaoRepository repository;
    
    public List<Regiao> carregaTodas() {
        return repository.findAll();
    }
    
}
