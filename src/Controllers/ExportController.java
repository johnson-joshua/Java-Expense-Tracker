/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Views.ExportDataView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author johnsonjm
 */
public class ExportController {
        public static ExportDataView exportPage;
        private String dataType;
        private String sortBy;
        private File dir;
        private String fileName;
        
    public void showExportDataView() {
        exportPage = new ExportDataView();
        exportPage.setVisible(true);
    }

    public void showMainPage() {
        resetFields();
        exportPage.setVisible(false);
    }

    public void resetFields(){
        //called after export process and after user clicks home
        exportPage.getCbxFileType().setSelectedIndex(0);
        exportPage.getCbxSort().setSelectedIndex(0);
        exportPage.getCbxTypeOfData().setSelectedIndex(0);
    }
    
    //exportData called when user selects type, sort, filetype and clicks export
    public void exportData() {
        //type of data - exp, inc, lim, goal ; sort data by - categ, amt ; type of file (csv or txt)
        
        //1.Store data from user
        dataType = exportPage.getCbxTypeOfData().getSelectedItem().toString(); //Epenses, Income, Goals, Limits
        sortBy = exportPage.getCbxSort().getSelectedItem().toString(); //Category, Amount, Name
        String orderByStatement = setupOrderByStatement(dataType, sortBy);
        if(orderByStatement == ""){
            resetFields();
        }
        else{        
        //2.Load sorted data from DB
            writeFile( loadDataFromDB(orderByStatement));
        }
        resetFields();
}
    
    private void writeFile(ResultSet rs){
        //4.Save to File (fileType)
        String fileType = exportPage.getCbxFileType().getSelectedItem().toString(); //.csv, .txt
        final JFileChooser fc = new JFileChooser();
        
        FileNameExtensionFilter filter;
        if(".txt".equals(fileType)){
            filter = new FileNameExtensionFilter("Text Files", "txt");
        }
        else{
            filter = new FileNameExtensionFilter("Comma Separated Values Files", "csv");
        }
        fc.setFileFilter(filter);
        int returnVal = fc.showSaveDialog(exportPage); 
        //5.Get file name & save location from user (JFileChooser.showSaveDialog())
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File originalFile = fc.getSelectedFile();
            File targetFile = null;
            if(".txt".equals(fileType)){
            fileName = originalFile.toString();
            if (!fileName.endsWith(".txt")){
                fileName += ".txt";
                // Gets the File object for the directory that contains the file
                File dir = originalFile.getParentFile();
                // Creates a File object for a file in the same directory with the name "mywork.pdf"
                targetFile = new File(dir, File.pathSeparator  +fileName);
            }
            }
            else{
               fileName = originalFile.toString();
               if(!fileName.endsWith(".csv")){
                   fileName += ".csv";
                    File dir = originalFile.getParentFile();
                    targetFile = new File(dir, File.pathSeparator +fileName);
               }
            }
            try {    
                targetFile = new File(dir, fileName);
                FileWriter fileWriter = new FileWriter(targetFile.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    boolean headerWritten = false;
                    rs.beforeFirst();
                    while(rs.next()){
                    //Collects data relevent to all income, exp, limits, and goals
                    String category = rs.getString("Category");
                    String amount = rs.getString("Amount"); 
                    if("Expenses".equals(dataType) || "Income".equals(dataType)){
                    String startDate = rs.getString("Date");
                    String stopDate = rs.getString("EndDate");
                    String recurring = rs.getString("Recurring");
                        String frequency = rs.getString("Frequency");
                        String notes = rs.getString("Notes");
                        if(".txt".equals(dataType)){
                            if(headerWritten == false){
                            bufferedWriter.write("Type \t Amount \t Recurring \t Start Date \t Stop Date \t Frequency \t Notes" +System.lineSeparator());
                            headerWritten = true;
                            }
                            bufferedWriter.write(category + " " +amount +" " +startDate +" " +stopDate +" " +recurring +" " +frequency +" " +notes +System.lineSeparator());
                        }
                        else{
                            if(headerWritten == false){
                            bufferedWriter.write("Type,Amount,Recurring,Start Date,Stop Date,Frequency,Notes" +System.lineSeparator());
                                headerWritten = true;
                            }
                            bufferedWriter.write(category + "," +amount +"," +startDate +"," +stopDate +"," +recurring +"," +frequency +"," +notes +System.lineSeparator());
                        }
                    }
                    else{//user selected limits or goals
                        String startDate = rs.getString("StartDate");
                        String stopDate = rs.getString("EndDate");
                        String name = rs.getString("Name");
                        String type = rs.getString("Type");
                        if(".txt".equals(dataType)){
                            if(headerWritten == false){
                                bufferedWriter.write("Name \t Amount \t Start Date \t Stop Date \t Type \t Category" +System.lineSeparator());
                                headerWritten = true;
                            }
                        bufferedWriter.write(category + " " +amount +" " +startDate +" " +stopDate +" " +name +" " +type +System.lineSeparator());
                        }
                        else{
                            if(headerWritten == false){
                                bufferedWriter.write("Name \t Amount \t Start Date \t Stop Date \t Type \t Category" +System.lineSeparator());
                                headerWritten = true;
                            }
                            bufferedWriter.write(category + "," +amount +"," +startDate +"," +stopDate +"," +name +"," +type +System.lineSeparator());
                        }
                    }
                    }
                
                bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
                }
            }
    }
    
