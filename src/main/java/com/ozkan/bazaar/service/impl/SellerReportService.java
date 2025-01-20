package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.model.SellerReport;
import com.ozkan.bazaar.repository.ISellerReportRepository;
import com.ozkan.bazaar.service.ISellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportService implements ISellerReportService {

    private final ISellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport report = sellerReportRepository.findBySellerId(seller.getId());
        if (report == null) {
            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport);
        }
        return report;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }
}
