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
    private DateChooserCombo dateChooserCombo3;
    private DateChooserCombo dateChooserCombo4;
    private JTextPane jTextPane1;
    private JCheckBox jCheckBoxFiltroDataPrecoAlterado;
    private boolean somenteEstoquePositivo;
    private String repositorioArquivos;
    
    @Override
    public void run() {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ProdutoService produtoService = ctx.getBean(ProdutoService.class);
        EtiquetaEletronicaService eService = ctx.getBean(EtiquetaEletronicaService.class);
        
        Calendar cInicio = dateChooserCombo3.getCurrent();
        cInicio.set(Calendar.HOUR_OF_DAY, 0);
        cInicio.set(Calendar.MINUTE, 0);
        cInicio.set(Calendar.SECOND, 0);
        cInicio.set(Calendar.MILLISECOND, 0);
        Calendar cFim = dateChooserCombo4.getCurrent();
        cFim.set(Calendar.HOUR_OF_DAY, 0);
        cFim.set(Calendar.MINUTE, 0);
        cFim.set(Calendar.SECOND, 0);
        cFim.set(Calendar.MILLISECOND, 0);
        
        String strDtInicio = sdf.format(new Date(cInicio.getTimeInMillis()));
        String strDtFim = sdf.format(new Date(cFim.getTimeInMillis()));
        
        eService.logaInfo("Iniciando processo", jTextPane1);
        
        List<Produto> produtosFiltrados = null;
        List<Produto> produtosPrecosAlterados = null;
        
        
        eService.logaInfo("Carregando produtos", jTextPane1);
        FiltroProduto filtro = FiltroProduto.builder()
                .categorias(categorias)
                .departamentos(departamentos)
                .fornecedor(fornecedor)
                .produtos(produtos)
                .secoes(secoes)
                .subcategorias(subCategorias)
                .build();


        produtosFiltrados = produtoService.carregaProdutos(filtro);
        
        if (jCheckBoxFiltroDataPrecoAlterado.isSelected() && produtosFiltrados != null && produtosFiltrados.size() > 0) {
            LogTelaUtils.logaInfo(
                    String.format("Verificando produtos com alteração de preço entre %s e %s", strDtInicio, strDtFim), jTextPane1);
            eService = ctx.getBean(EtiquetaEletronicaService.class);
            
            List<Long> cods = produtosFiltrados.stream().map(p -> p.getCodprod()).collect(Collectors.toList());

            Set<Long> codprods = eService.carregaCodProdutosComAlteracaoDePrecoPeriodo(filial, regiao,
                    new Date(cInicio.getTimeInMillis()), new Date(cFim.getTimeInMillis()), cods);
            
            
            if(codprods != null && codprods.size() > 0) {
                FiltroProduto filtroPreco = FiltroProduto.builder()
                    .codigos(codprods)
                    .build();
                produtosPrecosAlterados = produtoService.carregaProdutos(filtroPreco);
                
            }
                
        }
        
        if (jCheckBoxFiltroDataPrecoAlterado.isSelected()) {
            if(produtosPrecosAlterados != null && produtosPrecosAlterados.size() > 0) {
                LogTelaUtils.logaInfo(String.format("Carregados [%d] produtos com preços alterados.", produtosPrecosAlterados.size()), jTextPane1);
                eService.geraEtiquetasEletronicas(filial, regiao, produtosPrecosAlterados, jTextPane1, somenteEstoquePositivo, repositorioArquivos);
            } else {
                eService.logaInfo("Não foram encontrados produtos com os critérios informados", jTextPane1);
            }
        } else {
            if(produtosFiltrados != null && produtosFiltrados.size() > 0) {
                LogTelaUtils.logaInfo(String.format("Carregados [%d] produtos.", produtosFiltrados.size()), jTextPane1);
                eService.geraEtiquetasEletronicas(filial, regiao, produtosFiltrados, jTextPane1, somenteEstoquePositivo, repositorioArquivos);
            } else {
                eService.logaInfo("Não foram encontrados produtos com os critérios informados", jTextPane1);
            }
        }
        
        eService.logaInfo("Processo finalizado", jTextPane1);
        
    }
    
}
