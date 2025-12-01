CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ✅ Final Notifications Table
CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               user_id UUID NOT NULL,
                               recipient_email VARCHAR(255),
                               recipient_phone VARCHAR(20),
                               channel VARCHAR(20) NOT NULL,           -- e.g., EMAIL, SMS, PUSH
                               type VARCHAR(50) NOT NULL,              -- template name or notification type
                               subject VARCHAR(255),
                               body TEXT,
                               status VARCHAR(20) DEFAULT 'PENDING',
                               priority VARCHAR(20) DEFAULT 'NORMAL',
                               provider VARCHAR(50),
                               attempts INT DEFAULT 0,
                               error_message TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               sent_at TIMESTAMP
);
