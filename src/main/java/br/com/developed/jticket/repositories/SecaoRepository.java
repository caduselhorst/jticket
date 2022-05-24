/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.repositories;

import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.entities.Secao;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author carlos
 */
public interface SecaoRepository extends JpaRepository<Secao, Long> {
    
    public List<Secao> findByDepartamentoAndDtexclusao(Departamento departamento, Date dtexclusao);
    public List<Secao> findBydtexclusao(Date dtexclusao);
    
}
