package com.boost.module.voucher.service;

import com.boost.module.core.client.RecipientServiceClient;
import com.boost.module.core.model.RecipientVM;
import com.boost.module.core.util.ExceptionConstant;
import com.boost.module.voucher.db.VoucherCodeRepository;
import com.boost.module.voucher.db.entity.VoucherCodeVO;
import com.boost.module.voucher.dto.SpecialOfferDTO;
import com.boost.module.voucher.dto.VoucherCodeDTO;
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

    public ValidateVoucherResponseVM validateVoucher(ValidateVoucherRequestVM validateVoucherRequestVM) {
        // Find voucher by code
        VoucherCodeVO voucherCodeVO = voucherCodeRepository.findByCode(validateVoucherRequestVM.getCode())
                .orElseThrow(() -> new IllegalArgumentException(ExceptionConstant.Exception.INVALID_VOUCHER_CODE_EXCEPTION.getExceptionMessage()));

        VoucherCodeDTO voucherCodeDTO = VoucherCodeDTO.map(voucherCodeVO);

        // Get recipient and offer details
        RecipientVM recipientVM = recipientServiceClient.getRecipientByEmail(validateVoucherRequestVM.getEmail());
        if (recipientVM == null) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage());
        }

        // Check if email matches the voucher recipient
        if (!voucherCodeDTO.getRecipientId().equals(recipientVM.getId())) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.INVALID_VOUCHER_RECIPIENT_EXCEPTION.getExceptionMessage());
        }

        // Check if already used
        if (voucherCodeDTO.isUsed()) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.USED_VOUCHER_EXCEPTION.getExceptionMessage());
        }

        // Check if expired
        if (voucherCodeDTO.isExpired()) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.EXPIRED_VOUCHER_EXCEPTION.getExceptionMessage());
        }

        // Get offer details
        SpecialOfferDTO specialOfferDTO = specialOfferService.getOfferById(voucherCodeVO.getSpecialOfferId());

        // Mark voucher as used
        voucherCodeVO.setUsedDate(LocalDateTime.now());
        voucherCodeRepository.save(voucherCodeVO);

        return new ValidateVoucherResponseVM(true, specialOfferDTO.getDiscountPercentage(), specialOfferDTO.getName());
    }

    @Transactional(readOnly = true)
    public List<VoucherCodeResponseVM> getValidVouchersForRecipient(String email) {
        // Get recipient
        RecipientVM recipientVM = recipientServiceClient.getRecipientByEmail(email);
        if (recipientVM == null) {
            throw new IllegalArgumentException(ExceptionConstant.Exception.INVALID_RECIPIENT_EXCEPTION.getExceptionMessage());
        }

        // Get valid vouchers (not used and not expired)
        List<VoucherCodeVO> validVoucherVOList = voucherCodeRepository
                .findByRecipientIdAndUsedDateIsNullAndExpirationDateAfter(recipientVM.getId(), LocalDateTime.now());

        List<VoucherCodeDTO> validVoucherDTOList = validVoucherVOList.stream().map(VoucherCodeDTO::map).toList();

        List<VoucherCodeResponseVM> voucherCodeResponseVMList = new ArrayList<>();

        for (VoucherCodeDTO voucherCodeDTO : validVoucherDTOList) {
            VoucherCodeResponseVM voucherCodeResponseVM = VoucherCodeResponseVM.map(voucherCodeDTO);

            voucherCodeResponseVM.setRecipientEmail(recipientVM.getEmail());
            voucherCodeResponseVM.setRecipientName(recipientVM.getName());

            SpecialOfferDTO specialOfferDTO = specialOfferService.getOfferById(voucherCodeDTO.getSpecialOfferId());
            voucherCodeResponseVM.setOfferName(specialOfferDTO.getName());
            voucherCodeResponseVM.setDiscountPercentage(specialOfferDTO.getDiscountPercentage());
            voucherCodeResponseVMList.add(voucherCodeResponseVM);
        }
        return voucherCodeResponseVMList;
    }
}
