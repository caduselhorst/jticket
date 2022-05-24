/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Categoria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class CategoriaTableModel extends AbstractTableModel {
    
    private List<Categoria> categorias;
    private String[] colunas = {"Código", "Categoria"};
    
    public CategoriaTableModel(List<Categoria> categorias) {
        if(categorias == null) {
            this.categorias = new ArrayList<>();
        } else {
            this.categorias = categorias;
        }
    }

    @Override
    public int getRowCount() {
        return categorias.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Categoria c = categorias.get(rowIndex);
        switch(columnIndex) {
            case 0: {
                return c.getCodcategoria();
            }
            case 1: {
                return c.getCategoria();
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
    
    public void setCategorias(List<Categoria> categorias) {
        if(categorias == null) {
            this.categorias = new ArrayList<>();
        } else {
            this.categorias = categorias;
        }
        fireTableDataChanged();
    }
    
    public Categoria getCategoria(int rowIndex) {
        return categorias.get(rowIndex);
    }
    
}
