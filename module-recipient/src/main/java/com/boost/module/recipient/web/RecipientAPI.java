package com.boost.module.recipient.web;

import com.boost.module.core.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/recipients", produces = "application/json")
public interface RecipientAPI {
    ResponseEntity<ApiResponse<Boolean>> test();
}
