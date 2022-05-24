/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author carlos
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pcprodfilial")
@IdClass(ProdutoFilialId.class)
public class ProdutoFilial {
    
    @EqualsAndHashCode.Include
    @Id
    private Long codprod;
    
    @EqualsAndHashCode.Include
    @Id
    private String codfilial;
    
    private String foralinha;
    private Integer qtminimaatacado;
    
}
