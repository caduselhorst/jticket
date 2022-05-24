/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Regiao;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class RegiaoTableModel extends AbstractTableModel {
    
    private List<Regiao> regioes;
    private String[] colunas = {"Código", "Região"};
    
    public RegiaoTableModel(List<Regiao> regioes) {
        if(regioes == null) {
            this.regioes = new ArrayList<>();
        } else {
            this.regioes = regioes;
        }
    }

    @Override
    public int getRowCount() {
        return regioes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Regiao r = regioes.get(rowIndex);
        switch (columnIndex) {
            case 0: {
                return r.getNumregiao();
            }
            case 1: {
                return r.getRegiao();
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
        } else {
            return String.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    public void setRegioes(List<Regiao> regioes) {
        if(regioes == null) {
            this.regioes = new ArrayList<>();
        } else {
            this.regioes = regioes;
        }
        
        fireTableDataChanged();;
    }
    
    public Regiao getRegiao(int rowIndex) {
        return regioes.get(rowIndex);
    }
    
}
