package br.com.developed.jticket.formatters;

import java.text.ParseException;
import javax.swing.text.MaskFormatter;

public class HoraMaskFormatter extends MaskFormatter {
    
    public static MaskFormatter getInstance() {

        String formatos = "##:##:##";
        try {
            MaskFormatter mf = new MaskFormatter(formatos);
            mf.setValidCharacters("0123456789");
            return mf;
        } catch(ParseException e) {
            return null;
        }
 
    }
    
}