package com.ozkan.bazaar.repository;

import com.ozkan.bazaar.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {

    PaymentOrder findByPaymentLinkId(String paymentId);
}
