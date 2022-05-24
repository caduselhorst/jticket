/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author carlos
 */
@Entity
@Table(name="pcfilial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Filial {
    
    @EqualsAndHashCode.Include
    @Id
    private String codigo;
    private String razaosocial;
    private String endereco;
    private String cidade;
    private String uf;
    private String cep;
    private String telefone;
    private String cgc;
    private Long codcli;
    private Long codfornec;
    private String utilizavendaporembalagem;
    
}
