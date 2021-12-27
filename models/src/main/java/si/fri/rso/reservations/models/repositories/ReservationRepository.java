package si.fri.rso.reservations.models.repositories;

import org.springframework.data.repository.CrudRepository;
import si.fri.rso.reservations.models.entities.Reservation;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

}