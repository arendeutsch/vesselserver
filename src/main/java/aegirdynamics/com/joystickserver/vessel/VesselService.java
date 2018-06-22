package aegirdynamics.com.joystickserver.vessel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VesselService {

    private static String Q_GET_ALL_VESSELS = "select v.name, v.hull, v.date, v.length, v.width, vt.type from Vessel v join VesselType vt on v.type = vt.id";

    @Autowired
    private VesselRepository vesselRepository;
    @PersistenceContext
    private EntityManager em;

    public List<Vessel> getAllVessels() {
        List<Vessel> vessels = new ArrayList<>();
        vesselRepository.findAll().forEach(vessels::add);
        return vessels;
    }

    public List getAllVesselsWithTypes() {
        Query query = em.createQuery(Q_GET_ALL_VESSELS);
        return query.getResultList();
    }

    public Optional<Vessel> getVessel(Integer id) {
        return vesselRepository.findById(id);
    }

    public void addVessel(Vessel vessel) {
        vesselRepository.save(vessel);
    }

    public Vessel updateVessel(Vessel vessel, Integer id) {
        Optional<Vessel> vesselOptional = this.getVessel(id);
        if (vesselOptional.isPresent()) {
            Vessel ret = vesselOptional.get();
            ret.setStageAnchorPoints(vessel.getStageAnchorPoints());
            return vesselRepository.save(ret);
        }
        return null;
    }

    public void deleteVessel(Integer id) {
        vesselRepository.deleteById(id);
    }
}