    private String setupOrderByStatement(String typeOfData, String sortBy){
       // r.Amount, r.Date, c.Name as Category, t.Name as Type, " +
       // "r.Notes, r.UserID, r.Recurring, f.Name as Frequency, r.EndDate 
       this.dataType = typeOfData;
       this.sortBy = sortBy;
       String dbSortName = "";
       if("Income".equals(typeOfData) || "Expenses".equals(typeOfData)){
            if("Category".equals(sortBy)){
                dbSortName = "c.Name";   //"c.Name as Category";
            }
            if("Amount".equals(sortBy)){
                dbSortName = "r.Amount";
            }
            if("Name".equals(sortBy)){
                JOptionPane.showMessageDialog(exportPage, "Income and Expenses cannot be sorted by Name.");
                if(!exportPage.isShowing()){
                    exportPage.setVisible(true);
                }
            }
       }
       
       if("Goals".equals(typeOfData)){
           //g.Date, g.Amount, etc
           if("Category".equals(sortBy)){
               dbSortName = "c.Name";
           }
            
           if("Amount".equals(sortBy)){
                dbSortName = "g.Amount"; 
           }
           
           if("Name".equals(sortBy)){
                dbSortName = "g.Name";
            }
       }
       
       if("Limits".equals(typeOfData)){
           //l.Date, l.Amount, etc
           if("Category".equals(sortBy)){
               dbSortName = "c.Name";
           }
           if("Amount".equals(sortBy)){
               dbSortName = "l.Amount";
           }
           if("Name".equals(sortBy)){
               dbSortName = "l.Name";
           }
           dbSortName = "l.Name";
       }
       String orderBy = "ORDER BY " +dbSortName;
       return orderBy;
    }
    
    private ResultSet loadDataFromDB(String orderByStatement){
        DB db = new DB();
        ResultSet rs = null; //, rs2 = null; //In the case of goals and limits, get both income/expense goals and limits
        switch(dataType){
            case "Expenses":
                rs = db.getExpensesBySort(LoginController.user.userID, orderByStatement);
                break;
            case "Income":
                rs = db.getIncomeBySort(LoginController.user.userID, orderByStatement);
                break;
            case "Goals":
                rs = db.getGoalsBySort(LoginController.user.userID, 1, orderByStatement); //Income Goals
             // rs2 = db.getGoals(LoginController.user.userID, 2); //Expense Goals
                break;
            case "Limits":
                rs = db.getLimitsBySort(LoginController.user.userID, 1, orderByStatement); //Income Limits
              //  rs2 = db.getLimits(LoginController.user.userID, 2); //Expense Limits           
                break;
            default: break;
        }
        return rs;
    }
    
}
