package com.boost.module.recipient.service;

import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.recipient.db.RecipientRepository;
import com.boost.module.recipient.db.entity.RecipientVO;
import com.boost.module.recipient.dto.RecipientDTO;
import com.boost.module.recipient.model.RecipientRequestVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipientServiceTest {

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private RecipientService recipientService;

    private RecipientVO sampleRecipientVO;
    private RecipientRequestVM sampleRequestVM;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        
        sampleRecipientVO = new RecipientVO();
        sampleRecipientVO.setId(sampleId);
        sampleRecipientVO.setName("John Doe");
        sampleRecipientVO.setEmail("john.doe@example.com");
        sampleRecipientVO.setCreatedAt(LocalDateTime.now());
        sampleRecipientVO.setUpdatedAt(LocalDateTime.now());
        
        sampleRequestVM = new RecipientRequestVM();
        sampleRequestVM.setName("John Doe");
        sampleRequestVM.setEmail("john.doe@example.com");
    }

    @Test
    void createRecipient_Success() {
        when(recipientRepository.existsByEmail(anyString())).thenReturn(false);
        when(recipientRepository.save(any(RecipientVO.class))).thenReturn(sampleRecipientVO);

        RecipientDTO result = recipientService.createRecipient(sampleRequestVM);
        
        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        
        verify(recipientRepository).existsByEmail("john.doe@example.com");
        verify(recipientRepository).save(any(RecipientVO.class));
    }

    @Test
    void createRecipient_DuplicateEmail_ThrowsIllegalArgumentException() {
        when(recipientRepository.existsByEmail(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recipientService.createRecipient(sampleRequestVM)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.DUPLICATE_EMAIL_EXCEPTION.getExceptionMessage()));
        verify(recipientRepository).existsByEmail("john.doe@example.com");
        verify(recipientRepository, never()).save(any(RecipientVO.class));
    }

    @Test
    void getAllRecipients_Success() {
        RecipientVO secondRecipient = new RecipientVO();
        secondRecipient.setId(UUID.randomUUID());
        secondRecipient.setName("Jane Smith");
        secondRecipient.setEmail("jane.smith@example.com");
        
        when(recipientRepository.findAll()).thenReturn(Arrays.asList(sampleRecipientVO, secondRecipient));

        List<RecipientDTO> results = recipientService.getAllRecipients();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("John Doe", results.get(0).getName());
        assertEquals("jane.smith@example.com", results.get(1).getEmail());
        
        verify(recipientRepository).findAll();
    }

    @Test
    void getRecipientByEmail_Success() {
        when(recipientRepository.findByEmail(anyString())).thenReturn(Optional.of(sampleRecipientVO));

        RecipientDTO result = recipientService.getRecipientByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        
        verify(recipientRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void getRecipientByEmail_NotFound_ThrowsIllegalArgumentException() {
        when(recipientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recipientService.getRecipientByEmail("nonexistent@example.com")
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage()));
        verify(recipientRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void updateRecipient_Success() {
        RecipientRequestVM updateRequest = new RecipientRequestVM();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john.updated@example.com");
        
        RecipientVO updatedRecipient = new RecipientVO();
        updatedRecipient.setId(sampleId);
        updatedRecipient.setName("John Updated");
        updatedRecipient.setEmail("john.updated@example.com");
        
        when(recipientRepository.findById(any(UUID.class))).thenReturn(Optional.of(sampleRecipientVO));
        when(recipientRepository.existsByEmailAndIdNot(anyString(), any(UUID.class))).thenReturn(false);
        when(recipientRepository.save(any(RecipientVO.class))).thenReturn(updatedRecipient);

        RecipientDTO result = recipientService.updateRecipient(sampleId, updateRequest);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        assertEquals("John Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());
        
        verify(recipientRepository).findById(sampleId);
        verify(recipientRepository).existsByEmailAndIdNot("john.updated@example.com", sampleId);
        verify(recipientRepository).save(any(RecipientVO.class));
    }

    @Test
    void updateRecipient_NotFound_ThrowsIllegalArgumentException() {
        when(recipientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recipientService.updateRecipient(sampleId, sampleRequestVM)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage()));
        verify(recipientRepository).findById(sampleId);
        verify(recipientRepository, never()).save(any(RecipientVO.class));
    }

    @Test
    void updateRecipient_DuplicateEmail_ThrowsIllegalArgumentException() {
        RecipientRequestVM updateRequest = new RecipientRequestVM();
        updateRequest.setName("John Doe");
        updateRequest.setEmail("duplicate@example.com");
        
        when(recipientRepository.findById(any(UUID.class))).thenReturn(Optional.of(sampleRecipientVO));
        when(recipientRepository.existsByEmailAndIdNot(anyString(), any(UUID.class))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recipientService.updateRecipient(sampleId, updateRequest)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.DUPLICATE_EMAIL_EXCEPTION.getExceptionMessage()));
        verify(recipientRepository).findById(sampleId);
        verify(recipientRepository).existsByEmailAndIdNot("duplicate@example.com", sampleId);
        verify(recipientRepository, never()).save(any(RecipientVO.class));
    }

    @Test
    void deleteRecipient_Success() {
        when(recipientRepository.existsById(any(UUID.class))).thenReturn(true);
        doNothing().when(recipientRepository).deleteById(any(UUID.class));

        Boolean result = recipientService.deleteRecipient(sampleId);

        assertTrue(result);
        verify(recipientRepository).existsById(sampleId);
        verify(recipientRepository).deleteById(sampleId);
    }

    @Test
    void deleteRecipient_NotFound_ThrowsIllegalArgumentException() {
        when(recipientRepository.existsById(any(UUID.class))).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> recipientService.deleteRecipient(sampleId)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage()));
        verify(recipientRepository).existsById(sampleId);
        verify(recipientRepository, never()).deleteById(any(UUID.class));
    }
}
