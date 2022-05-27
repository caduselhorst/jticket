/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.models;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.entities.Filial;
import br.com.developed.jticket.entities.Fornecedor;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.entities.Regiao;
import br.com.developed.jticket.entities.Secao;
import br.com.developed.jticket.entities.SubCategoria;
import br.com.developed.jticket.exceptions.NegocioException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
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
public class Preferencias implements Serializable {
    
    private Filial filial;
    private List<Departamento> departamentos;
    private List<Secao> secoes;
    private List<Categoria> categorias;
    private List<SubCategoria> subCategorias;
    private Fornecedor fornecedor;
    private Regiao regiao;
    private List<Produto> produtos;
    private boolean somenteEstoquePositivo;
    private String caminhoRepositorioArquivos;
    
    public void salvarPreferencias() {
        try {
            FileOutputStream out = new FileOutputStream("preferencias.ser");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(this);
            oos.close();
            out.close();
        } catch (IOException e) {
            throw new NegocioException("Ocorreu um erro ao gravar as preferências", e);
        }
    }
    
    public Preferencias carregarPreferencias() {
        try {
            File f = new File("preferencias.ser");
            if(!f.exists()) {
                return null;
            }
            FileInputStream in = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(in);
            Preferencias p = (Preferencias) ois.readObject();
            ois.close();
            in.close();

            return p;
        } catch (IOException | ClassNotFoundException e) {
            throw new NegocioException("Ocorreu um erro ao carregar as preferências", e);
        }
    }
}
