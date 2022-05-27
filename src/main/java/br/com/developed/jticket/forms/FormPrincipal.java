/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.developed.jticket.forms;

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
import br.com.developed.jticket.services.EtiquetaEletronicaServiceProcess;
import br.com.developed.jticket.services.FilialService;
import br.com.developed.jticket.services.ParametroValorService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
        initComponents();
        configuraLaf();
        verificaTipoPreço();
                
        carregaPreferencias();

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
            }

            if(prefs.getRegiao() != null) {
                regiao = prefs.getRegiao();
                jTextFieldArea.setText(String.format("%d - %s", regiao.getNumregiao(), regiao.getRegiao()));
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
            }
            
            jCheckBoxEstoquePositivo.setSelected(prefs.isSomenteEstoquePositivo());
            jTextField1.setText(prefs.getCaminhoRepositorioArquivos());
            
            jButtonProcessar.setEnabled(true);
        }
        
            
    }
        
    private void configuraCampoProduto() {
        if(produtos != null && produtos.size() >= 1) {
            if(produtos.size() == 1) {
                jTextFieldProduto.setText(String.format("%d - %s", produtos.get(0).getCodprod(), 
                        produtos.get(0).getDescricao()));
            } else {
                jTextFieldCategoria.setText("MÚLTIPLOS");
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
            } else {
                jTextFieldDepartamento.setText("MÚLTIPLOS");
            }
        }
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
        jPanel3 = new javax.swing.JPanel();
        jButtonSair = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
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

        jLabel6.setText("Sub-Categoria");

        jLabel7.setText("Produto");

        jLabel8.setText("Fornecedor");

        jButtonPesquisaDepartamento.setText("...");
        jButtonPesquisaDepartamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaDepartamentoActionPerformed(evt);
            }
        });

        jButtonPesquisaFilial.setText("...");
        jButtonPesquisaFilial.setToolTipText("");
        jButtonPesquisaFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaFilialActionPerformed(evt);
            }
        });

        jTextFieldFilial.setToolTipText("");

        jButtonPesquisaRegiao.setText("...");
        jButtonPesquisaRegiao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaRegiaoActionPerformed(evt);
            }
        });

        jButtonPesquisaSecao.setText("...");
        jButtonPesquisaSecao.setToolTipText("");
        jButtonPesquisaSecao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaSecaoActionPerformed(evt);
            }
        });

        jButtonPesquisaCategoria.setText("...");
        jButtonPesquisaCategoria.setToolTipText("");
        jButtonPesquisaCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaCategoriaActionPerformed(evt);
            }
        });

        jButtonPesquisaSubCategoria.setText("...");
        jButtonPesquisaSubCategoria.setToolTipText("");
        jButtonPesquisaSubCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaSubCategoriaActionPerformed(evt);
            }
        });

        jButtonPesquisaProduto.setText("...");
        jButtonPesquisaProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaProdutoActionPerformed(evt);
            }
        });

        jButtonPesquisaFornecedor.setText("...");
        jButtonPesquisaFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisaFornecedorActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxEstoquePositivo, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(49, 49, 49)
                                    .addComponent(jButtonPesquisaProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButtonPesquisaSubCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(36, 36, 36)
                                    .addComponent(jButtonPesquisaCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(57, 57, 57)
                                    .addComponent(jButtonPesquisaSecao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButtonPesquisaDepartamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1))
                                    .addGap(53, 53, 53)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButtonPesquisaFilial, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                        .addComponent(jButtonPesquisaRegiao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(25, 25, 25)
                                .addComponent(jButtonPesquisaFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldArea, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldFilial)
                            .addComponent(jTextFieldSecao, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldDepartamento)
                            .addComponent(jTextFieldProduto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldSubCategoria, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldCategoria)
                            .addComponent(jTextFieldFornecedor)))
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
                        .addComponent(jButtonLimpar1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButtonPesquisaFilial)
                    .addComponent(jTextFieldFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButtonPesquisaRegiao)
                    .addComponent(jTextFieldArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButtonPesquisaDepartamento)
                    .addComponent(jTextFieldDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jButtonPesquisaSecao)
                    .addComponent(jTextFieldSecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jButtonPesquisaCategoria)
                    .addComponent(jTextFieldCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jButtonPesquisaSubCategoria)
                    .addComponent(jTextFieldSubCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jButtonPesquisaProduto)
                    .addComponent(jTextFieldProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButtonPesquisaFornecedor)
                    .addComponent(jTextFieldFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonSair.setMnemonic('S');
        jButtonSair.setText("Sair");
        jButtonSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSairActionPerformed(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 0, 255));
        jLabel10.setText("jLabel10");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSair, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSair)
                    .addComponent(jLabel10))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
                        .addGap(0, 187, Short.MAX_VALUE))
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

    private void jButtonSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonSairActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed

        resetaObjetosCarregados();
    }//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonPesquisaRegiaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaRegiaoActionPerformed
        FormSelecaoRegiao f = new FormSelecaoRegiao(this, true, ctx);
        f.setVisible(true);
        regiao = f.getRegiaoSel();
        f.dispose();
        if (regiao != null) {
            jTextFieldArea.setText(String.format("%d - %s", regiao.getNumregiao(), regiao.getRegiao()));
        }
    }//GEN-LAST:event_jButtonPesquisaRegiaoActionPerformed

    private void jButtonPesquisaFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisaFornecedorActionPerformed
        FormSelecaoFornecedor f = new FormSelecaoFornecedor(this, true, ctx);
        f.setVisible(true);
        fornecedor = f.getFornecedorSel();
        f.dispose();
        if (fornecedor != null) {
            jTextFieldFornecedor.setText(String.format("%d - %s", fornecedor.getCodfornec(), fornecedor.getFornecedor()));
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
        EtiquetaEletronicaServiceProcess process = new EtiquetaEletronicaServiceProcess(
                filial, departamentos, secoes, categorias, subCategorias, fornecedor, regiao, produtos, ctx, 
                dateChooserCombo3, dateChooserCombo4, jTextPane1, jCheckBoxFiltroDataPrecoAlterado, jCheckBoxEstoquePositivo.isSelected(), jTextField1.getText());
        process.start();
        
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
    private final ApplicationContext ctx;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo3;
    private datechooser.beans.DateChooserCombo dateChooserCombo4;
    private org.jdatepicker.DateLabelFormatter dateLabelFormatter1;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JCheckBox jCheckBoxEstoquePositivo;
    private javax.swing.JCheckBox jCheckBoxFiltroDataPrecoAlterado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
