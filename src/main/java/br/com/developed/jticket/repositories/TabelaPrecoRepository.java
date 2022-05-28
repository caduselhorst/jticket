/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.Embalagem;
import br.com.developed.jticket.entities.EmbalagemId;
import br.com.developed.jticket.entities.TabelaPreco;
import br.com.developed.jticket.entities.TabelaPrecoId;
import br.com.developed.jticket.models.BuscaPreco;
import br.com.developed.jticket.models.PrecoPromocional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author carlos
 */
public interface TabelaPrecoRepository extends JpaRepository<TabelaPreco,TabelaPrecoId>, 
        JpaSpecificationExecutor<TabelaPreco> {
    
    @Query(nativeQuery = true, value="select " +
                                    "coluna_preco( " +
                                        "buscaprecos(:codfilial, :numregiao, :codauxiliar, trunc(sysdate)), " +
                                        "'PVENDA') pvenda, " +
                                    "coluna_preco( " +
                                        "buscaprecos(:codfilial, :numregiao, :codauxiliar, trunc(sysdate)), " +
                                    "'PVENDAATAC') pvendaatac " +
                                "from  dual")
    public BuscaPreco buscaPreco(@Param("codfilial") String codfilial, @Param("numregiao") Long numregiao, 
            @Param("codauxiliar") Long codauxiliar);
    
    @Query(nativeQuery = true, value="SELECT " +
                                        "PCPRECOPROM.CODPRECOPROM, " +
                                        "PCPRECOPROM.CODPROD, " +
                                        "PCPRODUT.DESCRICAO, " +
                                        "PCPRODUT.EMBALAGEM, " +
                                        "PCPRODUT.UNIDADE, " +
                                        "PCPRECOPROM.NUMREGIAO, " +
                                        "PCREGIAO.REGIAO, " +
                                        "PCPRECOPROM.PRECOFIXO, " +
                                        "PCPRECOPROM.DTINICIOVIGENCIA, " +
                                        "PCPRECOPROM.DTFIMVIGENCIA " +
                                    "FROM " +
                                        "PCPRECOPROM, " +
                                        "PCPRODUT, " +
                                        "PCREGIAO, " +
                                        "PCPLPAG, " +
                                        "PCCLIENT, " +
                                        "PCTABPR, " +
                                        "PCREDECLIENTE, " +
                                        "PCFORNEC " +
                                    "WHERE PCPRECOPROM.CODPROD = PCTABPR.CODPROD " +
                                        "AND PCPRECOPROM.NUMREGIAO = PCTABPR.NUMREGIAO " +
                                        "AND PCPRECOPROM.CODPROD = PCPRODUT.CODPROD " +
                                        "AND PCPRECOPROM.NUMREGIAO = PCREGIAO.NUMREGIAO " +
                                        "AND PCPRECOPROM.CODPLPAGMAX = PCPLPAG.CODPLPAG(+) " +
                                        "AND PCPRECOPROM.CODCLI = PCCLIENT.CODCLI(+) " +
                                        "AND PCPRECOPROM.CODREDE = PCREDECLIENTE.CODREDE(+) " +
                                        "AND PCPRODUT.CODFORNEC = PCFORNEC.CODFORNEC " +
                                        "AND TRUNC(SYSDATE) BETWEEN PCPRECOPROM.DTINICIOVIGENCIA AND PCPRECOPROM.DTFIMVIGENCIA " +
                                        "AND PCPRECOPROM.NUMREGIAO = :numregicao " +
                                        "AND PCPRODUT.CODPROD=:codprod " +
                                    "ORDER BY PCPRODUT.DESCRICAO")
    public PrecoPromocional buscaPrecosPromocionais(
            @Param("codprod") Long codprod, 
            @Param("numregiao") Long numregiao);
}
