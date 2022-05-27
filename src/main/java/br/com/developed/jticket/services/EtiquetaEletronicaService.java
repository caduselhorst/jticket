/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.entities.Embalagem;
import br.com.developed.jticket.entities.Filial;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.entities.ProdutoFilial;
import br.com.developed.jticket.entities.ProdutoFilialId;
import br.com.developed.jticket.entities.Regiao;
import br.com.developed.jticket.entities.TabelaPreco;
import br.com.developed.jticket.entities.TabelaPrecoId;
import br.com.developed.jticket.models.BuscaPreco;
import br.com.developed.jticket.models.FiltroEmbalagem;
import br.com.developed.jticket.models.FiltroTabelaPreco;
import br.com.developed.jticket.models.RegistroToledo;
import br.com.developed.jticket.repositories.EmbalagemRepository;
import br.com.developed.jticket.repositories.ProdutoFilialRepository;
import br.com.developed.jticket.repositories.ProdutoRepository;
import br.com.developed.jticket.repositories.TabelaPrecoRepository;
import br.com.developed.jticket.specs.EmbalagemSpec;
import br.com.developed.jticket.specs.TabelaPrecoSpec;
import br.com.developed.jticket.utils.NumberUtils;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Slf4j
@Service
public class EtiquetaEletronicaService {
    
    public class LogaMensagemTela extends Thread {
        
        private Color color;
        private String mensagem;
        private JTextPane pane;
        
        public LogaMensagemTela(Color color, String mensagem, JTextPane pane) {
            this.color = color;
            this.mensagem = mensagem;
            this.pane = pane;
        }
        
        @Override
        public void run() {
            try {
                StyledDocument doc = pane.getStyledDocument();
                Style style = pane.getStyle("Style");
                if (style == null) {
                    style = pane.addStyle("Style", null);
                }
                StyleConstants.setForeground(style, color);
                pane.getStyledDocument().insertString(pane.getStyledDocument().getLength(), String.format(MSG_TEMPLATE, SDF.format(new Date()), mensagem), style);
            } catch (BadLocationException e) {
                log.error("Ocorreu um erro ao inserir o log na tela", e);
            }
        }
        
    }
    
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final String MSG_TEMPLATE = "%s - %s\n";
    
    private static final Color MINHA_MSG = Color.BLUE;
    private static final Color CLIENTE_MSG = new Color(255, 140, 0);
    private static final Color ERRO_MSG = Color.RED;
    
    @Autowired
    private TabelaPrecoRepository tabelaPrecoRepository;
    @Autowired
    private EmbalagemRepository embalagemRepository;
    @Autowired
    private ProdutoFilialRepository produtoFilialRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public Set<Long> carregaCodProdutosComAlteracaoDePrecoPeriodo(Filial filial, Regiao regiao,
            Date dataInicial, Date dataFinal) {
        
        Set<Long> codprods = new HashSet<>();
        
        if (filial.getUtilizavendaporembalagem() != null && filial.getUtilizavendaporembalagem().equals("S")) {
            // busca preços alterados na 2017
            
            FiltroEmbalagem filtro = FiltroEmbalagem.builder()
                    .codfilial(filial.getCodigo())
                    .dataAlteracaoInicio(dataInicial)
                    .dataAlteracaoFim(dataFinal)
                    .build();
            
            embalagemRepository.findAll(EmbalagemSpec.filtro(filtro))
                    .forEach(e -> codprods.add(e.getCodprod()));
            
        } else {
            // busca preços alterados na 201
            FiltroTabelaPreco filtro = FiltroTabelaPreco.builder()
                    .regiao(regiao)
                    .dataAlteracaoInicial(dataInicial)
                    .dataAlteracaoFinal(dataFinal)
                    .build();
            tabelaPrecoRepository.findAll(TabelaPrecoSpec.filtro(filtro)).forEach(p -> codprods.add(p.getCodprod()));
        }
        
        return codprods;
    }
    
