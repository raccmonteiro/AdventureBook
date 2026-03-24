-- Create catalog table for difficulty
CREATE TABLE IF NOT EXISTS section_type (
    id CHAR(1) PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- Insert section type values
INSERT INTO section_type (id, name) VALUES
    ('B', 'BEGIN'),
    ('E', 'END'),
    ('N', 'NODE');

-- Create section table
CREATE TABLE IF NOT EXISTS section (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT NOT NULL,
    type CHAR(1) NOT NULL,
    text TEXT NOT NULL,
    CONSTRAINT fk_section_book FOREIGN KEY (book_id) REFERENCES book(id)
);

-- Create option table
CREATE TABLE IF NOT EXISTS option (
    id BIGSERIAL PRIMARY KEY,
    section_id BIGINT NOT NULL,
    goto_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    CONSTRAINT fk_option_section FOREIGN KEY (section_id) REFERENCES section(id),
    CONSTRAINT fk_option_section_goto FOREIGN KEY (goto_id) REFERENCES section(id)
);

-- Create consequence table
CREATE TABLE IF NOT EXISTS consequence (
    id BIGSERIAL PRIMARY KEY,
    option_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    value float8 NOT NULL,
    text TEXT NOT NULL,
    CONSTRAINT fk_consequence_option FOREIGN KEY (option_id) REFERENCES option(id)
);


-- Unique partial index for BEGIN sections - ensures each book has exactly one BEGIN section
CREATE UNIQUE INDEX idx_section_begin_book_unique ON section(book_id) WHERE type = 'B';
