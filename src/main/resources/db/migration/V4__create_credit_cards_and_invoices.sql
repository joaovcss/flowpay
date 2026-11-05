CREATE TABLE credit_cards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) NOT NULL,
    name VARCHAR(100) NOT NULL,
    institution VARCHAR(100) NOT NULL,
    credit_limit NUMERIC(15, 2) NOT NULL,
    closing_day SMALLINT NOT NULL,
    due_day SMALLINT NOT NULL,
    archived_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT uq_credit_cards_user_id UNIQUE (user_id, id),
    CONSTRAINT chk_credit_cards_credit_limit CHECK (credit_limit >= 0),
    CONSTRAINT chk_credit_cards_closing_day CHECK (closing_day BETWEEN 1 AND 31),
    CONSTRAINT chk_credit_cards_due_day CHECK (due_day BETWEEN 1 AND 31)
);

CREATE TABLE invoices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    card_id UUID NOT NULL,
    reference_month DATE NOT NULL,
    closing_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(10) DEFAULT 'OPEN' NOT NULL,
    paid_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_invoices_card FOREIGN KEY (user_id, card_id) REFERENCES credit_cards(user_id, id),
    CONSTRAINT uq_invoices_card_id_reference_month UNIQUE (card_id, reference_month),
    CONSTRAINT uq_invoices_id_user UNIQUE (id, user_id),
    CONSTRAINT chk_invoices_status CHECK (status IN ('OPEN','CLOSED','PAID')),
    CONSTRAINT chk_invoices_paid_status CHECK ((status = 'PAID') = (paid_at IS NOT NULL)),
    CONSTRAINT chk_invoices_dates CHECK (due_date >= closing_date)
);