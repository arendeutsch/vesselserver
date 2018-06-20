package aegirdynamics.com.joystickserver.thruster;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ThrusterRepository extends CrudRepository<Thruster, String> {

    public List<Thruster> findByVesselId(String vesselId);
}
