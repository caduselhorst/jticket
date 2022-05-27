/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "pcsecao")
public class Secao implements Serializable {
    
    @EqualsAndHashCode.Include
    @Id
    private Long codsec;
    private String descricao;
    
    private Date dtexclusao;
    
    @OneToOne
    @JoinColumn(name = "codepto")
    private Departamento departamento;
}
