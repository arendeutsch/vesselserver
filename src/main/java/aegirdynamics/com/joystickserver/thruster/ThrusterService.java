package aegirdynamics.com.joystickserver.thruster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ThrusterService {

    @Autowired
    private ThrusterRepository thrusterRepository;

    public List<Thruster> getAllThrusters(String vesselId) {
        List<Thruster> thrusters = new ArrayList<>();
        thrusterRepository.findByVesselId(vesselId).forEach(thrusters::add);
        return thrusters;
    }

    public Optional<Thruster> getThruster(String id) {
        return thrusterRepository.findById(id);
    }

    public void addThruster(Thruster thruster) {
        thrusterRepository.save(thruster);
    }

    public void updateThruster(Thruster thruster) {
        thrusterRepository.save(thruster);
    }

    public void deleteThruster(String id) {
        thrusterRepository.deleteById(id);
    }
}
