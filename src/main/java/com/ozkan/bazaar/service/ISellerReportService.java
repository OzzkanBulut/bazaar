package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.model.SellerReport;

public interface ISellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);






}
