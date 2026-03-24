package com.pictet.adventurebook.game.persistence.repository;

import com.pictet.adventurebook.game.persistence.entity.OptionEntity;
import com.pictet.adventurebook.game.persistence.entity.SectionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SectionWithOptionsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<SectionEntity> findFirstSectionByBookIdWithOptions(Long bookId) {
        String sql = """
            SELECT s.id as s_id, s.book_id as s_book_id, s.type as s_type, s.text as s_text,
                   o.id as o_id, o.section_id as o_section_id, o.goto_id as o_goto_id, o.description as o_description
            FROM section s
            LEFT JOIN option o ON s.id = o.section_id
            WHERE s.book_id = :bookId
              AND s.type = 'B'
            ORDER BY o.id ASC
            """;
        Map<String, Object> params = new HashMap<>();
        params.put("bookId", bookId);
        return jdbcTemplate.query(sql, params, sectionResultSetExtractor());
    }

    public Optional<SectionEntity> findByIdWithOptions(Long sectionId) {
        String sql = """
            SELECT s.id as s_id, s.book_id as s_book_id, s.type as s_type, s.text as s_text,
                   o.id as o_id, o.section_id as o_section_id, o.goto_id as o_goto_id, o.description as o_description
            FROM section s
            LEFT JOIN option o ON s.id = o.section_id
            WHERE s.id = :sectionId
            ORDER BY o.id ASC
            """;
        Map<String, Object> params = new HashMap<>();
        params.put("sectionId", sectionId);
        return jdbcTemplate.query(sql, params, sectionResultSetExtractor());
    }

    private ResultSetExtractor<Optional<SectionEntity>> sectionResultSetExtractor() {
        return rs -> {
            SectionEntity section = null;
            while (rs.next()) {
                if (section == null) {
                    section = SectionEntity.builder()
                            .id(rs.getLong("s_id"))
                            .bookId(rs.getLong("s_book_id"))
                            .type(rs.getString("s_type").charAt(0))
                            .text(rs.getString("s_text"))
                            .options(new java.util.HashSet<>())
                            .build();
                }
                Long optionId = rs.getObject("o_id", Long.class);
                if (optionId != null) {
                    OptionEntity option = OptionEntity.builder()
                            .id(optionId)
                            .sectionId(rs.getLong("o_section_id"))
                            .gotoId(rs.getLong("o_goto_id"))
                            .description(rs.getString("o_description"))
                            .consequences(new java.util.HashSet<>())
                            .build();
                    section.getOptions().add(option);
                }
            }
            return Optional.ofNullable(section);
        };
    }
}
