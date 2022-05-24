/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.constraints.TipoFiltroFornecedor;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author carlos
 */
public class TipoPesquisaFornecedorComboBoxModel extends DefaultComboBoxModel<String> {
    
    private String[] tipos = {"Cód. Fornecedor", "Razão Social", "Fantasia"};

    @Override
    public int getSize() {
        return tipos.length;
    }

    @Override
    public String getElementAt(int index) {
        return tipos[index];
    }
    
}
