/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.specs;

import br.com.developed.jticket.entities.Embalagem;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.entities.TabelaPreco;
import br.com.developed.jticket.models.FiltroProduto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author carlos
 */
public class ProdutoSpec {
    
    public static Specification<Produto> filtro(FiltroProduto filtro) {
        return (root, query, builder) -> {
            
            List<Predicate> predicates = new ArrayList<>();
            
            //---------------- Subquery para consulta produtos com precos alterados por data na rotina 201
            List<Predicate> tabelaPrecosPredicates = new ArrayList<>();
            Subquery<TabelaPreco>  tabelaPrecoSubQuery = query.subquery(TabelaPreco.class);
            Root<TabelaPreco> tabelaPrecoRoot = tabelaPrecoSubQuery.from(TabelaPreco.class);
            
            tabelaPrecosPredicates.add(builder.greaterThanOrEqualTo(tabelaPrecoRoot.get("dtultaltpvenda"), filtro.getDataInicioAlteracaoPreco()));
            tabelaPrecosPredicates.add(builder.lessThanOrEqualTo(tabelaPrecoRoot.get("dtultaltpvenda"), filtro.getDataFimAlteracaoPreco()));
            tabelaPrecosPredicates.add(builder.equal(tabelaPrecoRoot.get("numregiao"), filtro.getRegiao()));
            
            tabelaPrecoSubQuery.select(tabelaPrecoRoot.get("codprod"));
            tabelaPrecoSubQuery.where(builder.and(tabelaPrecosPredicates.toArray(new Predicate[]{})));
            //----------------------------------------------------------------------------------------------
            
            //--------------- Subquery consulta produtos com preços alterados por data na rotina 2017
            List<Predicate> tabelaPrecos2017Predicates = new ArrayList<>();
            Subquery<Embalagem>  tabelaPreco2017SubQuery = query.subquery(Embalagem.class);
            Root<Embalagem> tabelaPreco2017Root = tabelaPreco2017SubQuery.from(Embalagem.class);
            
            tabelaPrecos2017Predicates.add(builder.greaterThanOrEqualTo(tabelaPreco2017Root.get("dtultaltpvenda"), filtro.getDataInicioAlteracaoPreco()));
            tabelaPrecos2017Predicates.add(builder.lessThanOrEqualTo(tabelaPreco2017Root.get("dtultaltpvenda"), filtro.getDataFimAlteracaoPreco()));
            tabelaPrecos2017Predicates.add(builder.equal(tabelaPreco2017Root.get("codfilial"), filtro.getCodfilial()));
            tabelaPrecos2017Predicates.add(builder.isNull(tabelaPreco2017Root.get("dtinativo")));
            
            tabelaPreco2017SubQuery.select(tabelaPreco2017Root.get("codprod"));
            tabelaPreco2017SubQuery.where(builder.and(tabelaPrecos2017Predicates.toArray(new Predicate[]{})));
            
            //----------------------------------------------------------------------------------------------
            
            predicates.add(builder.isNull(root.get("dtexclusao")));
            
            if(filtro.getCodigo() != null) {
                predicates.add(builder.equal(root.get("codprod"), filtro.getCodigo()));
            }
            
            if(filtro.getDescricao() != null) {
                predicates.add(builder.like(root.get("descricao"), "%" + filtro.getDescricao() + "%"));
            }
            
            if(filtro.getFornecedor() != null) {
                predicates.add(builder.equal(root.get("codfornec"), filtro.getFornecedor().getCodfornec()));
            }
            
            if(filtro.getDepartamentos() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codepto"));
                filtro.getDepartamentos().stream().map(d -> d.getCodepto()).forEach(l -> inClause.value(l));
                predicates.add(inClause);
            }
            
            if(filtro.getSecoes() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codsec"));
                filtro.getSecoes().stream().map(s -> s.getCodsec()).filter(l -> l != 0).forEach(l -> inClause.value(l));
                predicates.add(inClause);
            }
            
            if(filtro.getCategorias() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codcategoria"));
                filtro.getCategorias().stream().map(c -> c.getCodcategoria()).forEach(l -> inClause.value(l));
                predicates.add(inClause);
            }
            
            if(filtro.getSubcategorias() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codsubcategoria"));
                filtro.getSubcategorias().stream().map(c -> c.getCodsubcategoria()).forEach(l -> inClause.value(l));
                predicates.add(inClause);
            }
            
            if(filtro.getProdutos() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codprod"));
                filtro.getProdutos().stream().map(p -> p.getCodprod()).forEach(l -> inClause.value(l));
                predicates.add(inClause);
            }
            
            if(filtro.getCodigos() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codprod"));
                filtro.getCodigos().forEach(c -> inClause.value(c));
                predicates.add(inClause);
            }
            
            if(filtro.getTipoPreco() == 201) {
                predicates.add(builder.and(root.get("codprod").in(tabelaPrecoSubQuery)));
            }
                
            if(filtro.getTipoPreco() == 2017) {
                predicates.add(builder.and(root.get("codprod").in(tabelaPreco2017SubQuery)));
            }
            return builder.and(predicates.toArray(new Predicate[]{}));

        };
    }
    
}
