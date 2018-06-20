package aegirdynamics.com.joystickserver.vessel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VesselService {

    @Autowired
    private VesselRepository vesselRepository;

    public List<Vessel> getAllVessels() {
        List<Vessel> vessels = new ArrayList<>();
        vesselRepository.findAll().forEach(vessels::add);
        return vessels;
    }

    public Optional<Vessel> getVessel(Integer id) {
        return vesselRepository.findById(id);
    }

    public void addVessel(Vessel vessel) {
        vesselRepository.save(vessel);
    }

    public void updateVessel(Vessel vessel, Integer id) {
        vesselRepository.save(vessel);
    }

    public void deleteVessel(Integer id) {
        vesselRepository.deleteById(id);
    }
}
