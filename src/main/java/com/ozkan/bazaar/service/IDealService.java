package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Deal;

import java.util.List;

public interface IDealService {

    List<Deal> getDeals();

    Deal createDeal(Deal deal);

    Deal updateDeal(Deal deal, Long id) throws Exception;

    void deleteDeal(Long id) throws Exception;
}
