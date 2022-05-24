/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Departamento;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class DepartamentoTableModel extends AbstractTableModel {
    
    private List<Departamento> departamentos;
    private String[] colunas = {"Código", "Descrição"};
    
    public DepartamentoTableModel(List<Departamento> departamentos) {
        if(departamentos == null) {
            this.departamentos = new ArrayList<>();
        } else {
            this.departamentos = departamentos;
        }
    }

    @Override
    public int getRowCount() {
        return departamentos.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Departamento d = departamentos.get(rowIndex);
        switch (columnIndex) {
            case 0: {
                return d.getCodepto();
            }
            case 1: {
                return d.getDescricao();
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

    public void setDepartamentos(List<Departamento> departamentos) {
        if(departamentos == null) {
            this.departamentos = new ArrayList<>();
        } else {
            this.departamentos = departamentos;
        }
        
        fireTableDataChanged();;
    }
    
    public Departamento getDepartamento(int rowIndex) {
        return departamentos.get(rowIndex);
    }
    
}
