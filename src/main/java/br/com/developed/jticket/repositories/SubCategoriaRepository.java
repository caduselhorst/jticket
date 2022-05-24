/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.SubCategoria;
import br.com.developed.jticket.entities.SubCategoriaId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author carlos
 */
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, SubCategoriaId> {
    
    public List<SubCategoria> findByCodcategoriaIn(Long[] codcategorias);
    
}
