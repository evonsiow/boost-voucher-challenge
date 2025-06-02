package com.boost.module.voucher.service;

import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.voucher.db.SpecialOfferRepository;
import com.boost.module.voucher.db.entity.SpecialOfferVO;
import com.boost.module.voucher.dto.SpecialOfferDTO;
import com.boost.module.voucher.model.SpecialOfferRequestVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpecialOfferService {
    private final SpecialOfferRepository specialOfferRepository;

    public SpecialOfferService(SpecialOfferRepository specialOfferRepository) {
        this.specialOfferRepository = specialOfferRepository;
    }

    public SpecialOfferDTO createOffer(SpecialOfferRequestVM specialOfferRequestVM) {
        // Check if offer with same name already exists
        if (specialOfferRepository.existsByName(specialOfferRequestVM.getName())) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.DUPLICATE_SPECIAL_OFFER_EXCEPTION.getExceptionMessage());
        }

        SpecialOfferVO specialOfferVO = new SpecialOfferVO();
        specialOfferVO.setName(specialOfferRequestVM.getName());
        specialOfferVO.setDiscountPercentage(specialOfferRequestVM.getDiscountPercentage());

        SpecialOfferVO savedOfferVO = specialOfferRepository.save(specialOfferVO);
        return SpecialOfferVO.map(savedOfferVO);
    }

    @Transactional(readOnly = true)
    public SpecialOfferDTO getOfferById(UUID id) {
        SpecialOfferVO specialOfferVO = specialOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionConstant.Exception.INVALID_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()));
        return SpecialOfferVO.map(specialOfferVO);
    }

    @Transactional(readOnly = true)
    public List<SpecialOfferDTO> getAllOffers() {
        return specialOfferRepository.findAll().stream()
                .map(SpecialOfferDTO::map)
                .collect(Collectors.toList());
    }

    public SpecialOfferDTO updateOffer(UUID id, SpecialOfferRequestVM specialOfferRequestVM) {
        SpecialOfferVO specialOfferVO = specialOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionConstant.Exception.INVALID_SPECIAL_OFFER_EXCEPTION.getExceptionMessage()));

        // Check if another offer with the same name exists
        if (!specialOfferVO.getName().equals(specialOfferRequestVM.getName()) && specialOfferRepository.existsByName(specialOfferRequestVM.getName())) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.DUPLICATE_SPECIAL_OFFER_EXCEPTION.getExceptionMessage());
        }

        specialOfferVO.setName(specialOfferRequestVM.getName());
        specialOfferVO.setDiscountPercentage(specialOfferRequestVM.getDiscountPercentage());

        SpecialOfferVO updatedSpecialOfferVO = specialOfferRepository.save(specialOfferVO);
        return SpecialOfferVO.map(updatedSpecialOfferVO);
    }

    public Boolean deleteOffer(UUID id) {
        if (!specialOfferRepository.existsById(id)) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.INVALID_SPECIAL_OFFER_EXCEPTION.getExceptionMessage());
        }
        specialOfferRepository.deleteById(id);
        return true;
    }
}
