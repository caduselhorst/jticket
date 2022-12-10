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
import br.com.developed.jticket.utils.LogTelaUtils;
import datechooser.beans.DateChooserCombo;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author carlos
 */
@Slf4j
@AllArgsConstructor
public class EtiquetaEletronicaServiceProcess extends Thread {
    
    private Filial filial;
    private List<Departamento> departamentos;
    private List<Secao> secoes;
    private List<Categoria> categorias;
    private List<SubCategoria> subCategorias;
    private Fornecedor fornecedor;
    private Regiao regiao;
    private List<Produto> produtos;
    private final ApplicationContext ctx;
    private Calendar dateChooserCombo3;
    private Calendar dateChooserCombo4;
    private JTextPane jTextPane1;
    private boolean somenteEstoquePositivo;
    private String repositorioArquivos;
    private TipoPreco tipoPreco;
    
    @Override
    public void run() {
        
        log.info("Iniciando processo");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ProdutoService produtoService = ctx.getBean(ProdutoService.class);
        EtiquetaEletronicaService eService = ctx.getBean(EtiquetaEletronicaService.class);

        Calendar cInicio = dateChooserCombo3;
        cInicio.set(Calendar.HOUR_OF_DAY, 0);
        cInicio.set(Calendar.MINUTE, 0);
        cInicio.set(Calendar.SECOND, 0);
        cInicio.set(Calendar.MILLISECOND, 0);
        Calendar cFim = dateChooserCombo4;
        cFim.set(Calendar.HOUR_OF_DAY, 23);
        cFim.set(Calendar.MINUTE, 59);
        cFim.set(Calendar.SECOND, 59);
        cFim.set(Calendar.MILLISECOND, 999);

        String strDtInicio = sdf.format(new Date(cInicio.getTimeInMillis()));
        String strDtFim = sdf.format(new Date(cFim.getTimeInMillis()));
        
        
        try {
            
            log.info(String.format("Intervalo de datas: %s a %s", strDtInicio, strDtFim));

            eService.logaInfo("Iniciando processo", jTextPane1);

            List<Produto> produtosFiltrados = null;
            List<Produto> produtosPrecosAlterados = null;

            Integer tp;
            if(tipoPreco.equals(TipoPreco.EMBALAGEM)) {
                tp = 2017;
            } else {
                tp = 201;
            }

            eService.logaInfo("Carregando produtos", jTextPane1);
            FiltroProduto filtro = FiltroProduto.builder()
                    .categorias(categorias)
                    .departamentos(departamentos)
                    .fornecedor(fornecedor)
                    .produtos(produtos)
                    .secoes(secoes)
                    .subcategorias(subCategorias)
                    .regiao(regiao.getNumregiao())
                    .dataInicioAlteracaoPreco(new Date(cInicio.getTimeInMillis()))
                    .dataFimAlteracaoPreco(new Date(cFim.getTimeInMillis()))
                    .codfilial(filial.getCodigo())
                    .tipoPreco(tp)
                    .build();
            log.info(String.format("Filtro: %s", filtro.toString()));

            produtosFiltrados = produtoService.carregaProdutos(filtro);
            
            if(produtosFiltrados != null && produtosFiltrados.size() > 0) {
                eService.logaInfo(String.format("Foram selecionados [%d] produtos", produtosFiltrados.size()), jTextPane1);
                eService.geraEtiquetasEletronicas(filial, regiao, produtosFiltrados, jTextPane1, somenteEstoquePositivo, repositorioArquivos);
            } else {
                eService.logaInfo("Não foram encontrados produtos com os critérios informados", jTextPane1);
            }
            
        } catch (Exception e) {
            log.error("Erro ao executar o processo", e);
            eService.logaErro("Erro ao executar o processo:\n" + e.getMessage() + (e.getCause() != null ? "\nCausado por: " + e.getCause().getMessage() : ""), jTextPane1);
        }
            
        eService.logaInfo("Processo finalizado", jTextPane1);
        
    }
    
}
