package aegirdynamics.com.joystickserver.thruster;

import aegirdynamics.com.joystickserver.vessel.Vessel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ThrusterController {

    @Autowired
    private ThrusterService thrusterService;

    @RequestMapping(value = "/vessels/{id}/thrusters", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Thruster> getAllThrusters(@PathVariable String vesselId) {
        return thrusterService.getAllThrusters(vesselId);
    }

    @RequestMapping(value = "/vessels/{vesselId}/thrusters/{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public Optional<Thruster> getThruster(@PathVariable String id) {
        return thrusterService.getThruster(id);
    }

    @RequestMapping(value = "/vessels/{vesselId}/thrusters", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:3000")
    public void addThruster(@RequestBody Thruster thruster, @PathVariable Integer vesselId){
        thruster.setVessel(new Vessel(vesselId, "", "", 0, "", "", ""));
        thrusterService.addThruster(thruster);
    }

    @RequestMapping(value =  "/vessels/{vesselId}/thrusters/{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "http://localhost:3000")
    public void updateThruster(@RequestBody Thruster thruster, @PathVariable String id, @PathVariable Integer vesselId){
        thruster.setVessel(new Vessel(vesselId, "", "", 0, "", "", ""));
        thrusterService.updateThruster(thruster);
    }

    @RequestMapping(value = "/vessels/{vesselId}/thrusters/{id}", method = RequestMethod.DELETE)
    @CrossOrigin(origins = "http://localhost:3000")
    public void deleteThruster(@PathVariable String id) {
        thrusterService.deleteThruster(id);
    }
}
