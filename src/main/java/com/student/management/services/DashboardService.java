package com.student.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.management.Repository.ProductRepository;
import com.student.management.Repository.UserRepository;
import com.student.management.dto.Dashboard;

@Service
public class DashboardService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private UserRepository userRepo;
    public Dashboard getDashboard(){
        Dashboard dash=new Dashboard();
        dash.setUsers(userRepo.count());
        dash.setProducts(productRepo.count());
        return dash;
    }
    
}
