package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.voucher.model.ValidateVoucherRequestVM;
import com.boost.module.voucher.model.VoucherCodeResponseVM;
import com.boost.module.voucher.model.VoucherCodeRequestVM;
import com.boost.module.voucher.model.ValidateVoucherResponseVM;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.boost.module.core.util.APIConstant.APPLICATION_JSON;

@RequestMapping(value = "/vouchers", produces = APPLICATION_JSON)
public interface VoucherAPI {
    @PostMapping
    ResponseEntity<ApiResponse<List<VoucherCodeResponseVM>>> generateVoucherCodes(
            @Valid @RequestBody VoucherCodeRequestVM voucherCodeRequestVM);

    @PostMapping("/validate")
    ResponseEntity<ApiResponse<ValidateVoucherResponseVM>> validateVoucher(
            @Valid @RequestBody ValidateVoucherRequestVM validateVoucherRequestVM);
}
