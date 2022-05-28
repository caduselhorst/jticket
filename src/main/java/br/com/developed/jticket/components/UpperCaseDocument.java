package br.com.developed.jticket.components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class UpperCaseDocument extends PlainDocument {
    
    private int maxLength;
    private boolean somenteNumeros;
    
    public UpperCaseDocument() {
        maxLength = -1;
        somenteNumeros = false;
    }
    public UpperCaseDocument(int maxLength) {
        this.maxLength = maxLength;
    }
    
    public UpperCaseDocument(boolean somenteNumeros) {
        maxLength = -1;
        this.somenteNumeros = somenteNumeros;
    }
    
    public UpperCaseDocument(int maxLength, boolean somenteNumeros) {
        this.maxLength = maxLength;
        this.somenteNumeros = somenteNumeros;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        
        
        
        if(maxLength == -1) {
            if(somenteNumeros) {
                if(!Character.isDigit(str.charAt(0))) {
                    return;
                }
            }
            super.insertString(offs, str.toUpperCase(), a);
        } else {
            String stringAntiga = getText (0, getLength() );  
            int tamanhoNovo = stringAntiga.length() + str.length(); 

            if(tamanhoNovo <= maxLength) {
                if(somenteNumeros) {
                    if(!Character.isDigit(str.charAt(0))) {
                        return;
                    }
                }
                super.insertString(offs, str.toUpperCase(), a);
            } else {
                if(somenteNumeros) {
                    if(!Character.isDigit(str.charAt(0))) {
                        return;
                    }
                }
                super.insertString(offs, "", a);
            }
        }
    }
    
}