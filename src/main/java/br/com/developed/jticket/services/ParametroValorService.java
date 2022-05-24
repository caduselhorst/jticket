/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.services;

import br.com.developed.jticket.constraints.TipoPreco;
import br.com.developed.jticket.entities.ParametroValor;
import br.com.developed.jticket.entities.ParametroValorId;
import br.com.developed.jticket.exceptions.NegocioException;
import br.com.developed.jticket.repositories.ParametroValorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlos
 */
@Service
public class ParametroValorService {
    
    private static final String CON_UTILIZAVENDAPOREMBALAGEM = "CON_UTILIZAVENDAPOREMBALAGEM";
    private static final String CON_PRECOPOREMBALAGEM = "CON_PRECOPOREMBALAGEM";
    private static final String MSG_TIPO_VENDA_INDETERMINADO = "Não foi possível determinar o tipo de preço. "
            + "Para pesquisa de alteração de preço geração do arquivo para integraçã PRIX Toledo será utilizado "
            + "como preço a rotina 201 (pctabpr)";
    
    @Autowired
    private ParametroValorRepository repository;
    
    
    public TipoPreco verificaTipoPreco() {
        
        ParametroValor paramUtilizaVendaPorEmbalagem = repository.findById(new ParametroValorId(CON_UTILIZAVENDAPOREMBALAGEM, "99"))
                .orElseThrow(() -> new NegocioException("Parâmetro não encontrado no Winthor"));
        
        ParametroValor paramPrecoPorEmbalagem = repository.findById(new ParametroValorId(CON_UTILIZAVENDAPOREMBALAGEM, "99"))
                .orElseThrow(() -> new NegocioException("Parâmetro não encontrado no Winthor"));
        
        if(paramUtilizaVendaPorEmbalagem.getValor().equals("S") && paramPrecoPorEmbalagem.getValor().equals("S")) {
            return TipoPreco.EMBALAGEM;
        } else {
            if(paramUtilizaVendaPorEmbalagem.getValor().equals("N") && paramPrecoPorEmbalagem.getValor().equals("N")) {
                return TipoPreco.PRODUTO;
            } else {
                throw new NegocioException(MSG_TIPO_VENDA_INDETERMINADO);
            }
        }
        
    }
    
}
