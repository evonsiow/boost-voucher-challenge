package com.boost.module.recipient.service;

import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.recipient.db.RecipientRepository;
import com.boost.module.recipient.db.entity.RecipientVO;
import com.boost.module.recipient.dto.RecipientDTO;
import com.boost.module.recipient.model.RecipientRequestVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipientService {
    private final RecipientRepository recipientRepository;

    public RecipientService(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    public RecipientDTO createRecipient(RecipientRequestVM recipientRequestVM) {
        // Check if email already exists
        if (recipientRepository.existsByEmail(recipientRequestVM.getEmail())) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.DUPLICATE_EMAIL_EXCEPTION.getExceptionMessage());
        }

        // Create a new recipient
        RecipientVO recipientVO = new RecipientVO();
        recipientVO.setName(recipientRequestVM.getName());
        recipientVO.setEmail(recipientRequestVM.getEmail());

        // Save and convert to DTO
        RecipientVO savedRecipientVO = recipientRepository.save(recipientVO);
        return RecipientDTO.map(savedRecipientVO);
    }

    @Transactional(readOnly = true)
    public List<RecipientDTO> getAllRecipients() {
        return recipientRepository.findAll().stream()
                .map(RecipientVO::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecipientDTO getRecipientByEmail(String email) {
        RecipientVO recipientVO = recipientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage()));
        return RecipientDTO.map(recipientVO);
    }

    public RecipientDTO updateRecipient(UUID id, RecipientRequestVM recipientRequestVM) {
        RecipientVO recipientVO = recipientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage()));

        // Check if the email is being changed and if a new email already exists
        if (!recipientVO.getEmail().equals(recipientRequestVM.getEmail()) &&
                recipientRepository.existsByEmailAndIdNot(recipientRequestVM.getEmail(), id)) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.DUPLICATE_EMAIL_EXCEPTION.getExceptionMessage());
        }

        recipientVO.setName(recipientRequestVM.getName());
        recipientVO.setEmail(recipientRequestVM.getEmail());

        RecipientVO savedRecipientVO = recipientRepository.save(recipientVO);
        return RecipientDTO.map(savedRecipientVO);
    }

    public Boolean deleteRecipient(UUID id) {
        if (!recipientRepository.existsById(id)) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage());
        }
        recipientRepository.deleteById(id);
        return true;
    }
}
