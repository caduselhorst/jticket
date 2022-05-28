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
    private Double qtdEstoque;
    private String tipo;
    private String personalizado0;
    private String personalizado1;
    private String personalizado2;
    private String personalizado3;
    private String personalizado4;
    private String personalizado5;
    private String personalizado6;
    private String personalizado7;
    private String personalizado8;
    private String personalizado9;
    private String personalizado10;
    private String personalizado11;
    
    
    @Override
    public String toString() {
        String linha = "%d %d%s%s%s%s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s,";
        
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
                "23 0 |" + (precoItemUnitario != 0.0 ? String.format("%.2f", precoItemUnitario).replace(",", "") : "0") + "|",
                "45 0 |" + (precoItemMaster != 0.0 ? String.format("%.2f", precoItemMaster).replace(",", "") : "0") + "|",
                "25 0 |" + (precoItemUnitarioPromo != 0.0 ? String.format("%.2f", precoItemUnitarioPromo).replace(",", "") : "0") + "|",
                "46 0 |" + (precoItemMasterPromo != 0.0 ? String.format("%.2f", precoItemMasterPromo).replace(",", "") : "0") + "|",
                "5 0 |" + departamento + "|",
                "70 0 |" + unidade + "|",
                "50 0 |" + String.format("%.2f", qtdEstoque).replace(",", "") + "|",
                "121 0 |" + tipo + "|",
                "200 0 |" + personalizado0 + "|", 
                "201 0 |" + personalizado1 + "|", 
                "202 0 |" + personalizado2 + "|", 
                "203 0 |" + personalizado3 + "|", 
                "204 0 |" + personalizado4 + "|", 
                "205 0 |" + personalizado5 + "|", 
                "206 0 |" + personalizado6 + "|", 
                "207 0 |" + personalizado7 + "|", 
                "208 0 |" + personalizado8 + "|", 
                "209 0 |" + personalizado9 + "|", 
                "210 0 |" + personalizado10 + "|", 
                "211 0 |" + personalizado11 + "|" 
                );
                
        return retorno;
    }
    
}
