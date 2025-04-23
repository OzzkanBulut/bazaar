package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Deal;
import com.ozkan.bazaar.response.ApiResponse;
import com.ozkan.bazaar.service.IDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {

    private final IDealService dealService;

    @PostMapping()
    public ResponseEntity<Deal> createDeal(
            @RequestBody Deal deal
    ) {
        return ResponseEntity.ok(dealService.createDeal(deal));
    }

    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals(){
        return ResponseEntity.ok(dealService.getDeals());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(
            @RequestBody Deal deal,
            @PathVariable Long id
    ) throws Exception {
        Deal updatedDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updatedDeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeal(
            @PathVariable Long id
    ) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse response = new ApiResponse();
        response.setMessage("Deal deleted");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
