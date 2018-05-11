/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.ComboItemController;
import Controllers.DB;
import Controllers.IncomeController;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author johnsonjm
 */
public class AddIncomeView extends javax.swing.JFrame {
    private static IncomeController incomeController;
    /**
     * Creates new form AddIncomeView
     */
    public AddIncomeView() {
        initComponents();
        incomeController = new IncomeController();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblAmount = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        lblDate = new javax.swing.JLabel();
        dtpDate = new org.jdesktop.swingx.JXDatePicker();
        cbxCategory = new javax.swing.JComboBox<>();
        lblCategory = new javax.swing.JLabel();
        btnAddIncome = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        lblEndDate = new javax.swing.JLabel();
        dtpEndDate = new org.jdesktop.swingx.JXDatePicker();
        chkRecurring = new javax.swing.JCheckBox();
        lblFrequency = new javax.swing.JLabel();
        cbxFrequency = new javax.swing.JComboBox<>();
        lblNote = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtaNote = new javax.swing.JTextArea();

        setTitle("Add New Income");

        lblAmount.setText("Amount:");

        lblDate.setText("Date:");

        lblCategory.setText("Category:");

        btnAddIncome.setText("Add Income");
        btnAddIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddIncomeActionPerformed(evt);
            }
        });

        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        lblEndDate.setText("End Date:");
        lblEndDate.setEnabled(false);

        dtpEndDate.setEnabled(false);

        chkRecurring.setText("Recurring Income?");
        chkRecurring.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRecurringActionPerformed(evt);
            }
        });

        lblFrequency.setText("Frequency:");
        lblFrequency.setEnabled(false);

        cbxFrequency.setEnabled(false);

        lblNote.setText("Note:");

        txtaNote.setColumns(20);
        txtaNote.setRows(5);
        jScrollPane1.setViewportView(txtaNote);

        DB db = new DB();
        ResultSet rs = db.getCategories(1);   // 1 for income categories, 2 for expense categories
        try {
            while (rs.next()) {
                cbxCategory.addItem(new ComboItemController(rs.getInt("ID"), rs.getString("Name")).toString());
            }   } catch (SQLException ex) {
                Logger.getLogger(IncomeView.class.getName()).log(Level.SEVERE, null, ex);
            }
            db = new DB();
            rs = db.getFrequencies();
            try {
                while (rs.next()) {
                    cbxFrequency.addItem(new ComboItemController(rs.getInt("ID"), rs.getString("Name")).toString());
                }   } catch (SQLException ex) {
                    Logger.getLogger(IncomeView.class.getName()).log(Level.SEVERE, null, ex);
                }

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNote))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(dtpDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(cbxCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addComponent(lblEndDate)
                                                .addGap(18, 18, 18)
                                                .addComponent(dtpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(chkRecurring)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblFrequency)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbxFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addContainerGap())
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAmount)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDate)
                            .addComponent(dtpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEndDate)
                            .addComponent(dtpEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFrequency)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chkRecurring)
                                .addComponent(cbxFrequency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCategory))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(btnAddIncome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHome))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblNote)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                );

                dtpDate.getEditor().setEditable(false);
                dtpEndDate.getEditor().setEditable(false);

                pack();
            }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        incomeController.returnToMainPage();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void chkRecurringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRecurringActionPerformed
               //ENABLE/DISABLE LABEL AND FREQUENCY CBX
        if(chkRecurring.isSelected()){
            lblFrequency.setEnabled(true);
            cbxFrequency.setEnabled(true);
            lblEndDate.setEnabled(true);
            dtpEndDate.setEnabled(true);
        }
        else{
            lblFrequency.setEnabled(false);
            cbxFrequency.setEnabled(false);
            lblEndDate.setEnabled(false);
            dtpEndDate.setEnabled(false);
        }
    }//GEN-LAST:event_chkRecurringActionPerformed

    private void btnAddIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddIncomeActionPerformed
        if(incomeController.validateFields()){
        incomeController.processNewIncome();  
        }else
        {
            JOptionPane.showMessageDialog(this, "Invalid input when saving Income.");
        }
    }//GEN-LAST:event_btnAddIncomeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddIncomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddIncomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddIncomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddIncomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddIncomeView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddIncome;
    private javax.swing.JButton btnHome;
    private javax.swing.JComboBox<String> cbxCategory;
    private javax.swing.JComboBox<String> cbxFrequency;
    private javax.swing.JCheckBox chkRecurring;
    private org.jdesktop.swingx.JXDatePicker dtpDate;
    private org.jdesktop.swingx.JXDatePicker dtpEndDate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAmount;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblFrequency;
    private javax.swing.JLabel lblNote;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextArea txtaNote;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JComboBox<String> getCbxCategory() {
        return cbxCategory;
    }

    public void setCbxCategory(javax.swing.JComboBox<String> cbxCategory) {
        this.cbxCategory = cbxCategory;
    }

    public javax.swing.JComboBox<String> getCbxFrequency() {
        return cbxFrequency;
    }

    public void setCbxFrequency(javax.swing.JComboBox<String> cbxFrequency) {
        this.cbxFrequency = cbxFrequency;
    }

    public javax.swing.JCheckBox getChkRecurring() {
        return chkRecurring;
    }

    public void setChkRecurring(javax.swing.JCheckBox chkRecurring) {
        this.chkRecurring = chkRecurring;
    }

    public org.jdesktop.swingx.JXDatePicker getDtpDate() {
        return dtpDate;
    }

    public void setDtpDate(org.jdesktop.swingx.JXDatePicker dtpDate) {
        this.dtpDate = dtpDate;
    }

    public org.jdesktop.swingx.JXDatePicker getDtpEndDate() {
        return dtpEndDate;
    }

    public void setDtpEndDate(org.jdesktop.swingx.JXDatePicker dtpEndDate) {
        this.dtpEndDate = dtpEndDate;
    }

//    public javax.swing.JLabel getLblTotalIncome() {
//        return lblTotalIncome;
//    }
//
//    public void setLblTotalIncome(javax.swing.JLabel lblTotalIncome) {
//        this.lblTotalIncome = lblTotalIncome;
//    }

    public javax.swing.JTextField getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(javax.swing.JTextField txtAmount) {
        this.txtAmount = txtAmount;
    }

    public javax.swing.JLabel getLblNote() {
        return lblNote;
    }

    public void setLblNote(javax.swing.JLabel lblNote) {
        this.lblNote = lblNote;
    }

    public javax.swing.JTextArea getTxtaNote() {
        return txtaNote;
    }

    public void setTxtaNote(javax.swing.JTextArea txtaNote) {
        this.txtaNote = txtaNote;
    }

}