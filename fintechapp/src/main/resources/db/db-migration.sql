-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Wallets table
CREATE TABLE wallets (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    balance NUMERIC(15,2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Transaction logs table
CREATE TABLE transaction_logs (
    id SERIAL PRIMARY KEY,
    transaction_ref VARCHAR(64) NOT NULL UNIQUE,
    wallet_id INT REFERENCES wallets(id),
    type VARCHAR(20) CHECK (type IN ('PAYMENT', 'TRANSFER', 'WITHDRAWAL', 'SUBSCRIPTION')),
    amount NUMERIC(15,2) NOT NULL,
    description TEXT,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED', 'ROLLED_BACK')),
    timestamp TIMESTAMP DEFAULT NOW()
);
