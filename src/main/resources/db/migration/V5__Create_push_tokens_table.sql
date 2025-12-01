CREATE TABLE push_tokens (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             device_token VARCHAR(255) UNIQUE NOT NULL,
                             user_id UUID NOT NULL,
                             device_type VARCHAR(50),
                             active BOOLEAN DEFAULT TRUE
);