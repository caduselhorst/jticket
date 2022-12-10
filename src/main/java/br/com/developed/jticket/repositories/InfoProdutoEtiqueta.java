/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.developed.jticket.repositories;

import java.util.Date;

/**
 *
 * @author carlos
 */
public interface InfoProdutoEtiqueta {
    
    public Integer getSelecionado();
    public Long getCodepto();
    public String getDescDepto();
    public Long getCodsec();
    public Long getCodcategoria();
    public Integer getQtunit();
    public Double getEstoque_disponivel();
    public Long getCodprod();
    public Long getCodauxiliar();
    public String getCodfilial();
    public Date getDtultaltpvenda();
    public Long getNumnotaultent();
    public Date getDtultent();
    public Integer getQtminimaatacado();
    public Integer getPrazoval();
    public String getEmbalagem();
    public String getUnidade();
    public Integer getQtunit_emb();
    public String getDescproduto();
    public String getDescricao();
    public Integer getQtunitcx();
    public Date getData();
    public Double getPvenda();
    public Double getPvendaatac();
    public Date getDtiniciovigencia();
    public Date getDtfimvigencia();
    public Double getPrecofixo();
    public String getTipo();
    public String getTipoPreco();
    
}
