
-- Create table for book file content
CREATE TABLE IF NOT EXISTS book_file_content (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT,
    content JSON NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_file_content_book FOREIGN KEY (book_id) REFERENCES book(id)
);


-- Create catalog table for book status
CREATE TABLE IF NOT EXISTS book_status (
    id CHAR(1) PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- Insert book_status values
INSERT INTO book_status (id, name)
VALUES
    ('A', 'Active'),
    ('I', 'Inactive');

ALTER TABLE book ADD COLUMN status CHAR(1) REFERENCES book_status(id);
