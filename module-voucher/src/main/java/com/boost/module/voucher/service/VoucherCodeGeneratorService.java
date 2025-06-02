package com.boost.module.voucher.service;

import com.boost.module.voucher.db.VoucherCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class VoucherCodeGeneratorService {
    private final VoucherCodeRepository voucherCodeRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${voucher.code.length:12}")
    private int codeLength;

    @Value("${voucher.code.characters:ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789}")
    private String characters;

    @Autowired
    public VoucherCodeGeneratorService(VoucherCodeRepository voucherCodeRepository) {
        this.voucherCodeRepository = voucherCodeRepository;
    }

    public String generateUniqueCode() {
        String code;
        int attempts = 0;
        int maxAttempts = 10;

        do {
            code = generateRandomCode();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("Unable to generate unique voucher code after " + maxAttempts + " attempts");
            }
        } while (voucherCodeRepository.existsByCode(code));

        return code;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }

        // Add formatting for readability (e.g., XXXX-XXXX-XXXX)
        if (codeLength == 12) {
            return formatCode(code.toString());
        }

        return code.toString();
    }

    private String formatCode(String code) {
        return code.substring(0, 4) + "-" +
                code.substring(4, 8) + "-" +
                code.substring(8, 12);
    }
}
