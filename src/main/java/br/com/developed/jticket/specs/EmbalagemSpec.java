/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.specs;

import br.com.developed.jticket.entities.Embalagem;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.models.FiltroEmbalagem;
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
public class EmbalagemSpec {
    
    public static Specification<Embalagem> filtro(FiltroEmbalagem filtro) {
        return (root, query, builder) -> {
            
            List<Predicate> predicates = new ArrayList<>();
            
            if(filtro.getCodauxiliar() != null) {
                predicates.add(builder.equal(root.get("codauxiliar"), filtro.getCodauxiliar()));
            }
            
            if(filtro.getCodfilial() != null) {
                predicates.add(builder.equal(root.get("codfilial"), filtro.getCodfilial()));
            }
            
            if(filtro.getCodproduto() != null) {
                predicates.add(builder.equal(root.get("codprod"), filtro.getCodproduto()));
            }
            
            if(filtro.getDataAlteracaoInicio() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dtultaltpvenda"), filtro.getDataAlteracaoInicio()));
            }
            
            if(filtro.getDataAlteracaoFim() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dtultaltpvenda"), filtro.getDataAlteracaoFim()));
            }
            
            predicates.add(builder.isNull(root.get("dtinativo")));
            
            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
    
}
