/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Filial;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class FilialTableModel extends AbstractTableModel {
    
    private List<Filial> filiais;
    private String[] colunas = {"Código", "Razão Social"};
    
    public FilialTableModel(List<Filial> filiais) {
        if(filiais == null) {
            this.filiais = new ArrayList<>();
        } else {
            this.filiais = filiais;
        }
    }

    @Override
    public int getRowCount() {
        return filiais.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Filial f = filiais.get(rowIndex);
        switch(columnIndex) {
            case 0: {
                return f.getCodigo();
            }
            case 1: {
                return f.getRazaosocial();
            }
            default: {
                throw new ArrayIndexOutOfBoundsException("Índice de coluna inválido: " + columnIndex);
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }
    
    public void setFiliais(List<Filial> filiais) {
        if(filiais == null) {
            this.filiais = new ArrayList<>();
        } else {
            this.filiais = filiais;
        }
        
        fireTableDataChanged();
    }
    
    public Filial getFilial(int rowIndex) {
        return filiais.get(rowIndex);
    }
    
}
