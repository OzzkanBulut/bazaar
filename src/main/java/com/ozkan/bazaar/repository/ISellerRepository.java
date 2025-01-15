package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.domain.AccountStatus;
import com.ozkan.bazaar.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISellerRepository extends JpaRepository<Seller,Long> {

    Seller findByEmail(String email);

    List<Seller> findAllByAccountStatus(AccountStatus accountStatus);
}
