package com.student.management.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.student.management.enitity.Address;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId); // ✅ multiple addresses
    List<Address> findByUser_Id(Long userId);

}
