package aegirdynamics.com.joystickserver.thrusterType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThrusterTypeService {

    @Autowired
    private ThrusterTypeRepository thrusterTypeRepository;

    public List<ThrusterType> getAllThrusterTypes() {
        List<ThrusterType> thrustersTypes = new ArrayList<>();
        thrusterTypeRepository.findAll().forEach(thrustersTypes::add);
        return thrustersTypes;
    }
}

