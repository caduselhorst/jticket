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
@Table(name = "pctabpr")
@IdClass(TabelaPrecoId.class)
public class TabelaPreco {
    
    @EqualsAndHashCode.Include
    @Id
    private Long codprod;
    @EqualsAndHashCode.Include
    @Id
    private Long numregiao;
    
    private Date dtultaltpvenda;
    
}
