/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.constraints.TipoPreco;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;


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
    
    private TipoPreco tipoPreco;

    private PreferenciasAgendamento preferenciasAgendamento;
    
    private Long ultimaExecucao;
    private Long proximaExecucao;
    
    private final Map<Integer, String> mapDiaSemana = new HashMap<>();
    

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
            boolean somenteEstoquePositivo,
            String repositorioArquivos,
            PreferenciasAgendamento preferenciasAgendamento,
            JLabel labelStatus,
            TipoPreco tipoPreco
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
        this.somenteEstoquePositivo = somenteEstoquePositivo;
        this.repositorioArquivos = repositorioArquivos;
        this.preferenciasAgendamento = preferenciasAgendamento;
        this.labelStatus = labelStatus;
        this.tipoPreco = tipoPreco;
        
        if(preferenciasAgendamento.isDom()) {
            mapDiaSemana.put(Calendar.SUNDAY, preferenciasAgendamento.getHorarioDom());
        }
        
        if(preferenciasAgendamento.isSeg()) {
            mapDiaSemana.put(Calendar.MONDAY, preferenciasAgendamento.getHorarioSeg());
        }
        
        if(preferenciasAgendamento.isTer()) {
            mapDiaSemana.put(Calendar.TUESDAY, preferenciasAgendamento.getHorarioTer());
        }
        
        if(preferenciasAgendamento.isQua()) {
            mapDiaSemana.put(Calendar.WEDNESDAY, preferenciasAgendamento.getHorarioQua());
        }
        
        if(preferenciasAgendamento.isQui()) {
            mapDiaSemana.put(Calendar.THURSDAY, preferenciasAgendamento.getHorarioQui());
        }
        
        if(preferenciasAgendamento.isSex()) {
            mapDiaSemana.put(Calendar.FRIDAY, preferenciasAgendamento.getHorarioSex());
        }
        
        if(preferenciasAgendamento.isSab()) {
            mapDiaSemana.put(Calendar.SATURDAY, preferenciasAgendamento.getHorarioSab());
        }
    }

    private boolean parar;

    public void parar() {
        parar = true;
    }

    @Override
    public void run() {
        
        Calendar time = Calendar.getInstance();
        
        long timestamp = time.getTimeInMillis();
        long proximaExecucao = getProximaExecucao();
        
        labelStatus.setText("Proxima execução: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(proximaExecucao)));
        
        while (!parar) {
            
            //labelStatus.setText("Proxima execução: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(proximaExecucao)));
            
            if(timestamp >= proximaExecucao) {
                
                labelStatus.setText("Em execução ...");
                jTextPane1.setText(null);
                
                Calendar dtInicio = Calendar.getInstance();
                Calendar dtFim = Calendar.getInstance();
                
                if(!preferenciasAgendamento.isRecorrente()) {
                    dtInicio.add(Calendar.DAY_OF_MONTH, -1);
                }
                
                EtiquetaEletronicaServiceProcess p = new EtiquetaEletronicaServiceProcess(filial, departamentos, secoes, categorias, 
                        subCategorias, fornecedor, regiao, produtos, ctx, dtInicio, dtFim, jTextPane1, 
                        somenteEstoquePositivo, repositorioArquivos, tipoPreco);
                p.start();
                
                ultimaExecucao = Calendar.getInstance().getTimeInMillis();
                proximaExecucao = getProximaExecucao();
                
                labelStatus.setText("Proxima execução: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(proximaExecucao)));
                
                
            } else {
                try {
                    Thread.sleep(500);
                    time.add(Calendar.MILLISECOND, 500);
                    timestamp = time.getTimeInMillis();
                } catch (InterruptedException e) {
                    log.warn("Ocorreu um erro de interrupção de thread");
                }
            }
            
        }
    }
    
    private long getProximaExecucao() {
        
        Calendar atual = Calendar.getInstance();
        
        if(preferenciasAgendamento.isRecorrente()) {
            Calendar aux = Calendar.getInstance();
            
            String[] parsed = StringUtils.split(preferenciasAgendamento.getApartirDe(), ":");
            
            aux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parsed[0]));
            aux.set(Calendar.MINUTE, Integer.parseInt(parsed[1]));
            aux.set(Calendar.SECOND, Integer.parseInt(parsed[2]));
            
            if (ultimaExecucao == null || atual.getTimeInMillis() < aux.getTimeInMillis()) {
                return aux.getTimeInMillis();
            } else {
                atual.add(
                    (preferenciasAgendamento.getIndexHoraMinuto() == 0 ? Calendar.HOUR_OF_DAY : Calendar.MINUTE), 
                        preferenciasAgendamento.getQuantidade());
                
                return atual.getTimeInMillis();
            }
            
        } else {
            
            if(ultimaExecucao == null) {
                
                Calendar aux = Calendar.getInstance();
                
                while(!mapDiaSemana.containsKey(aux.get(Calendar.DAY_OF_WEEK))) {
                    aux.add(Calendar.DAY_OF_MONTH, 1);
                }
                
                String horario = mapDiaSemana.get(aux.get(Calendar.DAY_OF_WEEK));
                
                String[] hora = StringUtils.split(horario, ":");
                
                aux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora[0]));
                aux.set(Calendar.MINUTE, Integer.parseInt(hora[1]));
                aux.set(Calendar.SECOND, Integer.parseInt(hora[2]));
                
                if(atual.getTimeInMillis() < aux.getTimeInMillis()) {
                    return aux.getTimeInMillis();
                } else {
                    atual.add(Calendar.DAY_OF_MONTH, 1);
                    while(!mapDiaSemana.containsKey(atual.get(Calendar.DAY_OF_WEEK))) {
                        atual.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    
                    horario = mapDiaSemana.get(atual.get(Calendar.DAY_OF_WEEK));
                
                    hora = StringUtils.split(horario, ":");
                
                    atual.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora[0]));
                    atual.set(Calendar.MINUTE, Integer.parseInt(hora[1]));
                    atual.set(Calendar.SECOND, Integer.parseInt(hora[2]));
                    
                    return atual.getTimeInMillis();
                }
                
            } else {
                Calendar aux = Calendar.getInstance();
                
                aux.setTimeInMillis(ultimaExecucao);
                
                aux.add(Calendar.DAY_OF_MONTH, 1);
                
                while(!mapDiaSemana.containsKey(aux.get(Calendar.DAY_OF_WEEK))) {
                    aux.add(Calendar.DAY_OF_MONTH, 1);
                }
                
                String horario = mapDiaSemana.get(aux.get(Calendar.DAY_OF_WEEK));
                
                String[] hora = StringUtils.split(horario, ":");
                
                aux.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora[0]));
                aux.set(Calendar.MINUTE, Integer.parseInt(hora[1]));
                aux.set(Calendar.SECOND, Integer.parseInt(hora[2]));
                
                return aux.getTimeInMillis();
            }
        }
        
    }

}
