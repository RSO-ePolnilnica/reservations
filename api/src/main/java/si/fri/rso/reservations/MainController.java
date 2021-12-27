package si.fri.rso.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import si.fri.rso.reservations.models.entities.Reservation;
import si.fri.rso.reservations.services.ReservationService;

@Controller // This means that this class is a Controller
@RequestMapping(path="/reservations") // This means URL's start with /demo (after Application path)
public class MainController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addReservation (@RequestParam Integer stID, @RequestParam Integer uID, @RequestParam double lon) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Reservation n = new Reservation();
        n.setStation(stID);
        n.setUser(uID);
        n.setDate();
        reservationService.addReservation(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Reservation> getAllUsers() {
        // This returns a JSON or XML with the users
        return reservationService.getAllReservations();
    }
}