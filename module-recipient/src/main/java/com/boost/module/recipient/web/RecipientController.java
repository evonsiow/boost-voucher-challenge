package com.boost.module.recipient.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.core.web.AbstractCommonController;
import com.boost.module.recipient.dto.RecipientDTO;
import com.boost.module.recipient.model.RecipientRequestVM;
import com.boost.module.recipient.model.RecipientResponseVM;
import com.boost.module.recipient.service.RecipientService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
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
public class RecipientController extends AbstractCommonController implements RecipientAPI {
    private final RecipientService recipientService;

    public RecipientController(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new recipient", description = "Creates a new recipient with unique email")
    public ResponseEntity<ApiResponse<RecipientResponseVM>> createRecipient(@Valid @RequestBody RecipientRequestVM recipientRequestVM) {
        RecipientDTO recipientDTO = recipientService.createRecipient(recipientRequestVM);
        RecipientResponseVM responseVM = RecipientResponseVM.map(recipientDTO);
        return successResponse(responseVM);
    }

    @Override
    @GetMapping
    @Operation(summary = "Get all recipients", description = "Retrieves all recipients in the system")
    public ResponseEntity<ApiResponse<List<RecipientResponseVM>>> getAllRecipients() {
        List<RecipientDTO> recipientDTOList = recipientService.getAllRecipients();
        List<RecipientResponseVM> recipientResponseVMList = recipientDTOList.stream()
                .map(RecipientResponseVM::map)
                .collect(Collectors.toList());
        return successResponse(recipientResponseVMList);
    }

    @Override
    @GetMapping("/{email}")
    @Operation(summary = "Get recipient by email", description = "Retrieves a recipient by their email address")
    public ResponseEntity<ApiResponse<RecipientResponseVM>> getRecipientByEmail(
            @Parameter(description = "Email of the recipient") @PathVariable String email) {
        RecipientDTO recipientDTO = recipientService.getRecipientByEmail(email);
        RecipientResponseVM recipientResponseVM = RecipientResponseVM.map(recipientDTO);
        return successResponse(recipientResponseVM);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update recipient", description = "Updates an existing recipient")
    public ResponseEntity<ApiResponse<RecipientResponseVM>> updateRecipient(
            @Parameter(description = "ID of the recipient") @PathVariable UUID id,
            @Valid @RequestBody RecipientRequestVM recipientRequestVM) {
        RecipientDTO recipientDTO = recipientService.updateRecipient(id, recipientRequestVM);
        RecipientResponseVM recipientResponseVM = RecipientResponseVM.map(recipientDTO);
        return successResponse(recipientResponseVM);
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recipient", description = "Deletes a recipient by their ID")
    public ResponseEntity<ApiResponse<Boolean>> deleteRecipient(
            @Parameter(description = "ID of the recipient") @PathVariable UUID id) {
        Boolean deletionStatus = recipientService.deleteRecipient(id);
        return successResponse(deletionStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.DUPLICATE_EMAIL_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.DUPLICATE_EMAIL_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);
        else if (illegalArgumentException.getMessage().contains(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage()))
            return failedResponse(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getCode(),
                    illegalArgumentException.getMessage(), HttpStatus.CONFLICT);

        return failedResponse(ExceptionConstant.Exception.COMMON_EXCEPTION.getCode(),
                illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
