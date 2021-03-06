/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.ComboItemController;
import Controllers.DB;
import Controllers.GoalController;
import Controllers.Helper;
import Models.MyDefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author anchor
 */
public class AddGoalView extends javax.swing.JFrame {

    private static GoalController goalController;
    
    public AddGoalView() {
        initComponents();
        goalController = new GoalController();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblGoalName = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblGoalCategory = new javax.swing.JLabel();
        lblGoalAmount = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        dtpStartDate = new org.jdesktop.swingx.JXDatePicker();
        dtpStopDate = new org.jdesktop.swingx.JXDatePicker();
        cbxCategory = new javax.swing.JComboBox<>();
        txtAmount = new javax.swing.JTextField();
        scrlTable = new javax.swing.JScrollPane();
        tblGoals = new JTable(){
            private Border outside = new MatteBorder(1, 0, 1, 0, Color.RED);
            private Border inside = new EmptyBorder(0, 1, 0, 1);
            private Border highlight = new CompoundBorder(outside, inside);

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent)c;
                // Add a border to the selected row

                if (isRowSelected(row) && isCellEditable(row, 1))
                jc.setBorder( highlight );

                return c;
            }
        };
        btnSaveGoal = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnEditRecord = new javax.swing.JButton();
        btnDeleteRecord = new javax.swing.JButton();
        lblGoalType = new javax.swing.JLabel();
        lblIncomeOrExpense = new javax.swing.JLabel();

        setTitle("Goals");
        setPreferredSize(new java.awt.Dimension(650, 540));
        setSize(new java.awt.Dimension(650, 540));

        lblGoalName.setText("Goal Name");
        lblGoalName.setName("lblGoalName"); // NOI18N

        lblStartDate.setText("Start Date");
        lblStartDate.setName("lblStartDate"); // NOI18N

        lblEndDate.setText("End Date");
        lblEndDate.setName("lblEndDate"); // NOI18N

        lblGoalCategory.setText("Category");
        lblGoalCategory.setName("lblGoalCategory"); // NOI18N

        lblGoalAmount.setText("Amount");
        lblGoalAmount.setName("lblGoalAmount"); // NOI18N

        txtName.setMaximumSize(new java.awt.Dimension(110, 30));
        txtName.setMinimumSize(new java.awt.Dimension(110, 30));
        txtName.setPreferredSize(new java.awt.Dimension(110, 30));

        cbxCategory.setMaximumSize(new java.awt.Dimension(110, 30));
        cbxCategory.setMinimumSize(new java.awt.Dimension(110, 30));
        cbxCategory.setPreferredSize(new java.awt.Dimension(110, 30));

        txtAmount.setMaximumSize(new java.awt.Dimension(110, 30));
        txtAmount.setMinimumSize(new java.awt.Dimension(110, 30));
        txtAmount.setName("txtAmount"); // NOI18N
        txtAmount.setPreferredSize(new java.awt.Dimension(110, 30));

        tblGoals.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tblGoals.setModel(new MyDefaultTableModel(30, new String[]{"Name","Amount","Start Date", "Stop Date", "Goal Type", "Category"}));
        tblGoals.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //                   goalController.editRecord();
                }
            }
        });
        scrlTable.setViewportView(tblGoals);

        btnSaveGoal.setText("Save Goal");
        btnSaveGoal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveGoalActionPerformed(evt);
            }
        });

        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeClick(evt);
            }
        });

        btnEditRecord.setText("Edit Record");
        btnEditRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditRecordActionPerformed(evt);
            }
        });

        btnDeleteRecord.setText("Delete Record");
        btnDeleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteRecordActionPerformed(evt);
            }
        });

        lblGoalType.setText("Goal Type");

        lblIncomeOrExpense.setText("Income or Expense Goal");
        lblIncomeOrExpense.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lblIncomeOrExpense.setMinimumSize(new java.awt.Dimension(0, 0));
        lblIncomeOrExpense.setPreferredSize(new java.awt.Dimension(642, 520));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrlTable)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStartDate)
                            .addComponent(lblGoalName, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGoalType, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dtpStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblIncomeOrExpense, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dtpStopDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblGoalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblGoalCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDeleteRecord, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEditRecord, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSaveGoal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrlTable, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGoalName)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStartDate)
                            .addComponent(dtpStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGoalAmount)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEndDate)
                            .addComponent(dtpStopDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGoalCategory)
                            .addComponent(lblGoalType)
                            .addComponent(lblIncomeOrExpense, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(btnSaveGoal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditRecord)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeleteRecord)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dtpStartDate.getEditor().setEditable(false);
        dtpStopDate.getEditor().setEditable(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeClick
        // TODO add your handling code here:
        goalController.closeAddGoalView();
    }//GEN-LAST:event_btnHomeClick

    private void btnSaveGoalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveGoalActionPerformed
        if(goalController.validateFields()){
        goalController.processNewGoal();
        }
        else{
            JOptionPane.showMessageDialog(this, "Invalid input when saving the goal.");
        }
    }//GEN-LAST:event_btnSaveGoalActionPerformed

    private void btnDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteRecordActionPerformed
        goalController.deleteRecord();
    }//GEN-LAST:event_btnDeleteRecordActionPerformed

    private void btnEditRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRecordActionPerformed
        goalController.startTableEdit();
    }//GEN-LAST:event_btnEditRecordActionPerformed

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
            java.util.logging.Logger.getLogger(AddGoalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddGoalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddGoalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddGoalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddGoalView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteRecord;
    private javax.swing.JButton btnEditRecord;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnSaveGoal;
    private javax.swing.JComboBox<String> cbxCategory;
    private org.jdesktop.swingx.JXDatePicker dtpStartDate;
    private org.jdesktop.swingx.JXDatePicker dtpStopDate;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblGoalAmount;
    private javax.swing.JLabel lblGoalCategory;
    private javax.swing.JLabel lblGoalName;
    private javax.swing.JLabel lblGoalType;
    private javax.swing.JLabel lblIncomeOrExpense;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JScrollPane scrlTable;
    private javax.swing.JTable tblGoals;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JLabel getLblIncomeOrExpense() {
        return lblIncomeOrExpense;
    }

    public void setLblIncomeOrExpense(javax.swing.JLabel lblIncomeOrExpense) {
        this.lblIncomeOrExpense = lblIncomeOrExpense;
    }

    public javax.swing.JComboBox<String> getCbxCategory() {
        return cbxCategory;
    }

    public void setCbxCategory(javax.swing.JComboBox<String> cbxCategory) {
        this.cbxCategory = cbxCategory;
    }

    public org.jdesktop.swingx.JXDatePicker getDtpStartDate() {
        return dtpStartDate;
    }

    public void setDtpStartDate(org.jdesktop.swingx.JXDatePicker dtpStartDate) {
        this.dtpStartDate = dtpStartDate;
    }

    public org.jdesktop.swingx.JXDatePicker getDtpStopDate() {
        return dtpStopDate;
    }

    public void setDtpStopDate(org.jdesktop.swingx.JXDatePicker dtpStopDate) {
        this.dtpStopDate = dtpStopDate;
    }

    public javax.swing.JTextField getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(javax.swing.JTextField txtAmount) {
        this.txtAmount = txtAmount;
    }

    public javax.swing.JTextField getTxtName() {
        return txtName;
    }

    public void setTxtName(javax.swing.JTextField txtName) {
        this.txtName = txtName;
    }

    public javax.swing.JTable getTblGoals() {
        return tblGoals;
    }

    public void setTblGoals(javax.swing.JTable tblGoals) {
        this.tblGoals = tblGoals;
    }
}
