/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.developed.jticket.models;

import java.util.Date;

/**
 *
 * @author carlos
 */
public interface PrecoPromocional {
    public Long getCodprecoprom();
    public Long getCodprod();
    public String getDescricao();
    public String getEmbalagem();
    public String getUnidade();
    public Long getNumregiao();
    public String getRegiao();
    public Double getPrecofixo();
    public Date getDtiniciovigencia();
    public Date getFimVigencia();
}
