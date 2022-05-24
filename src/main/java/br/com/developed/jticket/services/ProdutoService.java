/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.models.FiltroProduto;
import br.com.developed.jticket.repositories.ProdutoRepository;
import br.com.developed.jticket.specs.ProdutoSpec;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;
    
    
    public List<Produto> carregaProdutos(FiltroProduto filtro) {
        return repository.findAll(ProdutoSpec.filtro(filtro));
    }
    
}
