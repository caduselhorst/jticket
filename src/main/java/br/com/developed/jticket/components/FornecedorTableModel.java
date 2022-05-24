/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Fornecedor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class FornecedorTableModel extends AbstractTableModel {
    
    private List<Fornecedor> fornecedores;
    private String[] colunas = {"Código", "Razão Social", "Fantasia", "CNPJ"};
    
    public FornecedorTableModel(List<Fornecedor> fornecedores) {
        atribuiFornecedores(fornecedores);
    }
    
    @Override
    public int getRowCount() {
        return fornecedores.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Fornecedor f = fornecedores.get(rowIndex);
        switch(columnIndex) {
            case 0: {
                return f.getCodfornec();
            }
            case 1: {
                return f.getFornecedor();
            }
            case 2: {
                return f.getFantasia();
            }
            case 3: {
                return f.getCgc();
            }
            default: {
                throw new ArrayIndexOutOfBoundsException("Índice de coluna inválido: " + columnIndex);
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0) {
            return Long.class;
        }
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }
    
    public Fornecedor getFornecedor(int rowIndex) {
        return fornecedores.get(rowIndex);
    }
    
    public void setFornecedores(List<Fornecedor> fornecedores) {
        atribuiFornecedores(fornecedores);
        fireTableDataChanged();
    }
    
    private void atribuiFornecedores(List<Fornecedor> fornecedores) {
        if(fornecedores == null) {
            this.fornecedores = new ArrayList<>();
        } else {
            this.fornecedores = fornecedores;
        }
    }
}
