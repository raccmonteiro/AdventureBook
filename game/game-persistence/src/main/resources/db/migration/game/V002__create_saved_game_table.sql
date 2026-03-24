-- Create saved_game table to persist game sessions
CREATE TABLE IF NOT EXISTS saved_game (
    id BIGSERIAL PRIMARY KEY,
    section_id BIGINT NOT NULL,
    state JSONB NOT NULL DEFAULT '{}',
    status CHAR(1) NOT NULL DEFAULT 'I', -- I: IN_PROGRESS, W: WON, L: LOST
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_saved_game_section FOREIGN KEY (section_id) REFERENCES section(id)
);

