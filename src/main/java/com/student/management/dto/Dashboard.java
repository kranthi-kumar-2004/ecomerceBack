package com.student.management.dto;

public class Dashboard {
    private long users;
    private long products;
    public void setUsers(long users){
        this.users=users;
    }
    public long getUsers(){
        return users;
    }
    public void setProducts(long products){
        this.products=products;
    }
    public long getProducts(){
        return products;
    }
}
