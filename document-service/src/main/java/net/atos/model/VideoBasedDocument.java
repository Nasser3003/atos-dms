package net.atos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "video_based_document")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoBasedDocument extends DocumentEntity {

    @NotNull
    @Field("resolution")
    private String resolution;

    @NotNull
    @Min(0)
    @Field("duration_in_seconds")
    private Long durationInSeconds;

    @Field("frame_rate")
    private Double frameRate;

    @Field("bit_rate")
    private Long bitRate;

    @Field("aspect_ratio")
    private String aspectRatio;

    private Boolean hasAudio;
}