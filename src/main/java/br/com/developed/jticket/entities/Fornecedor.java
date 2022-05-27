/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "pcfornec")
public class Fornecedor implements Serializable {
    
    @EqualsAndHashCode.Include
    @Id
    private Long codfornec;
    private String fornecedor;
    private String fantasia;
    private String cgc;
    private String ie;
    private String ender;
    private String numeroend;
    private String bairro;
    private String cidade;
    private String estado;
    
}
