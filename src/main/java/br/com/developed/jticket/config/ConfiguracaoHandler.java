/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 *
 * @author carlos
 */
public class ConfiguracaoHandler {
    
    private static ConfiguracaoHandler instance;
    private Properties props;
    
    private ConfiguracaoHandler() throws IOException {
        File cFile = new File("config.properties");
        if(!cFile.exists()) {
            cFile.createNewFile();
        }
        props = new Properties();
        
        props.load(new FileInputStream(cFile));
        
    }
    
    private void load() throws IOException {
        props.load(new FileInputStream(new File("config.properties")));
    }
    
    public static synchronized ConfiguracaoHandler getInstance() throws IOException {
        if(instance == null) {
            instance = new ConfiguracaoHandler();
        } else {
            instance.load();
        }
        return instance;
    }
    
    public void setLaf(String laf) throws IOException {
        if(props.getProperty("app.laf") == null) {
            props.put("app.laf", laf);
        } else {
            props.replace("app.laf", laf);
        }
        props.store(new PrintWriter(new File("config.properties")), null);
    }
    
    public String getLaf() {
        return props.getProperty("app.laf");
    }
}
