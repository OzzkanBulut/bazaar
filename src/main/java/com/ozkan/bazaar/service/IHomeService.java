package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Home;
import com.ozkan.bazaar.model.HomeCategory;

import java.util.List;

public interface IHomeService {
    Home createHomePageData(List<HomeCategory> homeCategoryList);
}
