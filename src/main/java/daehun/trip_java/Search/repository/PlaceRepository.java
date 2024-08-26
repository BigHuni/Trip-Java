package daehun.trip_java.Search.repository;
import daehun.trip_java.Search.domain.Place;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.Optional;

public interface PlaceRepository extends ElasticsearchRepository<Place, String> {
  Optional<Place> findByName(String name);
}