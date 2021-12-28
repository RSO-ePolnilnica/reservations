package si.fri.rso.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import si.fri.rso.reservations.models.entities.Reservation;
import si.fri.rso.reservations.models.entities.Station;
import si.fri.rso.reservations.services.ReservationService;
import si.fri.rso.reservations.services.StationService;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/reservations") // This means URL's start with /demo (after Application path)
public class MainController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StationService stationService;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addReservation (@RequestParam Integer stID, @RequestParam Integer uID, @RequestParam double lon) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request


        Optional<Station> r = stationService.getStationByID(stID);
        Station s = r.get();
        if(s.isReserved()){
            return "Invalid station";
        }
        s.setReserved(true);

        Reservation n = new Reservation();
        n.setStation(stID);
        n.setUser(uID);
        n.setDate();
        reservationService.addReservation(n);

        stationService.updateStation(s);
        return "Saved";
    }

    @Scheduled(fixedRate = 300000)
    public void refreshReservations() {
        Iterable<Station> stations = stationService.getAllStations();
        Date date = new Date();
        Iterator<Station> iter = stations.iterator();
        while(iter.hasNext()){
            Station s = iter.next();
            Optional<Reservation> r = reservationService.getReservationByID(s.getId());
            if(r.isPresent() == true){
                if((date.getTime() - r.get().getDate().getTime())/1000 > 7200){
                    s.setReserved(false);
                    stationService.updateStation(s);
                }
            }
        }
    }
}