CREATE TABLE idempotency (
                                     id BIGSERIAL PRIMARY KEY,
                                     operation_type VARCHAR(100) NOT NULL,
                                     idempotency_key VARCHAR(255) NOT NULL,
                                     request_hash VARCHAR(128),
                                     status VARCHAR(50) NOT NULL,
                                     response_payload TEXT,
                                     response_class VARCHAR(255),
                                     error_message VARCHAR(1000),
                                     created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                     updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                     version BIGINT
);

ALTER TABLE idempotency
    ADD CONSTRAINT uk_idempotency_operation_key
        UNIQUE (operation_type, idempotency_key);