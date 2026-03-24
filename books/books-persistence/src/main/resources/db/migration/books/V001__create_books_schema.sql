-- Create catalog table for difficulty
CREATE TABLE IF NOT EXISTS difficulty (
    id CHAR(1) PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- Insert difficulty values
INSERT INTO difficulty (id, name) VALUES
    ('E', 'Easy'),
    ('M', 'Medium'),
    ('H', 'Hard');

-- Create catalog table for category
CREATE TABLE IF NOT EXISTS category (
    id smallint PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(100)
);

-- Insert category values
INSERT INTO category (id, name, description) VALUES
    (1, 'Fiction', 'Fictional stories'),
    (2, 'Science', 'Science-based adventures'),
    (3, 'Horror', 'Horror and thriller content'),
    (4, 'Adventure', 'Adventure stories'),
    (5, 'Fantasy', 'Fantasy worlds'),
    (6, 'Mystery', 'Mystery and detective stories'),
    (7, 'Thriller', 'Suspense and thriller'),
    (8, 'Romance', 'Romance stories'),
    (9, 'Historical', 'Historical fiction');

-- Create book table
CREATE TABLE IF NOT EXISTS book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    difficulty_id CHAR(1) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_difficulty FOREIGN KEY (difficulty_id) REFERENCES difficulty(id)
);

-- Create book_category junction table
CREATE TABLE IF NOT EXISTS book_category (
    book_id BIGINT NOT NULL,
    category_id SMALLINT NOT NULL,
    PRIMARY KEY (book_id, category_id),
    CONSTRAINT fk_book_category_book FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    CONSTRAINT fk_book_category_category FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Create indexes for better query performance
CREATE INDEX idx_book_difficulty ON book(difficulty_id);
CREATE INDEX idx_book_category_book ON book_category(book_id);
CREATE INDEX idx_book_category_category ON book_category(category_id);

-- Create GIN index for full-text search on title and author
CREATE INDEX idx_book_title_fts ON book USING GIN (to_tsvector('english', title));
CREATE INDEX idx_book_author_fts ON book USING GIN (to_tsvector('english', author));

