CREATE TABLE transfers (
    id UUID  PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_transfers_user_id UNIQUE (user_id, id)
);

ALTER TABLE transactions
ADD COLUMN transfer_id UUID NULL,

DROP CONSTRAINT chk_transactions_type,

ADD CONSTRAINT chk_transactions_type CHECK (type IN ('INCOME','EXPENSE','INVOICE_PAYMENT','TRANSFER_OUT','TRANSFER_IN')),
ADD CONSTRAINT fk_transactions_transfer FOREIGN KEY (user_id, transfer_id) REFERENCES transfers(user_id, id),
ADD CONSTRAINT uq_transactions_transfer_type UNIQUE (transfer_id, type),
ADD CONSTRAINT chk_transactions_transfer CHECK ((type IN ('TRANSFER_OUT','TRANSFER_IN')) = (transfer_id IS NOT NULL));
