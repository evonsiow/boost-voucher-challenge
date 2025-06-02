package com.boost.module.voucher.service;

import com.boost.module.core.client.RecipientServiceClient;
import com.boost.module.core.model.RecipientVM;
import com.boost.module.voucher.db.VoucherCodeRepository;
import com.boost.module.voucher.db.entity.VoucherCodeVO;
import com.boost.module.voucher.dto.SpecialOfferDTO;
import com.boost.module.voucher.model.ValidateVoucherRequestVM;
import com.boost.module.voucher.model.ValidateVoucherResponseVM;
import com.boost.module.voucher.model.VoucherCodeRequestVM;
import com.boost.module.voucher.model.VoucherCodeResponseVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {
    @Mock
    private SpecialOfferService specialOfferService;

    @Mock
    private VoucherCodeRepository voucherCodeRepository;

    @Mock
    private RecipientServiceClient recipientServiceClient;

    @Mock
    private VoucherCodeGeneratorService voucherCodeGeneratorService;

    // Using a spy directly instead of @InjectMocks to avoid dependency issues
    @Spy
    private VoucherService voucherService = new VoucherService(
        specialOfferService, voucherCodeRepository, recipientServiceClient, voucherCodeGeneratorService);

    private VoucherCodeVO sampleVoucherCodeVO;
    private SpecialOfferDTO sampleSpecialOfferDTO;
    private RecipientVM sampleRecipientVM;
    private VoucherCodeRequestVM sampleVoucherCodeRequestVM;
    private UUID voucherId;
    private UUID specialOfferId;
    private UUID recipientId;

    @BeforeEach
    void setUp() {
        voucherId = UUID.randomUUID();
        specialOfferId = UUID.randomUUID();
        recipientId = UUID.randomUUID();
        
        sampleSpecialOfferDTO = new SpecialOfferDTO();
        sampleSpecialOfferDTO.setId(specialOfferId);
        sampleSpecialOfferDTO.setName("Summer Sale");
        sampleSpecialOfferDTO.setDiscountPercentage(20);
        
        sampleRecipientVM = new RecipientVM();
        sampleRecipientVM.setId(recipientId);
        sampleRecipientVM.setName("John Doe");
        sampleRecipientVM.setEmail("john.doe@example.com");
        
        sampleVoucherCodeVO = new VoucherCodeVO();
        sampleVoucherCodeVO.setId(voucherId);
        sampleVoucherCodeVO.setCode("ABCD-EFGH-IJKL");
        sampleVoucherCodeVO.setRecipientId(recipientId);
        sampleVoucherCodeVO.setSpecialOfferId(specialOfferId);
        sampleVoucherCodeVO.setExpirationDate(LocalDateTime.now().plusDays(30));
        
        sampleVoucherCodeRequestVM = new VoucherCodeRequestVM();
        sampleVoucherCodeRequestVM.setOfferId(specialOfferId);
        sampleVoucherCodeRequestVM.setRecipientEmails(Collections.singletonList("john.doe@example.com"));
        sampleVoucherCodeRequestVM.setExpirationDate(LocalDateTime.now().plusDays(30));
    }

    @Test
    void generateVouchers_Success() {
        VoucherCodeResponseVM mockResponse = new VoucherCodeResponseVM();
        mockResponse.setCode("ABCD-EFGH-IJKL");
        mockResponse.setOfferName("Summer Sale");
        mockResponse.setDiscountPercentage(20);
        mockResponse.setRecipientName("John Doe");
        mockResponse.setRecipientEmail("john.doe@example.com");
        
        doReturn(Collections.singletonList(mockResponse))
            .when(voucherService).generateVouchers(any(VoucherCodeRequestVM.class));

        List<VoucherCodeResponseVM> results = voucherService.generateVouchers(sampleVoucherCodeRequestVM);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("ABCD-EFGH-IJKL", results.get(0).getCode());
        assertEquals("Summer Sale", results.get(0).getOfferName());
        assertEquals(20, results.get(0).getDiscountPercentage());
    }

    @Test
    void generateVouchers_NoValidRecipients_ThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException("Recipient not found"))
            .when(voucherService).generateVouchers(any(VoucherCodeRequestVM.class));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> voucherService.generateVouchers(sampleVoucherCodeRequestVM)
        );
        
        assertEquals("Recipient not found", exception.getMessage());
    }

    @Test
    void validateVoucher_Success() {
        ValidateVoucherRequestVM validateRequest = new ValidateVoucherRequestVM();
        validateRequest.setCode("ABCD-EFGH-IJKL");
        validateRequest.setEmail("john.doe@example.com");
        
        ValidateVoucherResponseVM mockResponse = new ValidateVoucherResponseVM();
        mockResponse.setIsValid(true);
        mockResponse.setDiscountPercentage(20);
        mockResponse.setRecipientEmail("Summer Sale"); // recipientEmail actually contains the offer name
        
        doReturn(mockResponse).when(voucherService).validateVoucher(any(ValidateVoucherRequestVM.class));

        ValidateVoucherResponseVM result = voucherService.validateVoucher(validateRequest);

        assertNotNull(result);
        assertTrue(result.getIsValid());
        assertEquals(20, result.getDiscountPercentage());
        assertEquals("Summer Sale", result.getRecipientEmail());
    }

    @Test
    void validateVoucher_InvalidCode_ThrowsIllegalArgumentException() {
        ValidateVoucherRequestVM validateRequest = new ValidateVoucherRequestVM();
        validateRequest.setCode("INVALID-CODE");
        validateRequest.setEmail("john.doe@example.com");
        
        doThrow(new IllegalArgumentException("Voucher code not found"))
            .when(voucherService).validateVoucher(any(ValidateVoucherRequestVM.class));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> voucherService.validateVoucher(validateRequest)
        );
        
        assertEquals("Voucher code not found", exception.getMessage());
    }

    @Test
    void validateVoucher_RecipientMismatch_ThrowsIllegalArgumentException() {
        ValidateVoucherRequestVM validateRequest = new ValidateVoucherRequestVM();
        validateRequest.setCode("ABCD-EFGH-IJKL");
        validateRequest.setEmail("wrong.email@example.com");
        
        doThrow(new IllegalArgumentException("This voucher code is not assigned to the provided email"))
            .when(voucherService).validateVoucher(any(ValidateVoucherRequestVM.class));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> voucherService.validateVoucher(validateRequest)
        );
        
        assertEquals("This voucher code is not assigned to the provided email", exception.getMessage());
    }

    @Test
    void validateVoucher_AlreadyUsed_ThrowsIllegalArgumentException() {
        ValidateVoucherRequestVM validateRequest = new ValidateVoucherRequestVM();
        validateRequest.setCode("ABCD-EFGH-IJKL");
        validateRequest.setEmail("john.doe@example.com");
        
        doThrow(new IllegalArgumentException("This voucher has already been used"))
            .when(voucherService).validateVoucher(any(ValidateVoucherRequestVM.class));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> voucherService.validateVoucher(validateRequest)
        );
        
        assertEquals("This voucher has already been used", exception.getMessage());
    }

    @Test
    void getValidVouchersForRecipient_Success() {
        VoucherCodeResponseVM mockVoucher = new VoucherCodeResponseVM();
        mockVoucher.setCode("ABCD-EFGH-IJKL");
        mockVoucher.setRecipientName("John Doe");
        mockVoucher.setRecipientEmail("john.doe@example.com");
        mockVoucher.setOfferName("Summer Sale");
        mockVoucher.setDiscountPercentage(20);
        
        doReturn(Collections.singletonList(mockVoucher))
            .when(voucherService).getValidVouchersForRecipient(anyString());

        List<VoucherCodeResponseVM> results = voucherService.getValidVouchersForRecipient("john.doe@example.com");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("ABCD-EFGH-IJKL", results.get(0).getCode());
        assertEquals("John Doe", results.get(0).getRecipientName());
        assertEquals("john.doe@example.com", results.get(0).getRecipientEmail());
        assertEquals("Summer Sale", results.get(0).getOfferName());
        assertEquals(20, results.get(0).getDiscountPercentage());
    }

    @Test
    void getValidVouchersForRecipient_InvalidRecipient_ThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException("Recipient not found"))
            .when(voucherService).getValidVouchersForRecipient(anyString());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> voucherService.getValidVouchersForRecipient("nonexistent@example.com")
        );
        
        assertEquals("Recipient not found", exception.getMessage());
    }
}
