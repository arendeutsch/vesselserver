package aegirdynamics.com.joystickserver.vessel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class VesselController {

    @Autowired
    private VesselService vesselService;

    @RequestMapping(value = "/vessels", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Vessel> getAllVessels() {
        return vesselService.getAllVessels();
    }

    @RequestMapping(value = "/vesselstypes", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public List getAllVesselsList() {
        return vesselService.getAllVesselsWithTypes();
    }

    @RequestMapping(value = "/vessels/{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Optional<Vessel>> getVessel(@PathVariable Integer id) {
        Optional<Vessel> vessel = vesselService.getVessel(id);
        if (vessel.isPresent()) {
            return new ResponseEntity<>(vessel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(vessel, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/vessels", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:3000")
    public Vessel addVessel(@RequestBody Vessel vessel){
        return vesselService.addVessel(vessel);
    }

    @RequestMapping(value = "/vessels/{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Vessel> updateVessel(@RequestBody Vessel vessel, @PathVariable Integer id){
        Vessel ret = vesselService.updateVessel(vessel, id);
        if (ret != null) {
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ret, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/vessels/{id}", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "http://localhost:3000")
    public void deleteVessel(@PathVariable Integer id) {
        vesselService.deleteVessel(id);
    }

    @RequestMapping(value = "/vessel/{id}/getSolution", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getThrust(@PathVariable Integer id, @RequestBody Map<String, String> cmd){
        String response = vesselService.allocateThrust(id, cmd);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
