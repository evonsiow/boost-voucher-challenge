package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.voucher.model.SpecialOfferRequestVM;
import com.boost.module.voucher.model.SpecialOfferResponseVM;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

import static com.boost.module.core.util.APIConstant.APPLICATION_JSON;

@RequestMapping(value = "/offers", produces = APPLICATION_JSON)
public interface SpecialOfferAPI {
    @PostMapping
    ResponseEntity<ApiResponse<SpecialOfferResponseVM>> createOffer(@Valid @RequestBody SpecialOfferRequestVM specialOfferRequestVM);

    @GetMapping
    ResponseEntity<ApiResponse<List<SpecialOfferResponseVM>>> getAllOffers();

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<SpecialOfferResponseVM>> getOfferById(
            @Parameter(description = "ID of the special offer") @PathVariable UUID id);

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<SpecialOfferResponseVM>> updateOffer(
            @Parameter(description = "ID of the special offer") @PathVariable UUID id,
            @Valid @RequestBody SpecialOfferRequestVM request);

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Boolean>> deleteOffer(
            @Parameter(description = "ID of the special offer") @PathVariable UUID id);
}
