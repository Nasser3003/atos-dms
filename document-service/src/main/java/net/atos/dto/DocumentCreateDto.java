package net.atos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.atos.model.enums.EnumDataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCreateDto {

    @NotBlank(message = "Path cannot be blank")
    @Size(max = 255, message = "Path cannot exceed 255 characters")
    private String path;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Type cannot be null")
    private EnumDataType type;

    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be a positive number")
    private Long sizeInBytes;

    @NotBlank(message = "Extension cannot be blank")
    @Size(max = 10, message = "Extension cannot exceed 10 characters")
    private String extension;
}