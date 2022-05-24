/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.utils;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Carlos
 */
public class TableUtils {
    
    public static void redimensionaColunas(JTable tabela) {
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) tabela.getColumnModel();
            TableColumn col = colModel.getColumn(i);
            int width = 110;

            TableCellRenderer renderer = col.getHeaderRenderer();
            for (int r = 0; r < tabela.getRowCount(); r++) {
                renderer = tabela.getCellRenderer(r, i);
                Component comp = renderer.getTableCellRendererComponent(tabela, tabela.getValueAt(r, i),
                        false, false, r, i);
                width = Math.max(width, comp.getPreferredSize().width);
            }
            col.setPreferredWidth(width + 2);
        }
    }
}
