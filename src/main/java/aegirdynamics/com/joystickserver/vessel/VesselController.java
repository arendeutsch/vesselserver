package aegirdynamics.com.joystickserver.vessel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @RequestMapping(value = "/vessels/{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public Optional<Vessel> getVessel(@PathVariable Integer id) {
        return vesselService.getVessel(id);
    }

    @RequestMapping(value = "/vessels", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:3000")
    public void addVessel(@RequestBody Vessel vessel){
        vesselService.addVessel(vessel);
    }

    @RequestMapping(value = "/vessels/{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "http://localhost:3000")
    public void updateVessel(@RequestBody Vessel vessel, @PathVariable Integer id){
        vesselService.updateVessel(vessel, id);
    }

    @RequestMapping(value = "/vessels/{id}", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "http://localhost:3000")
    public void deleteVessel(@PathVariable Integer id) {
        vesselService.deleteVessel(id);
    }
}
