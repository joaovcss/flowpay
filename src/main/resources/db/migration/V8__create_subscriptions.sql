CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    account_id UUID NULL,
    card_id UUID NULL,
    name VARCHAR(100) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    start_date DATE NOT NULL,
    billing_day SMALLINT NOT NULL,
    periodicity VARCHAR(10) NOT NULL,
    canceled_on DATE NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscriptions_account FOREIGN KEY (user_id, account_id) REFERENCES bank_accounts(user_id, id),
    CONSTRAINT fk_subscriptions_card FOREIGN KEY (user_id, card_id) REFERENCES credit_cards(user_id, id),
    CONSTRAINT chk_subscriptions_canceled_on CHECK (canceled_on >= start_date),
    CONSTRAINT chk_subscriptions_amount CHECK (amount > 0),
    CONSTRAINT chk_subscriptions_billing_day CHECK (billing_day BETWEEN 1 AND 31),
    CONSTRAINT chk_subscriptions_periodicity CHECK (periodicity IN ('MONTHLY', 'YEARLY')),
    CONSTRAINT chk_subscriptions_account_or_card CHECK ((account_id IS NULL) <> (card_id IS NULL)),
    CONSTRAINT uq_subscriptions_user_id UNIQUE (user_id, id)
);

ALTER TABLE transactions
ADD CONSTRAINT uq_transactions_user_id UNIQUE (user_id, id);

CREATE TABLE subscription_charges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    subscription_id UUID NOT NULL,
    charge_date DATE NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    transaction_id UUID NULL,
    card_purchase_id UUID NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscription_charges_subscription FOREIGN KEY (user_id, subscription_id) REFERENCES subscriptions(user_id, id),
    CONSTRAINT fk_subscription_charges_transaction FOREIGN KEY (user_id, transaction_id) REFERENCES transactions(user_id, id),
    CONSTRAINT fk_subscription_charges_card_purchase FOREIGN KEY (user_id, card_purchase_id) REFERENCES card_purchases(user_id, id),
    CONSTRAINT uq_subscription_charges_transaction UNIQUE (transaction_id),
    CONSTRAINT uq_subscription_charges_purchase UNIQUE (card_purchase_id),
    CONSTRAINT uq_subscription_charges_date UNIQUE (subscription_id, charge_date),
    CONSTRAINT chk_subscription_charges_amount CHECK (amount > 0),
    CONSTRAINT chk_subscription_charges_transaction_or_purchase CHECK ((transaction_id IS NULL) <> (card_purchase_id IS NULL))
);