CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    account_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    occurred_on DATE NOT NULL,
    payment_method VARCHAR(20) NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_transactions_type CHECK (type IN ('INCOME','EXPENSE','INVOICE_PAYMENT')),
    CONSTRAINT chk_transactions_amount CHECK (amount > 0),
    CONSTRAINT chk_transactions_payment_method CHECK (payment_method IN ('PIX','DEBIT_CARD','BOLETO','CASH','TRANSFER','OTHER')),
    CONSTRAINT fk_transactions_account FOREIGN KEY (user_id, account_id) REFERENCES bank_accounts(user_id, id)
);

CREATE INDEX idx_transactions_account_date ON transactions(account_id, occurred_on);
CREATE INDEX idx_transactions_user_date ON transactions(user_id, occurred_on DESC);