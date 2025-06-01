package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.boost.module.core.util.APIConstant.APPLICATION_JSON;

@RequestMapping(value = "/vouchers", produces = APPLICATION_JSON)
public interface VoucherAPI {
    ResponseEntity<ApiResponse<Boolean>> test();
}
