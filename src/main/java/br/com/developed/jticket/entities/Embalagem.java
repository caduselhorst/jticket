/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Cleanup;
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
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pcembalagem")
@IdClass(EmbalagemId.class)
public class Embalagem {
    
    @EqualsAndHashCode.Include
    @Id
    private Long codauxiliar;
    
    @EqualsAndHashCode.Include
    @Id
    private String codfilial;
    
    private Long codprod;
    
    private String embalagem;
    private String unidade;
    private Integer qtunit;
    private Double pvenda;
    private Double pvendaatac;
    private Date dtultaltpvenda;
    private Date dtinativo;
    private String descricaoecf;
    
}
