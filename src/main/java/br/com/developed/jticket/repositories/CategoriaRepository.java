/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.CategoriaId;
import br.com.developed.jticket.entities.Secao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author carlos
 */
public interface CategoriaRepository extends JpaRepository<Categoria, CategoriaId> {
    
    public List<Categoria> findByCodsecIn(Long[] codsecs);
    
}
