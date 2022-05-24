/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.specs;

import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.models.FiltroProduto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author carlos
 */
public class ProdutoSpec {
    
    public static Specification<Produto> filtro(FiltroProduto filtro) {
        return (root, query, builder) -> {
            
            List<Predicate> predicates = new ArrayList<>();
            
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
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("departamento"));
                filtro.getDepartamentos().stream().map(d -> d.getCodepto()).forEach(l -> inClause.value(l));
                predicates.add(inClause);
            }
            
            if(filtro.getSecoes() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codsec"));
                filtro.getSecoes().stream().map(s -> s.getCodsec()).forEach(l -> inClause.value(l));
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
            
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
    
}