    public void geraEtiquetasEletronicas(Filial filial, Regiao regiao, List<Produto> produtos, JTextPane logTela, boolean somenteEstoquePositivo, String repositorioArquivo) {
        
        logaInfo("Gerando arquivo de etiquetas eletrônicas", logTela);
        Integer code = NumberUtils.getRandomInteger();
        
        List<RegistroToledo> registros = new ArrayList<>();
        
        produtos.forEach(p -> {
            
            Integer estoqueDisponivel = produtoRepository.buscaEstoqueDisponivel(p.getCodprod(), filial.getCodigo()).getEstoque();
            
            if (!somenteEstoquePositivo || (somenteEstoquePositivo && estoqueDisponivel > 0)) {
                RegistroToledo.RegistroToledoBuilder registroToledoBuilder = RegistroToledo.builder();
                registroToledoBuilder
                        .codigo(code)
                        .departamento(p.getDepartamento().getDescricao())
                        .qtdEstoque(estoqueDisponivel);
                
                if (filial.getUtilizavendaporembalagem() != null && filial.getUtilizavendaporembalagem().equals("S")) {
                    // Verifica preços na tabela de embalagem (rotina 2017)
                    FiltroEmbalagem filtro = FiltroEmbalagem.builder()
                            .codfilial(filial.getCodigo())
                            .codproduto(p.getCodprod())
                            .build();
                    List<Embalagem> embalagens = embalagemRepository.findAll(EmbalagemSpec.filtro(filtro), Sort.by("qtunit"));
                    if (embalagens != null && embalagens.size() > 0) {
                        registros.add(registroToledoBuilder
                                .precoItemUnitario(embalagens.get(0).getPvenda())
                                .precoItemMaster(embalagens.get(embalagens.size() - 1).getPvenda())
                                .precoItemUnitarioPromo(embalagens.get(0).getPvenda())
                                .precoItemMasterPromo(embalagens.get(embalagens.size() - 1).getPvenda())
                                .build());
                    }
                } else {
                    // Verifica preços na tabela pctabpr e utiliza as funçẽs nativas para buscar dados de preço e preço de atacado
                    if (filial.getCodigo() != null && regiao.getNumregiao() != null && p.getCodauxiliar() != null) {
                        
                        FiltroEmbalagem filtroEmbalagem = FiltroEmbalagem.builder()
                                .codfilial(filial.getCodigo())
                                .codproduto(p.getCodprod())
                                .build();
                        
                        List<Embalagem> embalagens = embalagemRepository.findAll(EmbalagemSpec.filtro(filtroEmbalagem));
                        BuscaPreco bp = tabelaPrecoRepository.buscaPreco(filial.getCodigo(), regiao.getNumregiao(), p.getCodauxiliar());
                        ProdutoFilial pf = produtoFilialRepository.findById(new ProdutoFilialId(p.getCodprod(), filial.getCodigo())).get();
                        
                        TabelaPreco tp = tabelaPrecoRepository.findById(new TabelaPrecoId(p.getCodprod(), regiao.getNumregiao())).get();
                        
                        embalagens.forEach(emb -> {
                            registros.add(registroToledoBuilder
                                    .codBarrasPrincipal(emb.getCodauxiliar())
                                    .unidade(emb.getUnidade())
                                    .precoItemUnitario(bp.getPvenda() * emb.getQtunit())
                                    .precoItemUnitarioPromo(0.0)
                                    .precoItemMaster(bp.getPvendaatac() * emb.getQtunit())
                                    .precoItemMasterPromo(0.0)
                                    .tipo("ATACADO")
                                    .personalizado0(String.format("%.2f", bp.getPvendaatac() * emb.getQtunit()).replace(",", ""))
                                    .personalizado1(String.valueOf(emb.getQtunit()))
                                    .personalizado2("1") /* TO DO VERIFICAR */
                                    .personalizado3(String.valueOf(p.getCodprod()))
                                    .personalizado4("6") /* TO DO VERIFICAR */
                                    .personalizado5(String.valueOf(emb.getQtunit()))
                                    .personalizado6(String.format("%.2f", bp.getPvendaatac() * emb.getQtunit()).replace(",", ""))
                                    .personalizado7("")
                                    .personalizado8("data ult entrada")
                                    .personalizado9("nota ultima entrada")
                                    .personalizado10(new SimpleDateFormat("dd/MM/yyyy").format(tp.getDtultaltpvenda()))
                                    .personalizado11("NORMAL")
                                    .build());
                        });
                        
                    } else {
                        log.warn(String.format("Não foi possível processar o produto [%d]: codigo de barras: %d",
                                p.getCodprod(), p.getCodauxiliar()));
                    }
                    
                }
            } else {
                logaInfo(String.format("Produto [%d] com estoque <= 0 (%d). Não será considerado", p.getCodprod(), estoqueDisponivel), logTela);
            }
        });
        
        geraArquivos(repositorioArquivo, code, registros, logTela);
        
    }
    
