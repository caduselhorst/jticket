/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.utils;

import br.com.developed.jticket.forms.FormPrincipal;
import java.awt.Color;
import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author carlos
 */
@Slf4j
public class LogTelaUtils {
    
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final String MSG_TEMPLATE = "%s - %s\n";
    
    private static final Color MINHA_MSG = Color.BLUE;
    private static final Color CLIENTE_MSG = new Color(255, 140, 0);
    private static final Color ERRO_MSG = Color.RED;
    
    public static void loga(Color color, String mensagem, JTextPane pane) {
        
        try {
            StyledDocument doc = pane.getStyledDocument();
            Style style = pane.getStyle("Style");
            if(style == null)  {
                style = pane.addStyle("Style", null);
            }
            StyleConstants.setForeground(style, color);
            pane.getStyledDocument().insertString(pane.getStyledDocument().getLength(), String.format(MSG_TEMPLATE, SDF.format(new Date()), mensagem), style);
        } catch (BadLocationException e) {
            log.error("Ocorreu um erro ao inserir o log na tela", e);
        }
        
        
    }
        
    public static void logaInfo(String mensage, JTextPane pane) {
        loga(MINHA_MSG, mensage, pane);
    }
    
    public static void logaWarn(String mensage, JTextPane pane) {
        loga(CLIENTE_MSG, mensage, pane);
    }
    
    public static void logaErro(String mensage, JTextPane pane) {
        loga(ERRO_MSG, mensage, pane);
    }
    
}
