/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author carlos
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroToledo {
    
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private Integer codigo;
    private Long codBarrasPrincipal;
    private List<Long> codBarras;
    private String descricao;
    private Date dataHoraAlteracaoEtiqueta;
    private Double precoItemUnitario;
    private Double precoItemMaster;
    private Double precoItemUnitarioPromo;
    private Double precoItemMasterPromo;
    private String departamento;
    private String unidade;
    private Integer qtdEstoque;
    private Boolean promocao;
    
    @Override
    public String toString() {
        String linha = "%d %d%s%s%s%s %s %s %s %s %s %s %s";
        
        String codBarrasAux = " ";
        String dtAlteracao = " ";
        
        if(codBarras != null) {
            codBarrasAux = " 9510 0 |" + codBarras.stream().map(String::valueOf).collect(Collectors.joining(" ")) + "| ";
        }
        
        if(dataHoraAlteracaoEtiqueta != null) {
            dtAlteracao = " 9500 0 |" + SDF.format(dataHoraAlteracaoEtiqueta) + "| ";
            codBarrasAux = " ";
        }
        
        String retorno = String.format(linha, 
                codigo, 
                codBarrasPrincipal,
                codBarrasAux,
                "7 0 |" + descricao + "|",
                dtAlteracao,
                "23 0 |" + String.format("%.2f", precoItemUnitario).replace(",", "") + "|",
                "45 0 |" + String.format("%.2f", precoItemMaster).replace(",", "") + "|",
                "25 0 |" + String.format("%.2f", precoItemUnitarioPromo).replace(",", "") + "|",
                "46 0 |" + String.format("%.2f", precoItemMasterPromo).replace(",", "") + "|",
                "5 0 |" + departamento + "|",
                "70 0 |" + unidade + "|",
                "50 0 |" + qtdEstoque + "|",
                "121 0 |" + (promocao ? "PROMO" : "NORMAL") + "|"
                );
                
        return retorno;
    }
    
}
