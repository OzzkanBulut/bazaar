package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.*;
import com.ozkan.bazaar.response.ApiResponse;
import com.ozkan.bazaar.response.PaymentLinkResponse;
import com.ozkan.bazaar.service.*;
import com.ozkan.bazaar.service.impl.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final IPaymentService paymentService;
    private final IUserService userService;
    private final ISellerService sellerService;
    private final ISellerReportService sellerReportService;
    private final ITransactionService transactionService;


    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        PaymentLinkResponse paymentLinkResponse;
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder,paymentId,paymentLinkId);

        if(paymentSuccess){
            for(Order order:paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() +1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+ order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
        }

        ApiResponse res = new ApiResponse("Payment Successful");

        return new ResponseEntity<>(res, HttpStatus.CREATED);



    }


}
