/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.components;

import br.com.developed.jticket.entities.Produto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author carlos
 */
public class ProdutoTableModel extends AbstractTableModel {
    
    private List<Produto> produtos;
    private String[] colunas = {"Código", "Descrição", "Embalagem", "Unidade"};
    
    public ProdutoTableModel (List<Produto> produtos) {
        atribuiProdutos(produtos);
    }
    
    
    private void atribuiProdutos(List<Produto> produtos) {
        if(produtos == null) {
            this.produtos = new ArrayList<>();
        } else {
            this.produtos = produtos;
        }
    }

    @Override
    public int getRowCount() {
        return produtos.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Produto p = produtos.get(rowIndex);
        switch(columnIndex) {
            case 0: {
                return p.getCodprod();
            }
            case 1: {
                return p.getDescricao();
            }
            case 2: {
                return p.getEmbalagem();
            }
            case 3: {
                return p.getUnidade();
            }
            default: {
                throw  new ArrayIndexOutOfBoundsException("Índice de coluna inválido: " + columnIndex);
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
    
    public Produto getProduto(int rowIndex) {
        return produtos.get(rowIndex);
    }
    
    public void setProdutos(List<Produto> produtos) {
        atribuiProdutos(produtos);
        fireTableDataChanged();
    }
}
