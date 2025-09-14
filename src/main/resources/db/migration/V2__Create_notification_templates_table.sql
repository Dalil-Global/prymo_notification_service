CREATE TABLE notification_templates (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        name VARCHAR(100) NOT NULL UNIQUE,
                                        subject VARCHAR(255),
                                        content TEXT NOT NULL,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               user_id UUID NOT NULL,
                               channel VARCHAR(20) NOT NULL,
                               type VARCHAR(50) NOT NULL,
                               status VARCHAR(20) DEFAULT 'PENDING',
                               priority VARCHAR(20) DEFAULT 'NORMAL',
                               content TEXT,
                               provider VARCHAR(50),
                               attempts INT DEFAULT 0,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               sent_at TIMESTAMP,
                               error_message TEXT
);
