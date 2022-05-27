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
import java.util.Date;
import java.util.List;
import java.util.Set;
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
        String strDtInicio = sdf.format(new Date(dateChooserCombo3.getCurrent().getTimeInMillis()));
        String strDtFim = sdf.format(new Date(dateChooserCombo4.getCurrent().getTimeInMillis()));
        
        eService.logaInfo("Iniciando processo", jTextPane1);
        
        List<Produto> produtosFiltrados;
        
        if (jCheckBoxFiltroDataPrecoAlterado.isSelected()) {
            LogTelaUtils.logaInfo(
                    String.format("Verificando produtos com alteração de preço entre %s e %s", strDtInicio, strDtFim), jTextPane1);
            eService = ctx.getBean(EtiquetaEletronicaService.class);

            Set<Long> codprods = eService.carregaCodProdutosComAlteracaoDePrecoPeriodo(filial, regiao,
                    new Date(dateChooserCombo3.getCurrent().getTimeInMillis()), new Date(dateChooserCombo4.getCurrent().getTimeInMillis()));
            FiltroProduto filtro = FiltroProduto.builder()
                    .codigos(codprods)
                    .build();
            produtosFiltrados = produtoService.carregaProdutos(filtro);
            eService.logaInfo("Produtos carregados", jTextPane1);
        } else {
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
            eService.logaInfo("Produtos carregados", jTextPane1);
            
        }
        eService.geraEtiquetasEletronicas(filial, regiao, produtosFiltrados, jTextPane1, somenteEstoquePositivo, repositorioArquivos);
        eService.logaInfo("Processo finalizado", jTextPane1);
        
    }
    
}
