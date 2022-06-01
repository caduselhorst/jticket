/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.forms;

import br.com.developed.jticket.components.UpperCaseDocument;
import br.com.developed.jticket.config.ConfiguracaoHandler;
import br.com.developed.jticket.constraints.TipoPreco;
import br.com.developed.jticket.entities.Categoria;
import br.com.developed.jticket.entities.Departamento;
import br.com.developed.jticket.entities.Filial;
import br.com.developed.jticket.entities.Fornecedor;
import br.com.developed.jticket.entities.Produto;
import br.com.developed.jticket.entities.Regiao;
import br.com.developed.jticket.entities.Secao;
import br.com.developed.jticket.entities.SubCategoria;
import br.com.developed.jticket.models.Preferencias;
import br.com.developed.jticket.models.PreferenciasAgendamento;
import br.com.developed.jticket.services.AgendamentoEtiquetaEletronicaService;
import br.com.developed.jticket.services.EtiquetaEletronicaServiceProcess;
import br.com.developed.jticket.services.FilialService;
import br.com.developed.jticket.services.FormularioPrincipalService;
import br.com.developed.jticket.services.ParametroValorService;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author carlos
 */
@Slf4j
public class FormPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form FormPrincipal
     */
    public FormPrincipal(ApplicationContext ctx) {
        this.ctx = ctx;
        this.formService = this.ctx.getBean(FormularioPrincipalService.class);
        initComponents();
        configuraLaf();
        configuraSairESC();
        verificaTipoPreço();
                
        carregaPreferencias();
        
        

    }
        
    public void configuraSairESC() {
        JRootPane meurootpane = getRootPane();
        meurootpane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
        
        meurootpane.getRootPane().getActionMap().put("ESCAPE", new AbstractAction("ESCAPE") {
            public void actionPerformed(ActionEvent e) {
                if(agendamentoService == null || !agendamentoService.isAlive()) {
                    jButtonSairActionPerformed(e);
                }
            }
        });
        
    }

    private void configuraLaf() {

        try {
            log.info("LAF -> " + ConfiguracaoHandler.getInstance().getLaf());
            if (ConfiguracaoHandler.getInstance().getLaf() != null) {
                UIManager.setLookAndFeel(ConfiguracaoHandler.getInstance().getLaf());
            } else {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (IOException e) {
            log.error("Ocorreu um erro ao ler o arquivo de configuração.", e);
        } catch (ClassNotFoundException e) {
            log.error("Classe do LAF não localizado.", e);
        } catch (InstantiationException e) {
            log.error("Ocorreu um erro ao instanciar a classe", e);
        } catch (IllegalAccessException e) {
            log.error("Erro ao carregar o LAF", e);
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Erro ao carregar o LAF", e);
        }
    }

    private void verificaTipoPreço() {
        ParametroValorService service = ctx.getBean(ParametroValorService.class);
        TipoPreco tipoPreco = service.verificaTipoPreco();
        if (tipoPreco.equals(TipoPreco.PRODUTO)) {
            jLabel10.setText("Precificação por Produto (Rotina 201)");
        } else {
            jLabel10.setText("Precificação por embalagem (Rotina 2017)");
        }
    }
    
    private void carregaPreferencias() {
        Preferencias prefs = new Preferencias();
        
        prefs = prefs.carregarPreferencias();
        
        if(prefs != null) {
            if(prefs.getFilial() != null) {
                this.filial = prefs.getFilial();
                jTextFieldFilial.setText(String.format("%s - %s", this.filial.getCodigo(), this.filial.getRazaosocial()));
                jTextField2.setText(filial.getCodigo());
            }

            if(prefs.getRegiao() != null) {
                regiao = prefs.getRegiao();
                jTextFieldArea.setText(String.format("%d - %s", regiao.getNumregiao(), regiao.getRegiao()));
                jTextField3.setText(String.valueOf(regiao.getNumregiao()));
            }
            
            if(prefs.getDepartamentos() != null) {
                departamentos = prefs.getDepartamentos();
                configuraCampoDepartamentos();
            }
            
            if(prefs.getSecoes() != null) {
                secoes = prefs.getSecoes();
                configuraCampoSecao();
            }
            
            if(prefs.getCategorias() != null) {
                categorias = prefs.getCategorias();
                configuraCampoCategoria();
            }
            
            if(prefs.getSubCategorias() != null) {
                subCategorias = prefs.getSubCategorias();
                configuraCampoSubCategoria();
            }
            
            if(prefs.getProdutos() != null) {
                produtos = prefs.getProdutos();
                configuraCampoProduto();
            }
            
            if(prefs.getFornecedor() != null) {
                fornecedor = prefs.getFornecedor();
                jTextFieldFornecedor.setText(String.format("%d - %s", fornecedor.getCodfornec(), 
                        fornecedor.getFornecedor()));
                jTextField9.setText(String.valueOf(fornecedor.getCodfornec()));
            }
            
            jCheckBoxEstoquePositivo.setSelected(prefs.isSomenteEstoquePositivo());
            jTextField1.setText(prefs.getCaminhoRepositorioArquivos());
            
            jButtonProcessar.setEnabled(true);
        }
        
        
        preferenciasAgendamento = new PreferenciasAgendamento().carregarPreferencias();
        
        if(preferenciasAgendamento == null) {
            jLabel11.setText("Parâmetros para processamento automático não configurados");
        } else {
            jLabel11.setText("Parâmetros para processamento automático configurados");
        }
            
    }
        
    private void configuraCampoProduto() {
        if(produtos != null && produtos.size() >= 1) {
            if(produtos.size() == 1) {
                jTextFieldProduto.setText(String.format("%d - %s", produtos.get(0).getCodprod(), 
                        produtos.get(0).getDescricao()));
                jTextField8.setText(String.valueOf(produtos.get(0).getCodprod()));
            } else {
                jTextFieldProduto.setText("MÚLTIPLOS");
                jTextField8.setText("MÚLTIPLOS");
            }
        }
    }
    
    private void configuraCampoSubCategoria() {
        if(subCategorias != null && subCategorias.size() >= 1) {
            if(categorias.size() == 1) {
                jTextFieldSubCategoria.setText(String.format("%d - %s", subCategorias.get(0).getCodsubcategoria(), 
                        subCategorias.get(0).getSubcategoria()));
            } else {
                jTextFieldSubCategoria.setText("MÚLTIPLOS");
            }
        }
    }
    
    private void configuraCampoCategoria() {
        if(categorias != null && categorias.size() >= 1) {
            if(categorias.size() == 1) {
                jTextFieldCategoria.setText(String.format("%d - %s", categorias.get(0).getCodcategoria(), 
                        categorias.get(0).getCategoria()));
            } else {
                jTextFieldCategoria.setText("MÚLTIPLOS");
            }
        }
    }
    
    private void configuraCampoSecao() {
        if(secoes != null && secoes.size() >= 1) {
            if(secoes.size() == 1) {
                jTextFieldSecao.setText(String.format("%d - %s", secoes.get(0).getCodsec(), 
                        secoes.get(0).getDescricao()));
            } else {
                jTextFieldSecao.setText("MÚLTIPLOS");
            }
        }
    }
    
    private void configuraCampoDepartamentos() {
        if(departamentos != null && departamentos.size() >= 1) {
            if(departamentos.size() == 1) {
                jTextFieldDepartamento.setText(String.format("%d - %s", departamentos.get(0).getCodepto(), 
                        departamentos.get(0).getDescricao()));
                jTextField4.setText(String.valueOf(departamentos.get(0).getCodepto()));
            } else {
                jTextFieldDepartamento.setText("MÚLTIPLOS");
                jTextField4.setText("MÚLTIPLOS");
            }
        }
    }
    
    private void habilitaDesabilitaCampoProcessamentoAutomatico() {
        jTextFieldFilial.setEnabled(!jTextFieldFilial.isEnabled());
        jTextFieldArea.setEnabled(!jTextFieldArea.isEnabled());
        jTextFieldDepartamento.setEnabled(!jTextFieldDepartamento.isEnabled());
        jTextFieldSecao.setEnabled(!jTextFieldSecao.isEnabled());
        jTextFieldProduto.setEnabled(!jTextFieldProduto.isEnabled());
        jTextFieldFornecedor.setEnabled(!jTextFieldFornecedor.isEnabled());
        
        jButtonPesquisaFilial.setEnabled(!jButtonPesquisaFilial.isEnabled());
        jButtonPesquisaRegiao.setEnabled(!jButtonPesquisaRegiao.isEnabled());
        jButtonPesquisaDepartamento.setEnabled(!jButtonPesquisaDepartamento.isEnabled());
        jButtonPesquisaSecao.setEnabled(!jButtonPesquisaSecao.isEnabled());
        jButtonPesquisaCategoria.setEnabled(!jButtonPesquisaCategoria.isEnabled());
        jButtonPesquisaSubCategoria.setEnabled(!jButtonPesquisaSubCategoria.isEnabled());
        jButtonPesquisaProduto.setEnabled(!jButtonPesquisaProduto.isEnabled());
        jButtonPesquisaFornecedor.setEnabled(!jButtonPesquisaFornecedor.isEnabled());
        
        jCheckBoxEstoquePositivo.setEnabled(!jCheckBoxEstoquePositivo.isEnabled());
        jCheckBoxFiltroDataPrecoAlterado.setEnabled(!jCheckBoxFiltroDataPrecoAlterado.isEnabled());
        
        jButtonProcessar.setEnabled(!jButtonProcessar.isEnabled());
        jButtonLimpar.setEnabled(!jButtonLimpar.isEnabled());
        
        jButtonLimpar1.setEnabled(!jButtonLimpar1.isEnabled());
        
        jButtonSair.setEnabled(!jButtonSair.isEnabled());
        
        jButton1.setEnabled(!jButton1.isEnabled());
        jButton2.setEnabled(!jButton2.isEnabled());
        
        jTextField2.setEnabled(!jTextField2.isEnabled());
        jTextField3.setEnabled(!jTextField3.isEnabled());
        jTextField4.setEnabled(!jTextField4.isEnabled());
        jTextField5.setEnabled(!jTextField5.isEnabled());
        jTextField8.setEnabled(!jTextField8.isEnabled());
        jTextField9.setEnabled(!jTextField9.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateLabelFormatter1 = new org.jdatepicker.DateLabelFormatter();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButtonPesquisaDepartamento = new javax.swing.JButton();
        jTextFieldDepartamento = new javax.swing.JTextField();
        jButtonPesquisaFilial = new javax.swing.JButton();
        jTextFieldFilial = new javax.swing.JTextField();
        jButtonPesquisaRegiao = new javax.swing.JButton();
        jTextFieldArea = new javax.swing.JTextField();
        jButtonPesquisaSecao = new javax.swing.JButton();
        jTextFieldSecao = new javax.swing.JTextField();
        jButtonPesquisaCategoria = new javax.swing.JButton();
        jTextFieldCategoria = new javax.swing.JTextField();
        jButtonPesquisaSubCategoria = new javax.swing.JButton();
        jTextFieldSubCategoria = new javax.swing.JTextField();
        jButtonPesquisaProduto = new javax.swing.JButton();
        jTextFieldProduto = new javax.swing.JTextField();
        jButtonPesquisaFornecedor = new javax.swing.JButton();
        jTextFieldFornecedor = new javax.swing.JTextField();
        jCheckBoxFiltroDataPrecoAlterado = new javax.swing.JCheckBox();
        dateChooserCombo3 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo4 = new datechooser.beans.DateChooserCombo();
        jCheckBoxEstoquePositivo = new javax.swing.JCheckBox();
        jButtonProcessar = new javax.swing.JButton();
        jButtonLimpar = new javax.swing.JButton();
        jButtonLimpar1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jButtonSair = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Developed - Ticket - Integrador Toledo.");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Filial");

        jLabel2.setText("Região");

        jLabel3.setText("Departamento");

        jLabel4.setText("Seção");

        jLabel5.setText("Categoria");

        jLabel6.setText("Sub-categoria");

        jLabel7.setText("Produto");

        jLabel8.setText("Fornecedor");

        jButtonPesquisaDepartamento.setText("...");
        jButtonPesquisaDepartamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaDepartamentoActionPerformed(evt);
            }
        });

        jTextFieldDepartamento.setEditable(false);
        jTextFieldDepartamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldDepartamentoKeyPressed(evt);
            }
        });

        jButtonPesquisaFilial.setText("...");
        jButtonPesquisaFilial.setToolTipText("");
        jButtonPesquisaFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaFilialActionPerformed(evt);
            }
        });

        jTextFieldFilial.setEditable(false);
        jTextFieldFilial.setToolTipText("");
        jTextFieldFilial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldFilialKeyPressed(evt);
            }
        });

        jButtonPesquisaRegiao.setText("...");
        jButtonPesquisaRegiao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaRegiaoActionPerformed(evt);
            }
        });

        jTextFieldArea.setEditable(false);
        jTextFieldArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldAreaKeyPressed(evt);
            }
        });

        jButtonPesquisaSecao.setText("...");
        jButtonPesquisaSecao.setToolTipText("");
        jButtonPesquisaSecao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaSecaoActionPerformed(evt);
            }
        });

        jTextFieldSecao.setEditable(false);
        jTextFieldSecao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldSecaoKeyPressed(evt);
            }
        });

        jButtonPesquisaCategoria.setText("...");
        jButtonPesquisaCategoria.setToolTipText("");
        jButtonPesquisaCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaCategoriaActionPerformed(evt);
            }
        });

        jTextFieldCategoria.setEditable(false);
        jTextFieldCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCategoriaKeyPressed(evt);
            }
        });

        jButtonPesquisaSubCategoria.setText("...");
        jButtonPesquisaSubCategoria.setToolTipText("");
        jButtonPesquisaSubCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaSubCategoriaActionPerformed(evt);
            }
        });

        jTextFieldSubCategoria.setEditable(false);
        jTextFieldSubCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldSubCategoriaKeyPressed(evt);
            }
        });

        jButtonPesquisaProduto.setText("...");
        jButtonPesquisaProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaProdutoActionPerformed(evt);
            }
        });

        jTextFieldProduto.setEditable(false);
        jTextFieldProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldProdutoKeyPressed(evt);
            }
        });

        jButtonPesquisaFornecedor.setText("...");
        jButtonPesquisaFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaFornecedorActionPerformed(evt);
            }
        });

        jTextFieldFornecedor.setEditable(false);
        jTextFieldFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldFornecedorKeyPressed(evt);
            }
        });

        jCheckBoxFiltroDataPrecoAlterado.setText("Filtrar produtos com preços alterados no período");
        jCheckBoxFiltroDataPrecoAlterado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFiltroDataPrecoAlteradoActionPerformed(evt);
            }
        });

        dateChooserCombo3.setEnabled(false);
        dateChooserCombo3.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);

        dateChooserCombo4.setEnabled(false);
        dateChooserCombo4.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);

        jCheckBoxEstoquePositivo.setText("Considerar somente produtos com estoque maior que zero");
        jCheckBoxEstoquePositivo.setToolTipText("");

        jButtonProcessar.setMnemonic('P');
        jButtonProcessar.setText("Processar");
        jButtonProcessar.setToolTipText("Executa o processamento");
        jButtonProcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProcessarActionPerformed(evt);
            }
        });

        jButtonLimpar.setMnemonic('L');
        jButtonLimpar.setText("Limpar");
        jButtonLimpar.setToolTipText("Limpa os filtros para um novo processamento");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonLimpar1.setMnemonic('L');
        jButtonLimpar1.setText("Salvar prefer.");
        jButtonLimpar1.setToolTipText("Limpa os filtros para um novo processamento");
        jButtonLimpar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpar1ActionPerformed(evt);
            }
        });

        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
        });

        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });

        jTextField6.setEnabled(false);
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField6KeyPressed(evt);
            }
        });

        jTextField7.setEnabled(false);
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField7KeyPressed(evt);
            }
        });

        jTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField8FocusLost(evt);
            }
        });
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField8KeyPressed(evt);
            }
        });

        jTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField9FocusLost(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField9KeyPressed(evt);
            }
        });

        jCheckBox1.setText("Processamento automático");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton2.setText("Configurar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(0, 0, 255));
        jLabel11.setText("jLabel11");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxEstoquePositivo, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonPesquisaSubCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPesquisaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPesquisaFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonPesquisaSecao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonPesquisaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonPesquisaDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonPesquisaFilial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonPesquisaRegiao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldProduto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldSubCategoria, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldCategoria, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldSecao, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldArea, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldDepartamento)
                            .addComponent(jTextFieldFornecedor)
                            .addComponent(jTextFieldFilial, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxFiltroDataPrecoAlterado)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateChooserCombo4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButtonProcessar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonLimpar1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButtonPesquisaFilial)
                    .addComponent(jTextFieldFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButtonPesquisaRegiao)
                    .addComponent(jTextFieldArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButtonPesquisaDepartamento)
                    .addComponent(jTextFieldDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jButtonPesquisaSecao)
                    .addComponent(jTextFieldSecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jButtonPesquisaCategoria)
                    .addComponent(jTextFieldCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jButtonPesquisaSubCategoria)
                    .addComponent(jTextFieldSubCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jButtonPesquisaProduto)
                    .addComponent(jTextFieldProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButtonPesquisaFornecedor)
                    .addComponent(jTextFieldFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxFiltroDataPrecoAlterado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserCombo4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxEstoquePositivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonProcessar)
                    .addComponent(jButtonLimpar)
                    .addComponent(jButtonLimpar1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setForeground(new java.awt.Color(0, 0, 255));
        jLabel10.setText("jLabel10");

        jButtonSair.setMnemonic('S');
        jButtonSair.setText("Sair");
        jButtonSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSair)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jButtonSair))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextField1.setEditable(false);

        jButton1.setText("...");
        jButton1.setToolTipText("");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Repositório dos arquivos");

        jTextPane1.setEditable(false);
        ((DefaultCaret)jTextPane1.getCaret()).setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
        jScrollPane2.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 185, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPesquisaFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaFilialActionPerformed

        FormSelecaoFilial f = new FormSelecaoFilial(this, true, ctx);
        f.setVisible(true);
        filial = f.getFilialSel();

        if (filial != null) {
            jTextFieldFilial.setText(String.format("%s - %s", filial.getCodigo(), filial.getRazaosocial()));
            jTextField2.setText(filial.getCodigo());
        }
        f.dispose();

    }//GEN-LAST:event_jButtonPesquisaFilialActionPerformed

    private void jButtonPesquisaSecaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaSecaoActionPerformed

        FormSelecaoSecao f = new FormSelecaoSecao(this, true, (departamentos == null ? null : departamentos.get(0)), ctx);
        f.setVisible(true);
        secoes = f.getSecoesSel();
        f.dispose();
        if (secoes != null) {

            configuraCampoSecao();
            
        }

    }//GEN-LAST:event_jButtonPesquisaSecaoActionPerformed

    private void jButtonPesquisaDepartamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaDepartamentoActionPerformed

        FormSelecaoDepartamento f = new FormSelecaoDepartamento(this, true, ctx);
        f.setVisible(true);
        departamentos = f.getDepartamentosSel();
        configuraCampoDepartamentos();

    }//GEN-LAST:event_jButtonPesquisaDepartamentoActionPerformed

    private void jButtonPesquisaCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaCategoriaActionPerformed

        FormSelecaoCategoria f = new FormSelecaoCategoria(this, true, ctx, secoes);
        f.setVisible(true);
        categorias = f.getCategoriasSel();
        f.dispose();
        configuraCampoCategoria();

    }//GEN-LAST:event_jButtonPesquisaCategoriaActionPerformed

    private void jButtonPesquisaSubCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaSubCategoriaActionPerformed

        FormSelecaoSubCategoria f = new FormSelecaoSubCategoria(this, true, ctx, categorias);
        f.setVisible(true);
        subCategorias = f.getSubCategoriasSel();
        f.dispose();
        configuraCampoSubCategoria();

    }//GEN-LAST:event_jButtonPesquisaSubCategoriaActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        
        resetaObjetosCarregados();
        jTextField1.setText(null);
        jTextField2.setText(null);
        jTextField3.setText(null);
        jTextField4.setText(null);
        jTextField5.setText(null);
        jTextField6.setText(null);
        jTextField7.setText(null);
        jTextField8.setText(null);
        jTextField9.setText(null);
        
        jTextFieldArea.setText(null);
        jTextFieldCategoria.setText(null);
        jTextFieldDepartamento.setText(null);
        jTextFieldFilial.setText(null);
        jTextFieldFornecedor.setText(null);
        jTextFieldProduto.setText(null);
        jTextFieldSecao.setText(null);
        jTextFieldSubCategoria.setText(null);
        jTextPane1.setText(null);
    }//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonPesquisaRegiaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaRegiaoActionPerformed
        FormSelecaoRegiao f = new FormSelecaoRegiao(this, true, ctx);
        f.setVisible(true);
        regiao = f.getRegiaoSel();
        f.dispose();
        if (regiao != null) {
            jTextFieldArea.setText(String.format("%d - %s", regiao.getNumregiao(), regiao.getRegiao()));
            jTextField3.setText(String.valueOf(regiao.getNumregiao()));
        }
    }//GEN-LAST:event_jButtonPesquisaRegiaoActionPerformed

    private void jButtonPesquisaFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaFornecedorActionPerformed
        FormSelecaoFornecedor f = new FormSelecaoFornecedor(this, true, ctx);
        f.setVisible(true);
        fornecedor = f.getFornecedorSel();
        f.dispose();
        if (fornecedor != null) {
            jTextFieldFornecedor.setText(String.format("%d - %s", fornecedor.getCodfornec(), fornecedor.getFornecedor()));
            jTextField9.setText(String.valueOf(fornecedor.getCodfornec()));
        }
    }//GEN-LAST:event_jButtonPesquisaFornecedorActionPerformed

    private void jButtonPesquisaProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaProdutoActionPerformed
        FormSelecaoProduto f = new FormSelecaoProduto(this, true, ctx);
        f.setVisible(true);
        produtos = f.getProdutosSel();
        f.dispose();
        configuraCampoProduto();

    }//GEN-LAST:event_jButtonPesquisaProdutoActionPerformed

    private void jButtonProcessarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProcessarActionPerformed
        jTextPane1.setText(null);
        if(filial == null) {
            JOptionPane.showMessageDialog(this, "É necessário informar uma filial", "Informação", JOptionPane.INFORMATION_MESSAGE);
            jButtonPesquisaFilialActionPerformed(evt);
        } else {
            if(regiao == null) {
                JOptionPane.showMessageDialog(this, "É necessário informar uma região", "Informação", JOptionPane.INFORMATION_MESSAGE);
                jButtonPesquisaRegiaoActionPerformed(evt);
            } else {
                if(jTextField1.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "É necessário informar o repositório dos arquivos", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    jButton1ActionPerformed(evt);
                } else {
                    EtiquetaEletronicaServiceProcess process = new EtiquetaEletronicaServiceProcess(
                            filial, departamentos, secoes, categorias, subCategorias, fornecedor, regiao, produtos, ctx, 
                            dateChooserCombo3, dateChooserCombo4, jTextPane1, jCheckBoxFiltroDataPrecoAlterado, jCheckBoxEstoquePositivo.isSelected(), jTextField1.getText());
                    process.start();
                }
            }
        }
        
    }//GEN-LAST:event_jButtonProcessarActionPerformed

    private void jCheckBoxFiltroDataPrecoAlteradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFiltroDataPrecoAlteradoActionPerformed
        dateChooserCombo3.setEnabled(jCheckBoxFiltroDataPrecoAlterado.isSelected());
        dateChooserCombo4.setEnabled(jCheckBoxFiltroDataPrecoAlterado.isSelected());
    }//GEN-LAST:event_jCheckBoxFiltroDataPrecoAlteradoActionPerformed

    private void jButtonLimpar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpar1ActionPerformed
        Preferencias prefs = Preferencias.builder()
                .caminhoRepositorioArquivos(jTextField1.getText())
                .categorias(categorias)
                .departamentos(departamentos)
                .filial(filial)
                .fornecedor(fornecedor)
                .produtos(produtos)
                .regiao(regiao)
                .secoes(secoes)
                .somenteEstoquePositivo(jCheckBoxEstoquePositivo.isSelected())
                .subCategorias(subCategorias)
                .processoAgendado(jCheckBox1.isSelected())
                .filtrarPorDataAlteracaoPreco(jCheckBoxFiltroDataPrecoAlterado.isSelected())
                .build();
        try {
            prefs.salvarPreferencias();
            JOptionPane.showMessageDialog(this, "Preferências salvas com sucesso.", 
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            log.error("Erro ao salvar as preferências", e);
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao gravar as preferências:\n"
                    + e.getCause().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonLimpar1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fileChooser = new JFileChooser(jTextField1.getText());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retval = fileChooser.showOpenDialog(this);
        if(retval == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            jTextField1.setText(f.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonSairActionPerformed

    private void jTextFieldAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldAreaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            regiao = null;
        }
    }//GEN-LAST:event_jTextFieldAreaKeyPressed

    private void jTextFieldFilialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilialKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            filial = null;
        }
    }//GEN-LAST:event_jTextFieldFilialKeyPressed

    private void jTextFieldDepartamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDepartamentoKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            departamentos = null;
        }
    }//GEN-LAST:event_jTextFieldDepartamentoKeyPressed

    private void jTextFieldSecaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSecaoKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            secoes = null;
        }
    }//GEN-LAST:event_jTextFieldSecaoKeyPressed

    private void jTextFieldCategoriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCategoriaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            categorias = null;
        }
    }//GEN-LAST:event_jTextFieldCategoriaKeyPressed

    private void jTextFieldSubCategoriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSubCategoriaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            subCategorias = null;
        }
    }//GEN-LAST:event_jTextFieldSubCategoriaKeyPressed

    private void jTextFieldProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProdutoKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            produtos = null;
        }
    }//GEN-LAST:event_jTextFieldProdutoKeyPressed

    private void jTextFieldFornecedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFornecedorKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
            ((JTextField) evt.getSource()).setText(null);
            fornecedor = null;
        }
    }//GEN-LAST:event_jTextFieldFornecedorKeyPressed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        focoProximoComponente(evt.getKeyCode(), jTextFieldFilial);
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
        focoProximoComponente(evt.getKeyCode(), jTextFieldArea);
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        focoProximoComponente(evt.getKeyCode(), jTextFieldDepartamento);
    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyPressed
        focoProximoComponente(evt.getKeyCode(), jTextFieldCategoria);
    }//GEN-LAST:event_jTextField6KeyPressed

    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed
        focoProximoComponente(evt.getKeyCode(), jTextFieldSecao);
    }//GEN-LAST:event_jTextField5KeyPressed

    private void jTextField7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyPressed
        focoProximoComponente(evt.getKeyCode(), jTextFieldSubCategoria);
    }//GEN-LAST:event_jTextField7KeyPressed

    private void jTextField8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyPressed
        focoProximoComponente(evt.getKeyCode(), jTextFieldProduto);
    }//GEN-LAST:event_jTextField8KeyPressed

    private void jTextField9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyPressed
        focoProximoComponente(evt.getKeyCode());
    }//GEN-LAST:event_jTextField9KeyPressed

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost

        if(!((JTextField) evt.getSource()).getText().isEmpty()) {
            filial = formService.carregaFilial(((JTextField) evt.getSource()).getText());
            if(filial != null) {
                jTextFieldFilial.setText(String.format("%s - %s", filial.getCodigo(), filial.getRazaosocial()));
            } else {
                jTextFieldFilial.setText(null);
            }
        } else {
            filial = null;
            jTextFieldFilial.setText(null);
        }

    }//GEN-LAST:event_jTextField2FocusLost

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        formService.selecionaText((JTextField) evt.getSource());
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        formService.selecionaText((JTextField) evt.getSource());
    }//GEN-LAST:event_jTextField3FocusGained

    private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
        formService.selecionaText((JTextField) evt.getSource());
    }//GEN-LAST:event_jTextField4FocusGained

    private void jTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusGained
        formService.selecionaText((JTextField) evt.getSource());
    }//GEN-LAST:event_jTextField5FocusGained

    private void jTextField8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField8FocusGained
        formService.selecionaText((JTextField) evt.getSource());
    }//GEN-LAST:event_jTextField8FocusGained

    private void jTextField9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusGained
        formService.selecionaText((JTextField) evt.getSource());
    }//GEN-LAST:event_jTextField9FocusGained

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        try {
            if(!((JTextField) evt.getSource()).getText().isEmpty()) {
                regiao = formService.carregaRegiao( Long.parseLong(((JTextField) evt.getSource()).getText()) );
                if(regiao == null) {
                    jTextFieldArea.setText(null);
                } else {
                    jTextFieldArea.setText(regiao.getNumregiao() + " - " + regiao.getRegiao());
                }
            } else {
                regiao = null;
                jTextFieldArea.setText(null);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um número", "Informação", JOptionPane.INFORMATION_MESSAGE);
            jTextField3.requestFocus();
        }
    }//GEN-LAST:event_jTextField3FocusLost

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
        try {
            if(!((JTextField) evt.getSource()).getText().isEmpty()) {
                Departamento departamento = formService.carregaDepartamento( Long.parseLong(((JTextField) evt.getSource()).getText()) );
                if(departamento == null) {
                    departamentos = null;
                    jTextFieldDepartamento.setText(null);
                } else {
                    departamentos = new ArrayList<>();
                    departamentos.add(departamento);
                    configuraCampoDepartamentos();
                }
            } else {
                departamentos = null;
                jTextFieldDepartamento.setText(null);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um número", "Informação", JOptionPane.INFORMATION_MESSAGE);
            jTextField4.requestFocus();
        }
    }//GEN-LAST:event_jTextField4FocusLost

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        try {
            if(!((JTextField) evt.getSource()).getText().isEmpty()) {
                Secao secao = formService.carregaSecao(Long.parseLong(((JTextField) evt.getSource()).getText()));
                if(secao == null) {
                    secoes = null;
                    jTextFieldSecao.setText(null);
                } else {
                    secoes = new ArrayList<>();
                    secoes.add(secao);
                    configuraCampoSecao();
                }
            } else {
                secoes = null;
                jTextFieldSecao.setText(null);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um número", "Informação", JOptionPane.INFORMATION_MESSAGE);
            jTextField5.requestFocus();
        }
        
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField8FocusLost
        try {
            if(!((JTextField) evt.getSource()).getText().isEmpty()) {
                Produto prod = formService.carregaProduto(Long.parseLong(((JTextField) evt.getSource()).getText()));
                if(prod == null) {
                    produtos = null;
                    jTextFieldProduto.setText(null);
                } else {
                    produtos = new ArrayList<>();
                    produtos.add(prod);
                    configuraCampoProduto();
                }
            } else {
                produtos = null;
                jTextFieldProduto.setText(null);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um número", "Informação", JOptionPane.INFORMATION_MESSAGE);
            jTextField8.requestFocus();
        }

    }//GEN-LAST:event_jTextField8FocusLost

    private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
        try {
            if(!((JTextField) evt.getSource()).getText().isEmpty()) {
                fornecedor = formService.carregaFornecedor(Long.parseLong(((JTextField) evt.getSource()).getText()));
                if(fornecedor == null) {
                    jTextFieldFornecedor.setText(null);
                } else {
                    jTextFieldFornecedor.setText(fornecedor.getCodfornec() + " - " + fornecedor.getFornecedor());
                }
            } else {
                fornecedor = null;
                jTextFieldFornecedor.setText(null);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um número", "Informação", JOptionPane.INFORMATION_MESSAGE);
            jTextField9.requestFocus();
        }
    }//GEN-LAST:event_jTextField9FocusLost

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        FormAgendamentoProcesso f = new FormAgendamentoProcesso(this, true);
        f.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        
        if(((JCheckBox) evt.getSource()).isSelected()) {
            
            preferenciasAgendamento = new PreferenciasAgendamento().carregarPreferencias();
            
            if(preferenciasAgendamento != null) {
                agendamentoService = new AgendamentoEtiquetaEletronicaService(filial, departamentos, secoes, categorias, 
                        subCategorias, fornecedor, regiao, produtos, ctx, dateChooserCombo3, dateChooserCombo4, 
                        jTextPane1, jCheckBoxFiltroDataPrecoAlterado, rootPaneCheckingEnabled, jTextField1.getText(), 
                        preferenciasAgendamento, jLabel11);
                agendamentoService.start();
                habilitaDesabilitaCampoProcessamentoAutomatico();
            } else {
                JOptionPane.showMessageDialog(this, "É necessário realizar a configuração do agendamento", "Informação", JOptionPane.INFORMATION_MESSAGE);
                ((JCheckBox) evt.getSource()).setSelected(false);
                jButton2ActionPerformed(evt);
            }
        } else {
            if(agendamentoService != null && agendamentoService.isAlive()) {
                agendamentoService.parar();
                jLabel11.setText("Finalizando processo");
                habilitaDesabilitaCampoProcessamentoAutomatico();
                jLabel11.setText("Parâmetros para processamento automático configurados.");
            }
        }
        
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void focoProximoComponente(int keyCode, Component proximo) {
        if(keyCode == KeyEvent.VK_ENTER) {
            if(proximo == null) {
                focusManager.focusNextComponent();
            } else {
                focusManager.focusNextComponent(proximo);
            }
        }
    }
    
    private void focoProximoComponente(int keyCode) {
        if(keyCode == KeyEvent.VK_ENTER) {
            focusManager.focusNextComponent();
        }
    }
    
    private void resetaObjetosCarregados() {
        filial = null;
        departamentos = null;
        secoes = null;
        categorias = null;
        subCategorias = null;
        fornecedor = null;
        regiao = null;
        produtos = null;
    }

    private Filial filial;
    private List<Departamento> departamentos;
    private List<Secao> secoes;
    private List<Categoria> categorias;
    private List<SubCategoria> subCategorias;
    private Fornecedor fornecedor;
    private Regiao regiao;
    private List<Produto> produtos;

    private FilialService filialService;
    private FormularioPrincipalService formService;
    private final ApplicationContext ctx;
    
    private PreferenciasAgendamento preferenciasAgendamento;
    
    private AgendamentoEtiquetaEletronicaService agendamentoService;
    
    private final KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo3;
    private datechooser.beans.DateChooserCombo dateChooserCombo4;
    private org.jdatepicker.DateLabelFormatter dateLabelFormatter1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonLimpar1;
    private javax.swing.JButton jButtonPesquisaCategoria;
    private javax.swing.JButton jButtonPesquisaDepartamento;
    private javax.swing.JButton jButtonPesquisaFilial;
    private javax.swing.JButton jButtonPesquisaFornecedor;
    private javax.swing.JButton jButtonPesquisaProduto;
    private javax.swing.JButton jButtonPesquisaRegiao;
    private javax.swing.JButton jButtonPesquisaSecao;
    private javax.swing.JButton jButtonPesquisaSubCategoria;
    private javax.swing.JButton jButtonProcessar;
    private javax.swing.JButton jButtonSair;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBoxEstoquePositivo;
    private javax.swing.JCheckBox jCheckBoxFiltroDataPrecoAlterado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField jTextFieldArea;
    private javax.swing.JTextField jTextFieldCategoria;
    private javax.swing.JTextField jTextFieldDepartamento;
    private javax.swing.JTextField jTextFieldFilial;
    private javax.swing.JTextField jTextFieldFornecedor;
    private javax.swing.JTextField jTextFieldProduto;
    private javax.swing.JTextField jTextFieldSecao;
    private javax.swing.JTextField jTextFieldSubCategoria;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
