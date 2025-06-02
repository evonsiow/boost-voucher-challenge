package com.boost.module.voucher.service;

import com.boost.module.voucher.db.VoucherCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherCodeGeneratorServiceTest {
    @Mock
    private VoucherCodeRepository voucherCodeRepository;

    @InjectMocks
    private VoucherCodeGeneratorService voucherCodeGeneratorService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(voucherCodeGeneratorService, "codeLength", 12);
        ReflectionTestUtils.setField(voucherCodeGeneratorService, "characters", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    @Test
    void generateUniqueCode_Success() {
        when(voucherCodeRepository.existsByCode(anyString())).thenReturn(false);

        String generatedCode = voucherCodeGeneratorService.generateUniqueCode();

        assertNotNull(generatedCode);
        assertEquals(14, generatedCode.length());
        assertTrue(generatedCode.matches("[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}"));
        
        verify(voucherCodeRepository).existsByCode(generatedCode);
    }

    @Test
    void generateUniqueCode_ExistingCode_ThenUniqueCode() {
        when(voucherCodeRepository.existsByCode(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        String generatedCode = voucherCodeGeneratorService.generateUniqueCode();

        assertNotNull(generatedCode);
        assertEquals(14, generatedCode.length());
        assertTrue(generatedCode.matches("[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}"));
        
        verify(voucherCodeRepository, times(2)).existsByCode(anyString());
    }

    @Test
    void generateUniqueCode_MaxAttempts_ThrowsRuntimeException() {
        when(voucherCodeRepository.existsByCode(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> voucherCodeGeneratorService.generateUniqueCode()
        );
        
        assertTrue(exception.getMessage().contains("Unable to generate unique voucher code after 10 attempts"));
        verify(voucherCodeRepository, times(9)).existsByCode(anyString());
    }
}
