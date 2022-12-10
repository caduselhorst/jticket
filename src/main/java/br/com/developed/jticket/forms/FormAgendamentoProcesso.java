/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package br.com.developed.jticket.forms;

import br.com.developed.jticket.components.ComboBoxHoraMinutoModel;
import br.com.developed.jticket.formatters.HoraMaskFormatter;
import br.com.developed.jticket.models.PreferenciasAgendamento;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultFormatterFactory;

/**
 *
 * @author carlos
 */
public class FormAgendamentoProcesso extends javax.swing.JDialog {

    /**
     * Creates new form FormAgendamentoProcesso
     */
    public FormAgendamentoProcesso(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configuraSairESC();
        carregaPreferencias();
    }
    
    public void configuraSairESC() {
        JRootPane meurootpane = getRootPane();
        meurootpane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
        
        meurootpane.getRootPane().getActionMap().put("ESCAPE", new AbstractAction("ESCAPE") {
            public void actionPerformed(ActionEvent e) {
                jButton2ActionPerformed(e);
            }
        });   
    }
    
    private void carregaPreferencias() {
        PreferenciasAgendamento pa = new PreferenciasAgendamento().carregarPreferencias();
        
        if(pa != null) {
            jRadioButton1.setSelected(!pa.isRecorrente());
            jRadioButton2.setSelected(pa.isRecorrente());

            jCheckBox1.setSelected(pa.isSeg());
            jCheckBox2.setSelected(pa.isTer());
            jCheckBox3.setSelected(pa.isQua());
            jCheckBox4.setSelected(pa.isQui());
            jCheckBox5.setSelected(pa.isSex());
            jCheckBox6.setSelected(pa.isSab());
            jCheckBox7.setSelected(pa.isDom());

            jFormattedTextField1.setText(pa.getHorarioSeg());
            jFormattedTextField2.setText(pa.getHorarioTer());
            jFormattedTextField3.setText(pa.getHorarioQua());
            jFormattedTextField4.setText(pa.getHorarioQui());
            jFormattedTextField5.setText(pa.getHorarioSex());
            jFormattedTextField6.setText(pa.getHorarioSab());
            jFormattedTextField7.setText(pa.getHorarioDom());

            jSpinner1.getModel().setValue(pa.getQuantidade());
            jComboBox1.setSelectedIndex(pa.getIndexHoraMinuto());
            jFormattedTextField8.setText(pa.getApartirDe());


            jCheckBox1.setEnabled(!pa.isRecorrente());
            jCheckBox2.setEnabled(!pa.isRecorrente());
            jCheckBox3.setEnabled(!pa.isRecorrente());
            jCheckBox4.setEnabled(!pa.isRecorrente());
            jCheckBox5.setEnabled(!pa.isRecorrente());
            jCheckBox6.setEnabled(!pa.isRecorrente());
            jCheckBox7.setEnabled(!pa.isRecorrente());
            jFormattedTextField1.setEnabled(!pa.isRecorrente());
            jFormattedTextField2.setEnabled(!pa.isRecorrente());
            jFormattedTextField3.setEnabled(!pa.isRecorrente());
            jFormattedTextField4.setEnabled(!pa.isRecorrente());
            jFormattedTextField5.setEnabled(!pa.isRecorrente());
            jFormattedTextField6.setEnabled(!pa.isRecorrente());
            jFormattedTextField7.setEnabled(!pa.isRecorrente());

            jSpinner1.setEnabled(pa.isRecorrente());
            jComboBox1.setEnabled(pa.isRecorrente());
            jFormattedTextField8.setEnabled(pa.isRecorrente());
        }
        
    }
    
