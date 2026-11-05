CREATE TABLE recurring_incomes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    account_id UUID NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    day_of_month SMALLINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recurring_incomes_account FOREIGN KEY (user_id, account_id) REFERENCES bank_accounts(user_id, id),
    CONSTRAINT uq_recurring_incomes_user_id UNIQUE (user_id, id),
    CONSTRAINT chk_recurring_incomes_amount CHECK (amount > 0),
    CONSTRAINT chk_recurring_incomes_day_of_month CHECK (day_of_month BETWEEN 1 AND 31),
    CONSTRAINT chk_recurring_incomes_end_date CHECK (end_date >= start_date)
);

ALTER TABLE transactions
ADD COLUMN recurring_income_id UUID NULL,
ADD COLUMN reference_month DATE NULL,

ADD CONSTRAINT fk_transactions_recurring_income FOREIGN KEY (user_id, recurring_income_id) REFERENCES recurring_incomes(user_id, id),
ADD CONSTRAINT uq_transactions_recurring_income_reference_month UNIQUE (recurring_income_id, reference_month),
ADD CONSTRAINT chk_transactions_recurring_income_type_or_id CHECK (recurring_income_id IS NULL OR type = 'INCOME'),
ADD CONSTRAINT chk_transactions_recurring_month CHECK ((recurring_income_id IS NULL) = (reference_month IS NULL));


