package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/vouchers", produces = "application/json")
public interface VoucherAPI {
    ResponseEntity<ApiResponse<Boolean>> test();
}
