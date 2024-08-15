package net.atos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "text_based_document")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TextBasedDocument extends DocumentEntity {

    @NotNull
    @Field("word_count")
    private Long wordCount;

}
