/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.models.EstoqueDisponivel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author carlos
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {
    
    public List<Produto> findByCodprodIn(List<Long> codprods);
    @Query(nativeQuery = true, value = "select pkg_estoque.estoque_disponivel(:codprod, :codfilial, 'V') estoque from dual")
    public EstoqueDisponivel buscaEstoqueDisponivel(@Param("codprod") Long codprod, @Param("codfilial") String codiflial);
    
    
}
