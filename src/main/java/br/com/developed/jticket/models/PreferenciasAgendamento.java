/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.models;

import br.com.developed.jticket.exceptions.NegocioException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author carlos
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferenciasAgendamento implements Serializable {
    
    private boolean diariamente;
    private String hora;
    private boolean aCada;
    private Integer indexHoraMinuto;
    private Integer quantidade;
    private String apartirDe;
    
    public void salvarPreferencias() {
        try {
            FileOutputStream out = new FileOutputStream("preferenciasAgendamento.ser");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(this);
            oos.close();
            out.close();
        } catch (IOException e) {
            throw new NegocioException("Ocorreu um erro ao gravar as preferĂȘncias", e);
        }
    }
    
    public PreferenciasAgendamento carregarPreferencias() {
        try {
            File f = new File("preferenciasAgendamento.ser");
            if(!f.exists()) {
                return null;
            }
            FileInputStream in = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(in);
            PreferenciasAgendamento p = (PreferenciasAgendamento) ois.readObject();
            ois.close();
            in.close();

            return p;
        } catch (IOException | ClassNotFoundException e) {
            throw new NegocioException("Ocorreu um erro ao carregar as preferĂȘncias", e);
        }
    }
    
}
