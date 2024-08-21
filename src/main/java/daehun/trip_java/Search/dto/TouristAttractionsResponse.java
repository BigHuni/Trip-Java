package daehun.trip_java.Search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TouristAttractionsResponse {
  @JsonProperty("TouristAttract1ionsInfo")
  private TouristAttractionsInfo touristAttractionsInfo;

  @Getter
  @Setter
  public static class TouristAttractionsInfo {
    @JsonProperty("row")
    private List<TouristAttraction> attractions;
  }

  @Getter
  @Setter
  public static class TouristAttraction {
    @JsonProperty("POST_SJ")
    private String name;

    @JsonProperty("ADDRESS")
    private String address;

    @JsonProperty("Y")
    private double latitude;

    @JsonProperty("X")
    private double longitude;
  }
}