/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.Regiao;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author carlos
 */
public interface RegiaoRepository extends JpaRepository<Regiao, Long> {
    
}
