/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.entities.Filial;
import br.com.developed.jticket.entities.Fornecedor;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.entities.Regiao;
import br.com.developed.jticket.entities.Secao;
import br.com.developed.jticket.entities.SubCategoria;
import br.com.developed.jticket.models.FiltroProduto;
import br.com.developed.jticket.models.PreferenciasAgendamento;
import br.com.developed.jticket.utils.LogTelaUtils;
import datechooser.beans.DateChooserCombo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 *
 * @author carlos
 */
@Slf4j
public class AgendamentoEtiquetaEletronicaService extends Thread {

    private Filial filial;
    private List<Departamento> departamentos;
    private List<Secao> secoes;
    private List<Categoria> categorias;
    private List<SubCategoria> subCategorias;
    private Fornecedor fornecedor;
    private Regiao regiao;
    private List<Produto> produtos;
    private final ApplicationContext ctx;
    private DateChooserCombo dateChooserCombo3;
    private DateChooserCombo dateChooserCombo4;
    private JTextPane jTextPane1;
    private JCheckBox jCheckBoxFiltroDataPrecoAlterado;
    private boolean somenteEstoquePositivo;
    private String repositorioArquivos;
    private JLabel labelStatus;

    private PreferenciasAgendamento preferenciasAgendamento;
    

    public AgendamentoEtiquetaEletronicaService(
            Filial filial,
            List<Departamento> departamentos,
            List<Secao> secoes,
            List<Categoria> categorias,
            List<SubCategoria> subCategorias,
            Fornecedor fornecedor,
            Regiao regiao,
            List<Produto> produtos,
            ApplicationContext ctx,
            DateChooserCombo dateChooserCombo3,
            DateChooserCombo dateChooserCombo4,
            JTextPane jTextPane1,
            JCheckBox jCheckBoxFiltroDataPrecoAlterado,
            boolean somenteEstoquePositivo,
            String repositorioArquivos,
            PreferenciasAgendamento preferenciasAgendamento,
            JLabel labelStatus
    ) {
        this.filial = filial;
        this.departamentos = departamentos;
        this.secoes = secoes;
        this.categorias = categorias;
        this.subCategorias = subCategorias;
        this.fornecedor = fornecedor;
        this.regiao = regiao;
        this.produtos = produtos;
        this.ctx = ctx;
        this.dateChooserCombo3 = dateChooserCombo3;
        this.dateChooserCombo4 = dateChooserCombo4;
        this.jTextPane1 = jTextPane1;
        this.jCheckBoxFiltroDataPrecoAlterado = jCheckBoxFiltroDataPrecoAlterado;
        this.somenteEstoquePositivo = somenteEstoquePositivo;
        this.repositorioArquivos = repositorioArquivos;
        this.preferenciasAgendamento = preferenciasAgendamento;
        this.labelStatus = labelStatus;
        
        
    }

    private boolean parar;

    public void parar() {
        parar = true;
    }

    @Override
    public void run() {
        
        long timestamp = Calendar.getInstance().getTimeInMillis();
        long proximaExecucao = getProximaExecucao(true);
        
        if(proximaExecucao > timestamp) {
            labelStatus.setText("Proxima execu????o: " + new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm:ss").format(new Date(proximaExecucao)));
        }
        
        while (!parar) {
            
            if(timestamp >= proximaExecucao) {
                
                labelStatus.setText("Em execu????o ...");
                jTextPane1.setText(null);
                
                EtiquetaEletronicaServiceProcess p = new EtiquetaEletronicaServiceProcess(filial, departamentos, secoes, categorias, 
                        subCategorias, fornecedor, regiao, produtos, ctx, dateChooserCombo3, dateChooserCombo4, jTextPane1, 
                        jCheckBoxFiltroDataPrecoAlterado, somenteEstoquePositivo, repositorioArquivos);
                p.start();
                
                timestamp = Calendar.getInstance().getTimeInMillis();
                proximaExecucao = getProximaExecucao(false);
                
                labelStatus.setText("Proxima execu????o: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(proximaExecucao)));
                
                
            } else {
                try {
                    Thread.sleep(500);
                    timestamp = Calendar.getInstance().getTimeInMillis();
                } catch (InterruptedException e) {
                    log.warn("Ocorreu um erro de interrup????o de thread");
                }
            }
            
        }
    }
    
    private long getProximaExecucao(boolean first) {
        
        Calendar atual = Calendar.getInstance();
        
        if(first) {
            
            Calendar aux = Calendar.getInstance();
            if(preferenciasAgendamento.isDiariamente()) {
                String[] parsed = StringUtils.tokenizeToStringArray(preferenciasAgendamento.getHora(), ":");
                aux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parsed[0]));
                aux.set(Calendar.MINUTE, Integer.parseInt(parsed[1]));
                aux.set(Calendar.SECOND, Integer.parseInt(parsed[2]));
                
                if(aux.getTimeInMillis() <= atual.getTimeInMillis()) {
                    aux.add(Calendar.DAY_OF_MONTH, 1);
                }
                
                return aux.getTimeInMillis();
                
            } else {
                
                String[] parsed = StringUtils.tokenizeToStringArray(preferenciasAgendamento.getApartirDe(), ":");
                aux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parsed[0]));
                aux.set(Calendar.MINUTE, Integer.parseInt(parsed[1]));
                aux.set(Calendar.SECOND, Integer.parseInt(parsed[2]));
                if(atual.getTimeInMillis() > aux.getTimeInMillis()) {
                    return atual.getTimeInMillis();
                } else {
                    return aux.getTimeInMillis();
                }
                
            }
            
        } else {
            if (preferenciasAgendamento.isDiariamente()) {
                atual.add(Calendar.DAY_OF_MONTH, 1);
                String[] parsed = StringUtils.tokenizeToStringArray(preferenciasAgendamento.getHora(), ":");
                atual.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parsed[0]));
                atual.set(Calendar.MINUTE, Integer.parseInt(parsed[1]));
                atual.set(Calendar.SECOND, Integer.parseInt(parsed[2]));
                
                return atual.getTimeInMillis();
            } else {
                switch (preferenciasAgendamento.getIndexHoraMinuto()) {
                    case 0: {
                        atual.add(Calendar.HOUR_OF_DAY, preferenciasAgendamento.getQuantidade());
                        break;
                    }
                    default: {
                        atual.add(Calendar.MINUTE, preferenciasAgendamento.getQuantidade());
                        break;
                    }               

                }
                return atual.getTimeInMillis();
            }
        }
        
    }

}
