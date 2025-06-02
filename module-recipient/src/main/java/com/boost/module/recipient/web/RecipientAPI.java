package com.boost.module.recipient.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.recipient.model.RecipientRequestVM;
import com.boost.module.recipient.model.RecipientResponseVM;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
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

@RequestMapping(value = "/recipients", produces = APPLICATION_JSON)
public interface RecipientAPI {
    @PostMapping
    ResponseEntity<ApiResponse<RecipientResponseVM>> createRecipient(@Valid @RequestBody RecipientRequestVM recipientRequestVM);

    @GetMapping
    ResponseEntity<ApiResponse<List<RecipientResponseVM>>> getAllRecipients();

    @GetMapping
    ResponseEntity<ApiResponse<RecipientResponseVM>> getRecipientByEmail(
            @Parameter(description = "Email of the recipient") @PathVariable String email);

    @PutMapping
    ResponseEntity<ApiResponse<RecipientResponseVM>> updateRecipient(
            @Parameter(description = "ID of the recipient") @PathVariable UUID id,
            @Valid @RequestBody RecipientRequestVM recipientRequestVM);

    @DeleteMapping
    ResponseEntity<ApiResponse<Boolean>> deleteRecipient(
            @Parameter(description = "ID of the recipient") @PathVariable UUID id);
}
