/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author carlos
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "pcprodut")
public class Produto {
    
    @EqualsAndHashCode.Include
    @Id
    private Long codprod;
    private String descricao;
    private String embalagem;
    private String unidade;
    private Long codauxiliar;
    private Long codauxiliar2;
    
    @ManyToOne
    @JoinColumn(name = "codfornec")
    private Fornecedor fornecedor;
    
    
    @ManyToOne
    @JoinColumn(name = "codepto")
    private Departamento departamento;
    
    @ManyToOne
    @JoinColumn(name = "codsec")
    private Secao secao;
    
    private Long codcategoria;
    private Long codsubcategoria;
    
    private Integer qtminimaatacado; 
}
