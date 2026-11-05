CREATE TABLE bank_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    institution VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    initial_balance NUMERIC(15, 2) NOT NULL DEFAULT 0,
    archived_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT chk_bank_accounts_type CHECK (type IN ('CHECKING','SAVINGS','BENEFIT','CASH')),
    CONSTRAINT uq_bank_accounts_id_user UNIQUE (id, user_id)
);

CREATE INDEX idx_bank_accounts_user_id ON bank_accounts(user_id);

