CREATE TABLE notification_templates (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        name VARCHAR(100) NOT NULL UNIQUE,
                                        subject VARCHAR(255),
                                        type VARCHAR(50), -- 🆕 added field to match entity
                                        content TEXT NOT NULL,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
