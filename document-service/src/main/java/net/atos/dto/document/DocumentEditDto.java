package net.atos.dto.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import lombok.*;
import net.atos.model.enums.EnumDataType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class DocumentEditDto {

    @NotNull(message = "ID cannot be null")
    private UUID id;

    @Size(max = 2500, message = "Path cannot exceed 2500 characters")
    private String filePath;

    private EnumDataType type;

    @Size(max = 50, message = "Tags cannot have more than 50 elements")
    private Set<String> tags;

    private Boolean isPublic;
}