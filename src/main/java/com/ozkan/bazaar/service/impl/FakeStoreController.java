package com.ozkan.bazaar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fakestore")
@RequiredArgsConstructor
public class FakeStoreController {

    private final FakeStoreService fakeStoreService;

    @PostMapping("/import")
    public ResponseEntity<String> importProducts(@RequestHeader("Authorization") String jwt) throws Exception {
        fakeStoreService.importProducts(jwt);
        return ResponseEntity.ok("Products imported successfully!");
    }
}
