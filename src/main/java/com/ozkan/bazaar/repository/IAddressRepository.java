package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends JpaRepository<Address,Long> {

}
