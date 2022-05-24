/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.entities.Secao;
import br.com.developed.jticket.repositories.SecaoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class SecaoService {
    
    @Autowired
    private SecaoRepository repository;
    
    public List<Secao> carregaTodas() {
        return repository.findBydtexclusao(null);
    }
    
    public List<Secao> carregaPorDepartamento(Departamento departamento) {
        return repository.findByDepartamentoAndDtexclusao(departamento, null);
    }
}
