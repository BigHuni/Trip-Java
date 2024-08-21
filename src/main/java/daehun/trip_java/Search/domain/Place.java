package daehun.trip_java.Search.domain;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "place")
public class Place {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String name;

  @Field(type = FieldType.Text)
  private String address;

  @Field(type = FieldType.Double)
  private Double latitude;

  @Field(type = FieldType.Double)
  private Double longitude;

  @Field(type = FieldType.Double)
  private Float rating;

  @Field(type = FieldType.Date)
  private LocalDateTime updatedAt;
}