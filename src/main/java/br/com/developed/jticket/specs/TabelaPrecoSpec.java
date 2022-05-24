/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.specs;

import br.com.developed.jticket.entities.TabelaPreco;
import br.com.developed.jticket.models.FiltroTabelaPreco;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author carlos
 */
public class TabelaPrecoSpec {
    
    public static Specification<TabelaPreco> filtro(FiltroTabelaPreco filtro) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if(filtro.getDataAlteracaoFinal() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dtultaltpvenda"), filtro.getDataAlteracaoFinal()));
            }
            
            if(filtro.getDataAlteracaoInicial() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dtultaltpvenda"), filtro.getDataAlteracaoInicial()));
            }
            
            if(filtro.getRegiao() != null) {
                predicates.add(builder.equal(root.get("numregiao"), filtro.getRegiao().getNumregiao()));
            }
            
            if(filtro.getCodProds() != null) {
                CriteriaBuilder.In<Long> inClause = builder.in(root.get("codprod"));
                filtro.getCodProds().forEach(c -> inClause.value(c));
                predicates.add(inClause);
            }
            
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
    
}
