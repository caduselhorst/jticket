/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.Embalagem;
import br.com.developed.jticket.entities.EmbalagemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author carlos
 */
public interface EmbalagemRepository extends JpaRepository<Embalagem, EmbalagemId>, 
        JpaSpecificationExecutor<Embalagem> {
    
}