    private void geraArquivos(String repositorioArquivo, Integer code, List<RegistroToledo> registros, JTextPane logTela) {
        try {
            String prefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            // Verifica a existência dos repositórios
            File diri1 = new File(repositorioArquivo + "/DataFiles");
            File dirm1 = new File(repositorioArquivo + "/MessageFiles");
            File dirr7 = new File(repositorioArquivo + "/ResultFiles");

            // Se não exist, cria
            if (!diri1.exists()) {
                diri1.mkdir();
            }
            
            if (!dirm1.exists()) {
                dirm1.mkdir();
            }
            
            if (!dirr7.exists()) {
                dirr7.mkdir();
            }

            // Criando os aquivos de mensagem (m1) e dados (i1)
            File i1 = new File(diri1.getAbsolutePath() + "/" + prefix + ".i1");
            File m1 = new File(dirm1.getAbsolutePath() + "/" + prefix + ".m1");

            // Gera arquivo de mensagens
            FileWriter writer = new FileWriter(m1);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.println("UPDATE," + code + ",," + i1.getAbsolutePath() + "," + dirr7.getAbsolutePath() + "/" + prefix + ".r7");
            printWriter.flush();
            printWriter.close();

            //Gera aquivo de dados
            writer = new FileWriter(i1);
            printWriter = new PrintWriter(writer);
            
            for (RegistroToledo r : registros) {
                printWriter.println(r);
            }
            printWriter.flush();
            printWriter.close();
            //registros.forEach(r -> printWriter.println(r));
            logaInfo("Arquivos gerados com sucesso", logTela);
        } catch (IOException e) {
            logaErro(e.getMessage(), logTela);
        }
    }
    
    public void loga(Color color, String mensagem, JTextPane pane) {
        
        try {
            StyledDocument doc = pane.getStyledDocument();
            Style style = pane.getStyle("Style");
            if (style == null) {
                style = pane.addStyle("Style", null);
            }
            StyleConstants.setForeground(style, color);
            pane.getStyledDocument().insertString(pane.getStyledDocument().getLength(), String.format(MSG_TEMPLATE, SDF.format(new Date()), mensagem), style);
            pane.setCaretPosition(pane.getStyledDocument().getLength());
        } catch (BadLocationException e) {
            log.error("Ocorreu um erro ao inserir o log na tela", e);
        }
        
    }
    
    public void logaInfo(String mensage, JTextPane pane) {
        loga(MINHA_MSG, mensage, pane);
    }
    
    public void logaWarn(String mensage, JTextPane pane) {
        loga(CLIENTE_MSG, mensage, pane);
    }
    
    public void logaErro(String mensage, JTextPane pane) {
        loga(ERRO_MSG, mensage, pane);
    }
}
