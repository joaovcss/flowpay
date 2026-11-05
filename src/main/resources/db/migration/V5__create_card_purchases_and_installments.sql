CREATE TABLE card_purchases (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    card_id UUID NOT NULL,
    description VARCHAR(255) NOT NULL,
    total_amount NUMERIC(15, 2) NOT NULL,
    installments_count SMALLINT NOT NULL,
    purchase_date DATE NOT NULL,
    has_interest BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_card_purchases_user_id UNIQUE (user_id, id),
    CONSTRAINT fk_card_purchases_credit_card FOREIGN KEY (user_id, card_id) REFERENCES credit_cards(user_id, id),
    CONSTRAINT chk_card_purchases_total_amount CHECK (total_amount > 0),
    CONSTRAINT chk_card_purchases_installments_count CHECK (installments_count BETWEEN 1 AND 48)
);

CREATE TABLE installments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    purchase_id UUID NOT NULL,
    invoice_id UUID NOT NULL,
    number SMALLINT NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_installments_purchase_number UNIQUE (purchase_id, number),
    CONSTRAINT fk_installments_purchase FOREIGN KEY (user_id, purchase_id) REFERENCES card_purchases(user_id, id) ON DELETE CASCADE,
    CONSTRAINT fk_installments_invoice FOREIGN KEY (user_id, invoice_id) REFERENCES invoices(user_id, id),
    CONSTRAINT chk_installments_number CHECK (number >= 1),
    CONSTRAINT chk_installments_amount CHECK (amount > 0)
);

CREATE INDEX idx_installments_invoice_id ON installments (invoice_id);
CREATE INDEX idx_card_purchases_user_date ON card_purchases (user_id, purchase_date);