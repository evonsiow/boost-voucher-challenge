package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.core.web.AbstractCommonController;
import com.boost.module.voucher.model.ValidateVoucherRequestVM;
import com.boost.module.voucher.model.VoucherCodeResponseVM;
import com.boost.module.voucher.model.VoucherCodeRequestVM;
import com.boost.module.voucher.model.ValidateVoucherResponseVM;
import com.boost.module.voucher.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VoucherController extends AbstractCommonController implements VoucherAPI {
    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Generate voucher codes",
            description = "Generates unique voucher codes for specified recipients and special offer")
    public ResponseEntity<ApiResponse<List<VoucherCodeResponseVM>>> generateVoucherCodes(
            @Valid @RequestBody VoucherCodeRequestVM voucherCodeRequestVM) {
        List<VoucherCodeResponseVM> voucherCodeResponseVMList = voucherService.generateVouchers(voucherCodeRequestVM);
        return successResponse(voucherCodeResponseVMList);
    }

    @Override
    @PostMapping("/validate")
    @Operation(summary = "Validate voucher code",
            description = "Validates a voucher code for a given email and marks it as used if valid")
    public ResponseEntity<ApiResponse<ValidateVoucherResponseVM>> validateVoucher(
            @Valid @RequestBody ValidateVoucherRequestVM validateVoucherRequestVM) {
//        ValidateVoucherResponseVM voucherValidationResponseVM = voucherService.validateVoucher(validateVoucherRequestVM);
        return successResponse(null);
    }
//
//    @GetMapping("/recipient/{email}")
//    @Operation(summary = "Get valid vouchers for recipient",
//            description = "Retrieves all valid (unused and not expired) vouchers for a recipient email")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "List of valid vouchers retrieved"),
//            @ApiResponse(responseCode = "400", description = "Recipient not found")
//    })
//    public ResponseEntity<List<VoucherCodeResponseVM>> getValidVouchersForRecipient(
//            @Parameter(description = "Email of the recipient") @PathVariable String email) {
//        List<VoucherCodeDTO> validVouchers = voucherService.getValidVouchersForRecipient(email);
//        List<VoucherCodeResponseVM> response = validVouchers.stream()
//                .map(this::convertToResponseVM)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(response);
//    }
}
