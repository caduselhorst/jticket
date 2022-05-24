/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.developed.jticket.utils;

import java.util.Random;

/**
 *
 * @author carlos
 */
public class NumberUtils {
    
    public static Integer getRandomInteger() {
        Random r = new Random();
        
        return r.nextInt(9999);
    }
    
}
