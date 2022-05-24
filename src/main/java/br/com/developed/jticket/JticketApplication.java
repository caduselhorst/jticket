package br.com.developed.jticket;

import br.com.developed.jticket.forms.FormPrincipal;
import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class JticketApplication implements CommandLineRunner {
    
    private static ApplicationContext ctx;

    public static void main(String[] args) {
        //SpringApplication.run(JticketApplication.class, args);
        ctx = new SpringApplicationBuilder(JticketApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        
        UIManager.installLookAndFeel("flat", "com.formdev.flatlaf.FlatLightLaf");
        UIManager.installLookAndFeel("flatdark", "com.formdev.flatlaf.FlatDarkLaf");
        
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormPrincipal(ctx).setVisible(true);
            }
        });
    }

}
