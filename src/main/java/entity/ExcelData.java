package entity;

import java.util.Date;

public class ExcelData {
    private String column1;
    private Integer column2;
    private Date column3;

    public ExcelData() {

    }

    public ExcelData(String column1, Integer column2, Date column3) {
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
    }

    // Getters and Setters
    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public Integer getColumn2() {
        return column2;
    }

    public void setColumn2(Integer column2) {
        this.column2 = column2;
    }

    public Date getColumn3() {
        return column3;
    }

    public void setColumn3(Date column3) {
        this.column3 = column3;
    }
}
