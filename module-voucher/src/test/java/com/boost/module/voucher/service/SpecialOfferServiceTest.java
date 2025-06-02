package com.boost.module.voucher.service;

import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.voucher.db.SpecialOfferRepository;
import com.boost.module.voucher.db.entity.SpecialOfferVO;
import com.boost.module.voucher.dto.SpecialOfferDTO;
import com.boost.module.voucher.model.SpecialOfferRequestVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialOfferServiceTest {
    @Mock
    private SpecialOfferRepository specialOfferRepository;

    @InjectMocks
    private SpecialOfferService specialOfferService;

    private SpecialOfferVO sampleSpecialOfferVO;

    private SpecialOfferRequestVM sampleRequestVM;

    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        
        sampleSpecialOfferVO = new SpecialOfferVO();
        sampleSpecialOfferVO.setId(sampleId);
        sampleSpecialOfferVO.setName("Summer Sale");
        sampleSpecialOfferVO.setDiscountPercentage(20);
        
        sampleRequestVM = new SpecialOfferRequestVM();
        sampleRequestVM.setName("Summer Sale");
        sampleRequestVM.setDiscountPercentage(20);
    }

    @Test
    void createOffer_Success() {
        when(specialOfferRepository.existsByName(anyString())).thenReturn(false);
        when(specialOfferRepository.save(any(SpecialOfferVO.class))).thenReturn(sampleSpecialOfferVO);

        SpecialOfferDTO result = specialOfferService.createOffer(sampleRequestVM);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        assertEquals("Summer Sale", result.getName());
        assertEquals(20, result.getDiscountPercentage());
        
        verify(specialOfferRepository).existsByName("Summer Sale");
        verify(specialOfferRepository).save(any(SpecialOfferVO.class));
    }

    @Test
    void createOffer_DuplicateName_ThrowsIllegalArgumentException() {
        when(specialOfferRepository.existsByName(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> specialOfferService.createOffer(sampleRequestVM)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.DUPLICATE_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()));
        verify(specialOfferRepository).existsByName("Summer Sale");
        verify(specialOfferRepository, never()).save(any(SpecialOfferVO.class));
    }

    @Test
    void getOfferById_Success() {
        when(specialOfferRepository.findById(any(UUID.class))).thenReturn(Optional.of(sampleSpecialOfferVO));

        SpecialOfferDTO result = specialOfferService.getOfferById(sampleId);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        assertEquals("Summer Sale", result.getName());
        assertEquals(20, result.getDiscountPercentage());
        
        verify(specialOfferRepository).findById(sampleId);
    }

    @Test
    void getOfferById_NotFound_ThrowsIllegalArgumentException() {
        when(specialOfferRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> specialOfferService.getOfferById(sampleId)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.INVALID_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()));
        verify(specialOfferRepository).findById(sampleId);
    }

    @Test
    void getAllOffers_Success() {
        SpecialOfferVO secondOffer = new SpecialOfferVO();
        secondOffer.setId(UUID.randomUUID());
        secondOffer.setName("Winter Deal");
        secondOffer.setDiscountPercentage(15);
        
        when(specialOfferRepository.findAll()).thenReturn(Arrays.asList(sampleSpecialOfferVO, secondOffer));

        List<SpecialOfferDTO> results = specialOfferService.getAllOffers();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Summer Sale", results.get(0).getName());
        assertEquals("Winter Deal", results.get(1).getName());
        assertEquals(20, results.get(0).getDiscountPercentage());
        assertEquals(15, results.get(1).getDiscountPercentage());
        
        verify(specialOfferRepository).findAll();
    }

    @Test
    void updateOffer_Success() {
        SpecialOfferRequestVM updateRequest = new SpecialOfferRequestVM();
        updateRequest.setName("Summer Super Sale");
        updateRequest.setDiscountPercentage(25);
        
        SpecialOfferVO updatedOffer = new SpecialOfferVO();
        updatedOffer.setId(sampleId);
        updatedOffer.setName("Summer Super Sale");
        updatedOffer.setDiscountPercentage(25);
        
        when(specialOfferRepository.findById(any(UUID.class))).thenReturn(Optional.of(sampleSpecialOfferVO));
        when(specialOfferRepository.existsByName(anyString())).thenReturn(false);
        when(specialOfferRepository.save(any(SpecialOfferVO.class))).thenReturn(updatedOffer);

        SpecialOfferDTO result = specialOfferService.updateOffer(sampleId, updateRequest);

        assertNotNull(result);
        assertEquals(sampleId, result.getId());
        assertEquals("Summer Super Sale", result.getName());
        assertEquals(25, result.getDiscountPercentage());
        
        verify(specialOfferRepository).findById(sampleId);
        verify(specialOfferRepository).save(any(SpecialOfferVO.class));
    }

    @Test
    void updateOffer_DuplicateName_ThrowsIllegalArgumentException() {
        SpecialOfferRequestVM updateRequest = new SpecialOfferRequestVM();
        updateRequest.setName("Winter Deal");
        updateRequest.setDiscountPercentage(25);
        
        when(specialOfferRepository.findById(any(UUID.class))).thenReturn(Optional.of(sampleSpecialOfferVO));
        when(specialOfferRepository.existsByName("Winter Deal")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> specialOfferService.updateOffer(sampleId, updateRequest)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.DUPLICATE_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()));
        verify(specialOfferRepository).findById(sampleId);
        verify(specialOfferRepository, never()).save(any(SpecialOfferVO.class));
    }

    @Test
    void deleteOffer_Success() {
        when(specialOfferRepository.existsById(any(UUID.class))).thenReturn(true);
        doNothing().when(specialOfferRepository).deleteById(any(UUID.class));

        Boolean result = specialOfferService.deleteOffer(sampleId);

        assertTrue(result);
        verify(specialOfferRepository).existsById(sampleId);
        verify(specialOfferRepository).deleteById(sampleId);
    }

    @Test
    void deleteOffer_NotFound_ThrowsIllegalArgumentException() {
        when(specialOfferRepository.existsById(any(UUID.class))).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> specialOfferService.deleteOffer(sampleId)
        );
        
        assertTrue(exception.getMessage().contains(ExceptionConstant.Exception.INVALID_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()));
        verify(specialOfferRepository).existsById(sampleId);
        verify(specialOfferRepository, never()).deleteById(any(UUID.class));
    }
}
