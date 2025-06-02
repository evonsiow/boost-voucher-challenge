package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.core.web.AbstractCommonController;
import com.boost.module.voucher.dto.SpecialOfferDTO;
import com.boost.module.voucher.model.SpecialOfferRequestVM;
import com.boost.module.voucher.model.SpecialOfferResponseVM;
import com.boost.module.voucher.service.SpecialOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class SpecialOfferController extends AbstractCommonController implements SpecialOfferAPI {
    private final SpecialOfferService specialOfferService;

    public SpecialOfferController(SpecialOfferService specialOfferService) {
        this.specialOfferService = specialOfferService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new special offer", description = "Creates a new special offer with discount percentage")
    public ResponseEntity<ApiResponse<SpecialOfferResponseVM>> createOffer(@Valid @RequestBody SpecialOfferRequestVM specialOfferRequestVM) {
        SpecialOfferDTO specialOfferDTO = specialOfferService.createOffer(specialOfferRequestVM);
        SpecialOfferResponseVM specialOfferResponseVM = SpecialOfferResponseVM.map(specialOfferDTO);
        return successResponse(specialOfferResponseVM);
    }

    @Override
    @GetMapping
    @Operation(summary = "Get all special offers", description = "Retrieves all special offers in the system")
    public ResponseEntity<ApiResponse<List<SpecialOfferResponseVM>>> getAllOffers() {
        List<SpecialOfferDTO> offers = specialOfferService.getAllOffers();
        List<SpecialOfferResponseVM> specialOfferResponseVMList = offers.stream()
                .map(SpecialOfferResponseVM::map)
                .collect(Collectors.toList());
        return successResponse(specialOfferResponseVMList);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Get special offer by ID", description = "Retrieves a special offer by its ID")
    public ResponseEntity<ApiResponse<SpecialOfferResponseVM>> getOfferById(
            @Parameter(description = "ID of the special offer") @PathVariable UUID id) {
        SpecialOfferDTO specialOfferDTO = specialOfferService.getOfferById(id);
        SpecialOfferResponseVM specialOfferResponseVM = SpecialOfferResponseVM.map(specialOfferDTO);
        return successResponse(specialOfferResponseVM);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update special offer", description = "Updates an existing special offer")
    public ResponseEntity<ApiResponse<SpecialOfferResponseVM>> updateOffer(
            @Parameter(description = "ID of the special offer") @PathVariable UUID id,
            @Valid @RequestBody SpecialOfferRequestVM specialOfferRequestVM) {
        SpecialOfferDTO specialOfferDTO = specialOfferService.updateOffer(id, specialOfferRequestVM);
        SpecialOfferResponseVM specialOfferResponseVM = SpecialOfferResponseVM.map(specialOfferDTO);
        return successResponse(specialOfferResponseVM);
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete special offer", description = "Deletes a special offer by its ID")
    public ResponseEntity<ApiResponse<Boolean>> deleteOffer(
            @Parameter(description = "ID of the special offer") @PathVariable UUID id) {
        Boolean deletionStatus = specialOfferService.deleteOffer(id);
        return successResponse(deletionStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.DUPLICATE_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.DUPLICATE_SPECIAL_OFFER_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);
        else if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.INVALID_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.INVALID_SPECIAL_OFFER_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);

        return failedResponse(ExceptionConstant.Exception.COMMON_EXCEPTION.getCode(),
                illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
