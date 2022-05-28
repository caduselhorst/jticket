/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.entities.Filial;
import br.com.developed.jticket.entities.Fornecedor;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.entities.Regiao;
import br.com.developed.jticket.entities.Secao;
import br.com.developed.jticket.repositories.CategoriaRepository;
import br.com.developed.jticket.repositories.DepartamentoRepository;
import br.com.developed.jticket.repositories.FilialRepository;
import br.com.developed.jticket.repositories.FornecedorRepository;
import br.com.developed.jticket.repositories.ProdutoRepository;
import br.com.developed.jticket.repositories.RegiaoRepository;
import br.com.developed.jticket.repositories.SecaoRepository;
import br.com.developed.jticket.repositories.SubCategoriaRepository;
import java.util.List;
import java.util.Optional;
import javax.swing.JTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class FormularioPrincipalService {
    
    @Autowired
    private FilialRepository filialRepository;
    @Autowired
    private RegiaoRepository regiaoRepository;
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private SecaoRepository secaoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    public Filial carregaFilial(String id) {
        Optional<Filial> filial = filialRepository.findById(id);
        if(filial.isEmpty()) {
            return null;
        } else {
            return filial.get();
        }
    }
    
    public Regiao carregaRegiao(Long id) {
        Optional<Regiao> regiao = regiaoRepository.findById(id);
        if(regiao.isEmpty()) {
            return null;
        } else {
            return regiao.get();
        }
    }
    
    public Departamento carregaDepartamento(Long id) {
        Optional<Departamento> departamento = departamentoRepository.findById(id);
        if(departamento.isEmpty()) {
            return null;
        } else {
            return departamento.get();
        }
    }
    
    public Secao carregaSecao(Long id) {
        Optional<Secao> secao = secaoRepository.findById(id);
        if(secao.isEmpty()) {
            return null;
        } else {
            return secao.get();
        }
    }
    
    public Produto carregaProduto(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if(produto.isEmpty()) {
            return null;
        } else {
            return produto.get();
        }
    }
    
    public Fornecedor carregaFornecedor(Long id) {
        Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
        if (fornecedor.isEmpty()) {
            return null;
        } else {
            return fornecedor.get();
        }
    }
    
    
    public void selecionaText(JTextField campo) {
        campo.selectAll();
    }
}
