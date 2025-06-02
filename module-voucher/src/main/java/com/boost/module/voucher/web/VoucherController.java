package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.core.web.AbstractCommonController;
import com.boost.module.voucher.model.ValidateVoucherRequestVM;
import com.boost.module.voucher.model.VoucherCodeResponseVM;
import com.boost.module.voucher.model.VoucherCodeRequestVM;
import com.boost.module.voucher.model.ValidateVoucherResponseVM;
import com.boost.module.voucher.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        ValidateVoucherResponseVM voucherValidationResponseVM = voucherService.validateVoucher(validateVoucherRequestVM);
        return successResponse(voucherValidationResponseVM);
    }

    @Override
    @GetMapping("/recipients/{email}")
    @Operation(summary = "Get valid vouchers for recipient",
            description = "Retrieves all valid (unused and not expired) vouchers for a recipient email")
    public ResponseEntity<ApiResponse<List<VoucherCodeResponseVM>>> getValidVouchersForRecipient(
            @Parameter(description = "Email of the recipient") @PathVariable String email) {
        List<VoucherCodeResponseVM> voucherCodeResponseVMList = voucherService.getValidVouchersForRecipient(email);
        return successResponse(voucherCodeResponseVMList);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.INVALID_VOUCHER_CODE_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.INVALID_VOUCHER_CODE_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);
        else if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);
        else if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.INVALID_VOUCHER_RECIPIENT_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.INVALID_VOUCHER_RECIPIENT_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);
        else if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.USED_VOUCHER_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.USED_VOUCHER_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);
        else if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.EXPIRED_VOUCHER_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.EXPIRED_VOUCHER_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);

        return failedResponse(ExceptionConstant.Exception.COMMON_EXCEPTION.getCode(),
                illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
