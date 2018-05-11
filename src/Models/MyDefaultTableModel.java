package Models;

import com.mysql.jdbc.RowData;
import javax.swing.table.DefaultTableModel;

public class MyDefaultTableModel extends DefaultTableModel {
    private boolean[][] editable_cells; // 2d array to represent rows and columns
    private String[] columnNames = new String[]{"Type", "Amount", "Recurring", "Start Date", "Stop Date", "Frequency", "Notes"};

    public MyDefaultTableModel(int rows, int cols) { // constructor
//            super( new Object [][] {
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null}
//            },new String [] {
//                "Type", "Amount", "Recurring", "Start Date", "Stop Date", "Frequency", "Notes"
//            });
               super(new Object[] {"Type", "Amount", "Recurring", "Start Date", "Stop Date", "Frequency", "Notes"}, rows);
        this.editable_cells = new boolean[rows][cols];
    }
    
    private Object[][] getObjectArray(int rows, int cols){
        Object[][] objectArray;
        objectArray = new Object[rows][cols];
        for(int c = 0; c < cols; c++){
            for(int r = 0; r < rows; r++){
                objectArray[r][c] = null;
            }
        }
        return objectArray;
    }
    
    public MyDefaultTableModel(int rows, String[] colNames) { // constructor
//            super( new Object [][] {
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null}
//            },
//            colNames);
        super(colNames, rows);
        this.editable_cells = new boolean[rows][colNames.length];
    }

    @Override
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        return this.editable_cells[row][column];
    }

    public void setCellEditable(int row, int col, boolean value) {
        this.editable_cells[row][col] = value; // set cell true/false
        this.fireTableCellUpdated(row, col);
    }
    
    public void addMoreRows(int numRows, int numOfCols){
//        this.addRow(new Object [][] {
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null},
//                {null, null, null, null, null, null, null}},columnNames);
        for(int i = 0; i < numRows; i++){
            int size = this.getRowCount();
            this.insertRow(size, new Object[numOfCols]);
        }
    }
    
}