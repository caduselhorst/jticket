/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.SubCategoria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class SubCategoriaTableModel extends AbstractTableModel {
    
    private List<SubCategoria> subCategorias;
    private String[] colunas = {"Código", "Sub-categoria"};
    
    public SubCategoriaTableModel(List<SubCategoria> subCategorias) {
        if(subCategorias == null) {
            this.subCategorias = new ArrayList<>();
        } else {
            this.subCategorias = subCategorias;
        }
    }

    @Override
    public int getRowCount() {
        return subCategorias.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SubCategoria sc = subCategorias.get(rowIndex);
        switch(columnIndex) {
            case 0: {
                return sc.getCodsubcategoria();
            }
            case 1: {
                return sc.getSubcategoria();
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
    
    public void setSubCategorias(List<SubCategoria> subCategorias) {
        if(subCategorias == null) {
            this.subCategorias = new ArrayList<>();
        } else {
            this.subCategorias = subCategorias;
        }
        fireTableDataChanged();
    }
    
    public SubCategoria getSubCategoria(int rowIndex) {
        return subCategorias.get(rowIndex);
    }
    
}
