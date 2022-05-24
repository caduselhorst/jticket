/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "pcsubcategoria")
@IdClass(SubCategoriaId.class)
public class SubCategoria {
    
    @EqualsAndHashCode.Include
    @Id
    private Long codsubcategoria;
    
    @EqualsAndHashCode.Include
    @Id
    private Long codcategoria;
    
    @EqualsAndHashCode.Include
    @Id
    private Long codsec;
    
    private String subcategoria;
    
    
    
}
