/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.models;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.entities.Fornecedor;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.entities.Secao;
import br.com.developed.jticket.entities.SubCategoria;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author carlos
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiltroProduto {
    
    private Long codigo;
    private Set<Long> codigos;
    private String descricao;
    private Fornecedor fornecedor;
    private List<Departamento> departamentos;
    private List<Secao> secoes;
    private List<Categoria> categorias;
    private List<SubCategoria> subcategorias;
    private List<Produto> produtos;
    
}
