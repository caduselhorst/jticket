/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.components;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author carlos
 */
public class ComboBoxHoraMinutoModel extends DefaultComboBoxModel<String> {
    
    private final String [] tipo = {"horas", "minutos"};

    @Override
    public String getElementAt(int index) {
        return tipo[index];
    }

    @Override
    public int getSize() {
        return tipo.length;
    }
    
    
    
    
    
}
