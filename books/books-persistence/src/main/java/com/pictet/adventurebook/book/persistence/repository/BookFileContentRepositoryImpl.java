package com.pictet.adventurebook.book.persistence.repository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FilterInputStream;
import java.io.IOException;

import com.pictet.adventurebook.book.persistence.api.BookFileContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookFileContentRepositoryImpl implements BookFileContentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public InputStream findContentById(Long id) {
        try {
            Connection con = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT content FROM book_file_content WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                InputStream binaryStream = rs.getBinaryStream(1);
                return new FilterInputStream(binaryStream) {
                    @Override
                    public void close() throws IOException {
                        try {
                            super.close();
                        } finally {
                            try {
                                rs.close();
                            } catch (SQLException e) {
                                log.error("Error closing ResultSet", e);
                            }
                            try {
                                ps.close();
                            } catch (SQLException e) {
                                log.error("Error closing PreparedStatement", e);
                            }
                            try {
                                con.close();
                            } catch (SQLException e) {
                                log.error("Error closing Connection", e);
                            }
                        }
                    }
                };
            } else {
                rs.close();
                ps.close();
                con.close();
                return null;
            }
        } catch (SQLException e) {
            log.error("Failed to retrieve content stream for id: " + id, e);
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateBookId(Long fileId, Long bookId) {
        jdbcTemplate.update(
                "UPDATE book_file_content SET book_id = ? WHERE id = ?",
                bookId, fileId
        );
    }

    @Override
    @Transactional
    public Long save(InputStream inputStream) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO book_file_content (content) VALUES (?::json)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setCharacterStream(1, new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                return ps;
            }, keyHolder);

            Number key = (Number) keyHolder.getKeys().get("id");
            return key != null ? key.longValue() : null;
        } catch (Exception e) {
            log.error("Failed to save book file content", e);
            return null;
        }
    }
}
