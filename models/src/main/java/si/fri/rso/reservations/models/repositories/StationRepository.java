package si.fri.rso.reservations.models.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.rso.reservations.models.entities.Station;

public interface StationRepository extends CrudRepository<Station, Integer> {

}