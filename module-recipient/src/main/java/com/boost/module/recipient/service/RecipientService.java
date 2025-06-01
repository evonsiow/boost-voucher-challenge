package com.boost.module.recipient.service;

import com.boost.module.recipient.db.RecipientRepository;
import com.boost.module.recipient.db.entity.RecipientVO;
import com.boost.module.recipient.dto.RecipientDTO;
import com.boost.module.recipient.model.RecipientCreationRequestVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipientService {
    private final RecipientRepository recipientRepository;

    public RecipientService(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    public RecipientDTO createRecipient(RecipientCreationRequestVM recipientCreationRequestVM) {
        // Check if email already exists
        if (recipientRepository.existsByEmail(recipientCreationRequestVM.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + recipientCreationRequestVM.getEmail());
        }

        // Create new recipient
        RecipientVO recipientVO = new RecipientVO();
        recipientVO.setName(recipientCreationRequestVM.getName());
        recipientVO.setEmail(recipientCreationRequestVM.getEmail());

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
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found with email: " + email));
        return RecipientDTO.map(recipientVO);
    }
}