    private boolean valida() {
        
        if(!jRadioButton1.isSelected() && !jRadioButton2.isSelected()) {
            JOptionPane.showMessageDialog(this, "Informe um tipo de agendamento", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
        
        if(jRadioButton1.isSelected()) {
            // agendamento
            
            // valida nenhum dia da semana  informado
            if(!jCheckBox1.isSelected() && !jCheckBox2.isSelected() && !jCheckBox3.isSelected() && !jCheckBox4.isSelected()
                    && !jCheckBox5.isSelected() && !jCheckBox6.isSelected() && !jCheckBox7.isSelected()) {
                JOptionPane.showMessageDialog(this, 
                        "Selecione ao menos um dia da semana", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            // valida dia marcado sem horário ou horário sem dia marcado seg
            if(jCheckBox1.isSelected() && jFormattedTextField1.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_DIA_MARCADO_SEM_HORARIO, "para a Segunda-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(!jCheckBox1.isSelected() && !jFormattedTextField1.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO, "na Segunda-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            // valida dia marcado sem horário ou horário sem dia marcado ter
            if(jCheckBox2.isSelected() && jFormattedTextField2.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_DIA_MARCADO_SEM_HORARIO, "para a Terça-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(!jCheckBox2.isSelected() && !jFormattedTextField2.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO, "na Terça-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            // valida dia marcado sem horário ou horário sem dia marcado qua
            if(jCheckBox3.isSelected() && jFormattedTextField3.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_DIA_MARCADO_SEM_HORARIO, "para a Quarta-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(!jCheckBox3.isSelected() && !jFormattedTextField3.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO, "na Quarta-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            // valida dia marcado sem horário ou horário sem dia marcado qui
            if(jCheckBox4.isSelected() && jFormattedTextField4.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_DIA_MARCADO_SEM_HORARIO, "para a Quinta-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(!jCheckBox4.isSelected() && !jFormattedTextField4.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO, "na Quinta-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            // valida dia marcado sem horário ou horário sem dia marcado sex
            if(jCheckBox5.isSelected() && jFormattedTextField5.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_DIA_MARCADO_SEM_HORARIO, "para a Sexta-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(!jCheckBox5.isSelected() && !jFormattedTextField5.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO, "na Sexta-feira"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            // valida dia marcado sem horário ou horário sem dia marcado sab
            if(jCheckBox6.isSelected() && jFormattedTextField6.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_DIA_MARCADO_SEM_HORARIO, "para o Sábado"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(!jCheckBox6.isSelected() && !jFormattedTextField6.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO, "no Sábado"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            // valida dia marcado sem horário ou horário sem dia marcado dom
            if(jCheckBox7.isSelected() && jFormattedTextField7.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_DIA_MARCADO_SEM_HORARIO, "para o Domingo"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(!jCheckBox7.isSelected() && !jFormattedTextField7.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, 
                        String.format(MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO, "no Domingo"), "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } else {
            // para execução recorrente
            if((Integer) jSpinner1.getValue() <= 0) {
                JOptionPane.showMessageDialog(this, "Informe o intervalo de tempo", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            if(jComboBox1.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Informe o tipo de intervalo", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            if(jFormattedTextField8.getText().equals("  :  :  ")) {
                JOptionPane.showMessageDialog(this, "Informe o horário de inicio de execução", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
        
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Agendamento de execução automática");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Agendado");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Segunda-feira");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Terça-feira");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Quarta-feira");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("Quinta-feira");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox5.setText("Sexta-feira");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jCheckBox6.setText("Sábado");
        jCheckBox6.setToolTipText("");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jCheckBox7.setText("Domingo");
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        jFormattedTextField1.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField1.setEnabled(false);

        jFormattedTextField2.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField2.setEnabled(false);

        jFormattedTextField3.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField3.setEnabled(false);

        jFormattedTextField4.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField4.setEnabled(false);

        jFormattedTextField5.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField5.setEnabled(false);

        jFormattedTextField6.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField6.setEnabled(false);

        jFormattedTextField7.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField7.setEnabled(false);

        jRadioButton2.setText("Recorrente");
        jRadioButton2.setToolTipText("");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("A cada");

        jSpinner1.setEnabled(false);

        jComboBox1.setModel(new ComboBoxHoraMinutoModel());
        jComboBox1.setEnabled(false);

        jLabel2.setText("a partir de");

        jFormattedTextField8.setFormatterFactory(new DefaultFormatterFactory(HoraMaskFormatter.getInstance()));
        jFormattedTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField8.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(106, 106, 106)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox3)
                                    .addComponent(jCheckBox2)
                                    .addComponent(jCheckBox4)
                                    .addComponent(jCheckBox5)
                                    .addComponent(jCheckBox6)
                                    .addComponent(jCheckBox7))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jFormattedTextField5)
                            .addComponent(jFormattedTextField4)
                            .addComponent(jFormattedTextField3)
                            .addComponent(jFormattedTextField2)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField7)
                            .addComponent(jFormattedTextField6)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jCheckBox1)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel1)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setMnemonic('G');
        jButton1.setText("Gravar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setMnemonic('C');
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        if (((JRadioButton) evt.getSource()).isSelected()) {
            jCheckBox1.setEnabled(true);
            jCheckBox2.setEnabled(true);
            jCheckBox3.setEnabled(true);
            jCheckBox4.setEnabled(true);
            jCheckBox5.setEnabled(true);
            jCheckBox6.setEnabled(true);
            jCheckBox7.setEnabled(true);
            
            jSpinner1.getModel().setValue(0);
            jSpinner1.setEnabled(false);
            jComboBox1.setSelectedIndex(-1);
            jComboBox1.setEnabled(false);
            jFormattedTextField8.setText(null);
            jFormattedTextField8.setEnabled(false);
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        if (((JRadioButton) evt.getSource()).isSelected()) {
            jCheckBox1.setEnabled(false);
            jCheckBox1.setSelected(false);
            jCheckBox2.setEnabled(false);
            jCheckBox2.setSelected(false);
            jCheckBox3.setEnabled(false);
            jCheckBox3.setSelected(false);
            jCheckBox4.setEnabled(false);
            jCheckBox4.setSelected(false);
            jCheckBox5.setEnabled(false);
            jCheckBox5.setSelected(false);
            jCheckBox6.setEnabled(false);
            jCheckBox6.setSelected(false);
            jCheckBox7.setEnabled(false);
            jCheckBox7.setSelected(false);
            jFormattedTextField1.setEnabled(false);
            jFormattedTextField1.setText(null);
            jFormattedTextField2.setEnabled(false);
            jFormattedTextField2.setText(null);
            jFormattedTextField3.setEnabled(false);
            jFormattedTextField3.setText(null);
            jFormattedTextField4.setEnabled(false);
            jFormattedTextField4.setText(null);
            jFormattedTextField5.setEnabled(false);
            jFormattedTextField5.setText(null);
            jFormattedTextField6.setEnabled(false);
            jFormattedTextField6.setText(null);
            jFormattedTextField7.setEnabled(false);
            jFormattedTextField7.setText(null);
            
            jSpinner1.setEnabled(true);
            jComboBox1.setEnabled(true);
            jFormattedTextField8.setEnabled(true);
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(valida()) {
            PreferenciasAgendamento pa = PreferenciasAgendamento.builder()
                    .recorrente(jRadioButton2.isSelected())
                    .apartirDe(jFormattedTextField8.getText())
                    .indexHoraMinuto(jComboBox1.getSelectedIndex())
                    .quantidade((Integer) jSpinner1.getModel().getValue())
                    .seg(jCheckBox1.isSelected())
                    .horarioSeg(jFormattedTextField1.getText())
                    .ter(jCheckBox2.isSelected())
                    .horarioTer(jFormattedTextField2.getText())
                    .qua(jCheckBox3.isSelected())
                    .horarioQua(jFormattedTextField3.getText())
                    .qui(jCheckBox4.isSelected())
                    .horarioQui(jFormattedTextField4.getText())
                    .sex(jCheckBox5.isSelected())
                    .horarioSex(jFormattedTextField5.getText())
                    .sab(jCheckBox6.isSelected())
                    .horarioSab(jFormattedTextField6.getText())
                    .dom(jCheckBox7.isSelected())
                    .horarioDom(jFormattedTextField7.getText())
                    .build();
            
            pa.salvarPreferencias();
            
            JOptionPane.showMessageDialog(this, "Preferências gravadas com sucesso.", "Informaçao", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        habilitaDesabilitaCampo(jCheckBox1, jFormattedTextField1);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        habilitaDesabilitaCampo(jCheckBox2, jFormattedTextField2);
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        habilitaDesabilitaCampo(jCheckBox3, jFormattedTextField3);
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        habilitaDesabilitaCampo(jCheckBox4, jFormattedTextField4);
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        habilitaDesabilitaCampo(jCheckBox5, jFormattedTextField5);
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        habilitaDesabilitaCampo(jCheckBox6, jFormattedTextField6);
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed
        habilitaDesabilitaCampo(jCheckBox7, jFormattedTextField7);
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    
    private void habilitaDesabilitaCampo(JCheckBox checkBox, JFormattedTextField campo) {
        if(checkBox.isSelected()) {
            campo.setEnabled(true);
        } else {
            campo.setEnabled(false);
            campo.setText(null);
        }
    }
    
    private static final String MSG_DIA_MARCADO_SEM_HORARIO = "Informe um horário para execução %s";
    private static final String MSG_HORARIO_INFORMADO_SEM_DIA_SEMANA_MARCADO = "For informado um horário para execução %s porém o campo não foi selecionado. Selecione ou deixe o horário em branco.";

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables
}
