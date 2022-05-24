/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.repositories.DepartamentoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;
    
    public List<Departamento> carregaTodos() {
        return repository.findAll();
    }
    
}
