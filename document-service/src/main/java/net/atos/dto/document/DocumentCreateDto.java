package net.atos.dto.document;

import lombok.*;
import net.atos.model.enums.EnumDataType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class DocumentCreateDto {

    public DocumentCreateDto(String filePath, EnumDataType type, Long sizeInBytes, MultipartFile file) {
        this.filePath = filePath;
        this.type = type;
        this.sizeInBytes = sizeInBytes;
        this.file = file;
    }

    @NotBlank(message = "Path cannot be blank")
    @Size(max = 2500, message = "Path cannot exceed 2500 characters")
    private String filePath;

    @NotNull(message = "Type cannot be null")
    private EnumDataType type;

    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be a positive number")
    private Long sizeInBytes;

    @NotNull(message = "File cannot be null")
    private MultipartFile file;

}