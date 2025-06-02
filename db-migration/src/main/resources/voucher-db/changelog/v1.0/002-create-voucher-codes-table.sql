--changeset author:create-voucher-codes-table
CREATE TABLE voucher_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(20) UNIQUE NOT NULL,
    recipient_id UUID NOT NULL,
    special_offer_id UUID NOT NULL,
    expiration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    used_date TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (special_offer_id) REFERENCES special_offers(id) ON DELETE CASCADE
);

-- Note: recipient_id references recipients table in user_db, so no foreign key constraint
-- Application layer will handle referential integrity between services

CREATE INDEX idx_voucher_codes_code ON voucher_codes(code);
CREATE INDEX idx_voucher_codes_recipient_id ON voucher_codes(recipient_id);
CREATE INDEX idx_voucher_codes_special_offer_id ON voucher_codes(special_offer_id);
CREATE INDEX idx_voucher_codes_expiration_date ON voucher_codes(expiration_date);
CREATE INDEX idx_voucher_codes_used_date ON voucher_codes(used_date);
--rollback DROP TABLE voucher_codes; 