package com.boost.module.voucher.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.core.web.AbstractCommonController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoucherController extends AbstractCommonController implements VoucherAPI {
    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Boolean>> test() {
        return successResponse("hihi", true);
    }
}
