package com.pictet.adventurebook.book.domain;

import com.pictet.adventurebook.book.domain.type.Author;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.Difficulty;
import com.pictet.adventurebook.book.domain.type.Title;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @Test
    void book_shouldCreateWithAllFields() {
        BookId id = BookId.valueOf(1L);
        Title title = Title.valueOf("The Hobbit");
        Author author = Author.valueOf("J.R.R. Tolkien");
        Difficulty difficulty = Difficulty.MEDIUM;
        List<Category> categories = List.of(Category.FANTASY, Category.ADVENTURE);

        Book book = new Book(id, title, author, difficulty, categories);

        assertThat(book.id()).isEqualTo(id);
        assertThat(book.title()).isEqualTo(title);
        assertThat(book.author()).isEqualTo(author);
        assertThat(book.difficulty()).isEqualTo(difficulty);
        assertThat(book.categories()).containsExactly(Category.FANTASY, Category.ADVENTURE);
    }

    @Test
    void book_shouldBeEqualWhenAllFieldsMatch() {
        Book book1 = new Book(
                BookId.valueOf(1L),
                Title.valueOf("Title"),
                Author.valueOf("Author"),
                Difficulty.EASY,
                List.of(Category.FICTION)
        );

        Book book2 = new Book(
                BookId.valueOf(1L),
                Title.valueOf("Title"),
                Author.valueOf("Author"),
                Difficulty.EASY,
                List.of(Category.FICTION)
        );

        assertThat(book1).isEqualTo(book2);
    }

    @Test
    void book_shouldNotBeEqualWhenIdDiffers() {
        Book book1 = new Book(
                BookId.valueOf(1L),
                Title.valueOf("Title"),
                Author.valueOf("Author"),
                Difficulty.EASY,
                List.of(Category.FICTION)
        );

        Book book2 = new Book(
                BookId.valueOf(2L),
                Title.valueOf("Title"),
                Author.valueOf("Author"),
                Difficulty.EASY,
                List.of(Category.FICTION)
        );

        assertThat(book1).isNotEqualTo(book2);
    }
}
