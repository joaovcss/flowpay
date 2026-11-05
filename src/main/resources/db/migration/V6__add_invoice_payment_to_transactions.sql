ALTER TABLE transactions
ADD COLUMN invoice_id UUID NULL,

ADD CONSTRAINT fk_transactions_invoice FOREIGN KEY (user_id, invoice_id) REFERENCES invoices(user_id, id),
ADD CONSTRAINT uq_transactions_invoice_id UNIQUE (invoice_id),
ADD CONSTRAINT chk_transactions_invoice_payment CHECK ((type = 'INVOICE_PAYMENT') = (invoice_id IS NOT NULL));