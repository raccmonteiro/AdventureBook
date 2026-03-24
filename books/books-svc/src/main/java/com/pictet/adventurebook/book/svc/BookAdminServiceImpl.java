package com.pictet.adventurebook.book.svc;

import java.util.Collection;
import java.util.List;

import com.pictet.adventurebook.book.domain.type.Category;
import com.pictet.adventurebook.book.domain.type.BookId;
import com.pictet.adventurebook.book.svc.api.BookAdminService;
import com.pictet.adventurebook.book.svc.features.upload.UploadBookHandler;
import com.pictet.adventurebook.book.svc.features.category.AddCategoryHandler;
import com.pictet.adventurebook.book.svc.features.category.RemoveCategoryHandler;
import com.pictet.adventurebook.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class BookAdminServiceImpl implements BookAdminService {

    private final AddCategoryHandler addCategoryHandler;
    private final RemoveCategoryHandler removeCategoryHandler;
    private final UploadBookHandler uploadBookHandler;

    @Override
    public Result<List<Category>> addCategory(BookId bookId, List<Category> categories) {
        return addCategoryHandler.handle(bookId, categories);
    }

    @Override
    public Result<List<Category>> removeCategory(BookId bookId, Collection<Category> categories) {
        return removeCategoryHandler.handle(bookId, categories);
    }

    @Override
    public Result<Void> uploadBook(InputStream inputStream) {
        return uploadBookHandler.handle(inputStream);
    }

}
