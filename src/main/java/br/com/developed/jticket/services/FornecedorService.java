/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.constraints.TipoFiltroFornecedor;
import br.com.developed.jticket.entities.Fornecedor;
import br.com.developed.jticket.exceptions.NegocioException;
import br.com.developed.jticket.repositories.FornecedorRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class FornecedorService {
    
    @Autowired
    private FornecedorRepository repository;
    
    public List<Fornecedor> carregaFornecedor(TipoFiltroFornecedor tipo, String valor) {
        
        if(tipo.equals(TipoFiltroFornecedor.CODIGO)) {
            try {
                Long cod = Long.parseLong(valor);
                
                Optional<Fornecedor> optFornecedor = repository.findById(cod);
                
                if(optFornecedor.isPresent()) {
                    return Arrays.asList(optFornecedor.get());
                } else {
                    return null;
                }
                
            } catch (NumberFormatException e) {
                throw new NegocioException("Para esse filtro só sáo permitidos números");
            }
        }
        
        if(tipo.equals(TipoFiltroFornecedor.FANTASIA)) {
            return repository.findByFantasiaContainsIgnoreCase(valor);
        }
        
        if(tipo.equals(TipoFiltroFornecedor.RAZAO)) {
            return repository.findByFornecedorContainsIgnoreCase(valor);
        }
        
        throw new NegocioException("Tipo de filtro inválido");
        
    }
    
}
