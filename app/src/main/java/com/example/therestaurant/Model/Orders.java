package com.example.therestaurant.Model;

public class Orders
{
    private  String date, pid, pname, price, quantity, status, table, waiterName ,waiterPhone;

    public Orders() {
    }

    public Orders(String date, String pid, String pname, String price, String quantity, String status, String table, String waiterName, String waiterPhone) {
        this.date = date;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.table = table;
        this.waiterName = waiterName;
        this.waiterPhone = waiterPhone;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTable()
    {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getWaiterPhone() {
        return waiterPhone;
    }

    public void setWaiterPhone(String waiterPhone) {
        this.waiterPhone = waiterPhone;
    }
}
