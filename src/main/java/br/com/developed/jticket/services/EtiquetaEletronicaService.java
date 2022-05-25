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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        
        if(filial.getUtilizavendaporembalagem() != null && filial.getUtilizavendaporembalagem().equals("S")) {
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
    
    public void geraEtiquetasEletronicas(Filial filial, Regiao regiao, List<Produto> produtos) {
        
        Integer code = NumberUtils.getRandomInteger();
        
        List<RegistroToledo> registros = new ArrayList<>();
        
        produtos.forEach(p -> {
            
            Integer esqueDisponivel = produtoRepository.buscaEstoqueDisponivel(p.getCodprod(), filial.getCodigo()).getEstoque();
            
            RegistroToledo.RegistroToledoBuilder registroToledoBuilder = RegistroToledo.builder();
            registroToledoBuilder
                .codBarrasPrincipal(p.getCodauxiliar())
                .codigo(code)
                .departamento(p.getDepartamento().getDescricao())
                .descricao(p.getDescricao())
                .unidade(p.getUnidade())
                .qtdEstoque(esqueDisponivel);
            
            if(filial.getUtilizavendaporembalagem() != null && filial.getUtilizavendaporembalagem().equals("S")){
                // Verifica preços na tabela de embalagem (rotina 2017)
                FiltroEmbalagem filtro = FiltroEmbalagem.builder()
                        .codfilial(filial.getCodigo())
                        .codproduto(p.getCodprod())
                        .build();
                List<Embalagem> embalagens = embalagemRepository.findAll(EmbalagemSpec.filtro(filtro), Sort.by("qtunit"));
                if(embalagens != null && embalagens.size() > 0) {
                    registros.add(registroToledoBuilder
                        .precoItemUnitario(embalagens.get(0).getPvenda())
                        .precoItemMaster(embalagens.get(embalagens.size() - 1).getPvenda())
                        .precoItemUnitarioPromo(embalagens.get(0).getPvenda())
                        .precoItemMasterPromo(embalagens.get(embalagens.size() - 1).getPvenda())
                        .promocao(false)
                        .build());
                }
            } else {
                // Verifica preços na tabela pctabpr e utiliza as funçẽs nativas para buscar dados de preço e preço de atacado
                log.info(String.format("Analisando produto: codprod[%d] filial[%s] codauxiliar[%d] numregiao[%d]",
                        p.getCodprod(), filial.getCodigo(), p.getCodauxiliar(), regiao.getNumregiao()));
                BuscaPreco bp = tabelaPrecoRepository.buscaPreco(filial.getCodigo(), regiao.getNumregiao(), p.getCodauxiliar());
                ProdutoFilial pf = produtoFilialRepository.findById(new ProdutoFilialId(p.getCodprod(), filial.getCodigo())).get();
                
                int qtMinAtacado = pf.getQtminimaatacado();
                
                if(qtMinAtacado == 0) {
                    qtMinAtacado = p.getQtminimaatacado();
                }
                
                registros.add(registroToledoBuilder
                    .precoItemUnitario(bp.getPvenda())
                    .precoItemUnitarioPromo(bp.getPvenda())
                    .precoItemMaster(bp.getPvendaatac() * qtMinAtacado)
                    .precoItemMasterPromo(bp.getPvendaatac() * qtMinAtacado)
                    .promocao(false)
                    .build());
            }
        });
        
        registros.forEach(r -> System.out.println(r));
        
    }
    
}
