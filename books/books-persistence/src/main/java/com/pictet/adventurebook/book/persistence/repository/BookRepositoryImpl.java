package com.pictet.adventurebook.book.persistence.repository;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.Book;
import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Title;
import com.pictet.adventurebook.book.persistence.entity.BookEntity;
import com.pictet.adventurebook.book.persistence.repository.spring.BookSpringRepository;
import com.pictet.adventurebook.book.persistence.repository.spring.CategorySpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.pictet.adventurebook.book.persistence.api.BookRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final BookSpringRepository bookSpringRepository;
    private final CategorySpringRepository categorySpringRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(BookId id) {
        return bookSpringRepository.findById(id.getValue())
                                   .map(this::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> searchBooks(Title title, Author author, Collection<Category> categories, Difficulty difficulty, int page, int size) {
        String titleStr = title != null ? title.getValue() : null;
        String authorStr = author != null ? author.getValue() : null;
        Character difficultyId = difficulty != null ? difficulty.getId() : null;
        List<Integer> categoryIds = categories != null ? categories.stream().map(Category::getId).toList() : null;
        int offset = page * size;

        List<BookEntity> bookEntities = bookSpringRepository.searchBooks(titleStr, authorStr, difficultyId, categoryIds, size, offset);

        return bookEntities.stream()
                           .map(this::toModel)
                           .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(BookId id) {
        return bookSpringRepository.existsById(id.getValue());
    }

    private Book toModel(BookEntity entity) {
        List<Category> categories = categorySpringRepository.findAllByBookId(entity.getId()).stream()
                                                            .map(bc -> Category.fromId(bc.getCategoryId()))
                                                            .toList();

        return new Book(
                BookId.valueOf(entity.getId()),
                Title.valueOf(entity.getTitle()),
                Author.valueOf(entity.getAuthor()),
                Difficulty.fromId(entity.getDifficultyId()),
                categories
        );
    }
}
