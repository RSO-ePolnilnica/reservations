package si.fri.rso.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import si.fri.rso.reservations.models.entities.Reservation;
import si.fri.rso.reservations.models.entities.ReservationTemp;
import si.fri.rso.reservations.models.entities.Station;
import si.fri.rso.reservations.services.ReservationService;
import si.fri.rso.reservations.services.StationService;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

@RestController
@RefreshScope
@CrossOrigin(origins = "https://frontend-t4vmtoqorq-ew.a.run.app")
public class MainController {

    @Value("${allowReservation:true}")
    private boolean canReserve;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StationService stationService;

    @GetMapping("/")
    public ResponseEntity healthCheck(){
        return ResponseEntity.ok().build();
    }

    @PostMapping(path="/reservations/add") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity addReservation (@RequestBody ReservationTemp reservationTemp) {
        if(canReserve == false){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Station> r = stationService.getStationByID(reservationTemp.station);
        Station s = r.get();
        if(s.isReserved()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        s.setReserved(true);

        Reservation n = new Reservation();
        n.setStation(reservationTemp.station);
        n.setUser(reservationTemp.userID);
        n.setDate();
        reservationService.addReservation(n);

        stationService.updateStation(s);
        return ResponseEntity.ok().build();
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