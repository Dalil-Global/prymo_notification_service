CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Notifications table
CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               recipient_email VARCHAR(255) NOT NULL,
                               subject VARCHAR(255),
                               body TEXT NOT NULL,
                               status VARCHAR(20) DEFAULT 'PENDING',
                               priority VARCHAR(20) DEFAULT 'NORMAL',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
