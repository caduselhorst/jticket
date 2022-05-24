/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.ProdutoFilial;
import br.com.developed.jticket.entities.ProdutoFilialId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author carlos
 */
public interface ProdutoFilialRepository extends JpaRepository<ProdutoFilial, ProdutoFilialId> {
    
}
