-- V2: Add constraints, indexes, and defaults for robustness

-- Add NOT NULL constraints to users table
ALTER TABLE users ALTER COLUMN salon_id SET NOT NULL;
ALTER TABLE users ALTER COLUMN email SET NOT NULL;
ALTER TABLE users ALTER COLUMN password_hash SET NOT NULL;
ALTER TABLE users ALTER COLUMN role SET NOT NULL;
ALTER TABLE users ALTER COLUMN enabled SET NOT NULL;
ALTER TABLE users ALTER COLUMN created_at SET NOT NULL;

-- Add UNIQUE constraint on (salon_id, email) to prevent duplicate owner accounts
ALTER TABLE users ADD CONSTRAINT uq_users_email_salon UNIQUE (salon_id, email);

-- Add index on (salon_id, email) for fast lookups during login
CREATE INDEX idx_users_salon_email ON users(salon_id, email);

-- Add NOT NULL constraint to salon table
ALTER TABLE salon ALTER COLUMN name SET NOT NULL;
ALTER TABLE salon ALTER COLUMN created_at SET NOT NULL;

-- Set default values for created_at if not already set
ALTER TABLE salon ALTER COLUMN created_at SET DEFAULT now();
ALTER TABLE users ALTER COLUMN created_at SET DEFAULT now();

-- Set default value for enabled
ALTER TABLE users ALTER COLUMN enabled SET DEFAULT true;
