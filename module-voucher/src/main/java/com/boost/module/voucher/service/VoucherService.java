package com.boost.module.voucher.service;

import com.boost.module.core.client.RecipientServiceClient;
import com.boost.module.core.model.RecipientVM;
import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.voucher.db.VoucherCodeRepository;
import com.boost.module.voucher.db.entity.VoucherCodeVO;
import com.boost.module.voucher.dto.SpecialOfferDTO;
import com.boost.module.voucher.dto.VoucherCodeDTO;
import com.boost.module.voucher.model.SpecialOfferResponseVM;
import com.boost.module.voucher.model.ValidateVoucherRequestVM;
import com.boost.module.voucher.model.ValidateVoucherResponseVM;
import com.boost.module.voucher.model.VoucherCodeRequestVM;
import com.boost.module.voucher.model.VoucherCodeResponseVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VoucherService {
    private final SpecialOfferService specialOfferService;
    private final VoucherCodeRepository voucherCodeRepository;
    private final RecipientServiceClient recipientServiceClient;
    private final VoucherCodeGeneratorService voucherCodeGeneratorService;

    public VoucherService(SpecialOfferService specialOfferService, VoucherCodeRepository voucherCodeRepository, RecipientServiceClient recipientServiceClient, VoucherCodeGeneratorService voucherCodeGeneratorService) {
        this.specialOfferService = specialOfferService;
        this.voucherCodeRepository = voucherCodeRepository;
        this.recipientServiceClient = recipientServiceClient;
        this.voucherCodeGeneratorService = voucherCodeGeneratorService;
    }

    public List<VoucherCodeResponseVM> generateVouchers(VoucherCodeRequestVM voucherCodeRequestVM) {
        // Validate special offer exists
        SpecialOfferDTO specialOfferDTO = specialOfferService.getOfferById(voucherCodeRequestVM.getOfferId());

        // Validate all recipients exist
        List<RecipientVM> recipientVMList = new ArrayList<>();
        for (String email : voucherCodeRequestVM.getRecipientEmails()) {
            RecipientVM recipientVM = recipientServiceClient.getRecipientByEmail(email);
            if (recipientVM != null) {
                recipientVMList.add(recipientVM);
            }
        }

        // Handle case where no recipients are found
        if (recipientVMList.isEmpty()) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage());
        }

        // Generate vouchers for each recipient
        List<VoucherCodeResponseVM> voucherCodeResponseVMList = new ArrayList<>();
        for (RecipientVM recipientVM : recipientVMList) {
            String uniqueVoucherCode = voucherCodeGeneratorService.generateUniqueCode();

            VoucherCodeVO voucherCodeVO = new VoucherCodeVO();
            voucherCodeVO.setCode(uniqueVoucherCode);
            voucherCodeVO.setRecipientId(recipientVM.getId());
            voucherCodeVO.setSpecialOfferId(specialOfferDTO.getId());
            voucherCodeVO.setExpirationDate(voucherCodeRequestVM.getExpirationDate());

            VoucherCodeVO savedVoucherVO = voucherCodeRepository.save(voucherCodeVO);
            VoucherCodeDTO voucherCodeDTO = VoucherCodeDTO.map(savedVoucherVO);

            VoucherCodeResponseVM voucherCodeResponseVM = VoucherCodeResponseVM.map(voucherCodeDTO);
            voucherCodeResponseVMList.add(voucherCodeResponseVM);
        }
        return voucherCodeResponseVMList;
    }

//    public ValidateVoucherResponseVM validateVoucher(ValidateVoucherRequestVM validateVoucherRequestVM) {
//        // Find voucher by code
//        VoucherCodeVO voucher = voucherCodeRepository.findByCode(validateVoucherRequestVM.getCode())
//                .orElseThrow(() -> new IllegalArgumentException("Voucher code not found: " + validateVoucherRequestVM.getCode()));
//
//        // Get recipient and offer details
//        RecipientVM recipient = recipientServiceClient.getRecipientByEmail(validateVoucherRequestVM.getEmail());
//        if (recipient == null) {
//            return VoucherValidationResponseVM.invalid("Recipient not found with email: " + validateVoucherRequestVM.getEmail());
//        }
//
//        // Check if email matches the voucher recipient
//        if (!voucher.getRecipientId().equals(recipient.getId())) {
//            throw new RecipientMismatchException("This voucher code is not assigned to the provided email");
//        }
//
//        // Check if already used
//        if (voucher.isUsed()) {
//            throw new VoucherAlreadyUsedException("This voucher has already been used on " + voucher.getUsedDate());
//        }
//
//        // Check if expired
//        if (voucher.isExpired()) {
//            throw new VoucherExpiredException("This voucher expired on " + voucher.getExpirationDate());
//        }
//
//        // Get offer details
//        SpecialOfferDTO offer = specialOfferService.getOfferById(voucher.getSpecialOfferId());
//
//        // Mark voucher as used
//        voucher.setUsedDate(LocalDateTime.now());
//        voucherCodeRepository.save(voucher);
//
//        return VoucherValidationResponseVM.valid(offer.getDiscountPercentage(), offer.getName());
//    }
}
