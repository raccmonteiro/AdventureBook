package com.pictet.adventurebook.filereader.batch.importbook;

import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictet.adventurebook.book.persistence.api.BookFileContentRepository;
import com.pictet.adventurebook.filereader.model.BookHeader;
import com.pictet.adventurebook.filereader.model.ImportItem;
import com.pictet.adventurebook.filereader.model.SectionJson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Factory for creating JsonItemReader instances for the import book batch.
 * Handles reading from database sources.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ImportBookReaderFactory {

    private final BookFileContentRepository bookFileContentRepository;

    public JsonItemReader<ImportItem> createReader(Long fileId) {
        if (fileId == null) {
            throw new IllegalArgumentException("fileId must not be null");
        }
        log.info("Creating streaming reader from database for fileId: {}", fileId);
        InputStream inputStream = bookFileContentRepository.findContentById(fileId);
        if (inputStream == null) {
            throw new IllegalStateException("No content found for fileId: " + fileId);
        }
        Resource resource = new InputStreamResource(inputStream);

        // Custom JsonObjectReader that streams the book header and sections one by one
        JacksonJsonObjectReader<ImportItem> jsonObjectReader = new JacksonJsonObjectReader<>(ImportItem.class) {
            private JsonParser jsonParser;
            private ObjectMapper mapper;
            private boolean headerRead = false;
            private boolean inSectionsArray = false;

            @Override
            public void open(Resource resource) throws Exception {
                log.info("Opening resource for streaming JSON reading");
                this.mapper = new ObjectMapper();
                InputStream is = resource.getInputStream();
                this.jsonParser = mapper.getFactory().createParser(is);

                JsonToken token = this.jsonParser.nextToken();
                if (token != JsonToken.START_OBJECT) {
                    throw new IllegalStateException("The Json input stream must start with an object, but was " + token);
                }
            }

            @Override
            public ImportItem read() throws Exception {
                if (this.jsonParser == null) {
                    return null;
                }

                while (true) {
                    JsonToken token = this.jsonParser.nextToken();
                    if (token == null || token == JsonToken.END_OBJECT) {
                        return null;
                    }

                    if (!headerRead) {
                        BookHeader.BookHeaderBuilder bookHeaderBuilder = BookHeader.builder()
                            .fileId(fileId);

                        // Stay at the top level until we find "sections" or end of object
                        while (token != null && token != JsonToken.END_OBJECT) {
                            if (token == JsonToken.FIELD_NAME) {
                                String fieldName = this.jsonParser.getCurrentName();
                                this.jsonParser.nextToken();
                                if ("title".equals(fieldName)) {
                                    bookHeaderBuilder.title(this.jsonParser.getValueAsString());
                                } else if ("author".equals(fieldName)) {
                                    bookHeaderBuilder.author(this.jsonParser.getValueAsString());
                                } else if ("difficulty".equals(fieldName)) {
                                    bookHeaderBuilder.difficulty(this.jsonParser.getValueAsString());
                                } else if ("sections".equals(fieldName)) {
                                    if (this.jsonParser.currentToken() != JsonToken.START_ARRAY) {
                                        throw new IllegalStateException("Expected 'sections' to be an array");
                                    }
                                    bookHeaderBuilder.build();
                                    this.inSectionsArray = true;
                                    this.headerRead = true;
                                    // Return header before processing sections
                                    return new ImportItem(bookHeaderBuilder.build());
                                } else {
                                    this.jsonParser.skipChildren();
                                }
                            }
                            token = this.jsonParser.nextToken();
                        }


                        if (!headerRead) {
                            this.headerRead = true;
                            return new ImportItem(bookHeaderBuilder.build());
                        }
                        return null;
                    }

                    if (inSectionsArray) {
                        token = this.jsonParser.currentToken();
                        if (token == JsonToken.START_ARRAY) {
                            token = this.jsonParser.nextToken();
                        }

                        if (token == JsonToken.END_ARRAY) {
                            this.inSectionsArray = false;
                            // Continue to outer loop to find more fields or end object
                            continue;
                        }

                        if (token == JsonToken.START_OBJECT) {
                            SectionJson section = this.jsonParser.readValueAs(SectionJson.class);
                            return new ImportItem(section);
                        }
                    }
                }
            }

            @Override
            public void close() throws Exception {
                if (this.jsonParser != null) {
                    this.jsonParser.close();
                }
            }
        };

        return new JsonItemReaderBuilder<ImportItem>()
                .jsonObjectReader(jsonObjectReader)
                .resource(resource)
                .name("importItemReader")
                .build();
    }
}
