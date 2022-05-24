/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Secao;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class SecaoTableModel extends AbstractTableModel {
    
    private List<Secao> secoes;
    private String[] colunas = {"Codigo", "Descricao"};
    
    public SecaoTableModel(List<Secao> secoes) {
        if(secoes == null) {
            this.secoes = new ArrayList<>();
        } else {
            this.secoes = secoes;
        }
    }

    @Override
    public int getRowCount() {
        return secoes.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Secao s = secoes.get(rowIndex);
        switch(columnIndex) {
            case 0: {
                return s.getCodsec();
            }
            case 1: {
                return s.getDescricao();
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
    
    public Secao getSecao(int rowIndex) {
        return secoes.get(rowIndex);
    }
    
    public void setSecoes(List<Secao> secoes) {
        if(secoes == null) {
            this.secoes = new ArrayList<>();
        } else {
            this.secoes = secoes;
        }
        fireTableDataChanged();
    }
}
