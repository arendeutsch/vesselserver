package aegirdynamics.com.joystickserver.vesselType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VesselTypeService {

    @Autowired
    private VesselTypeRepository vesselTypeRepository;

    public List<VesselType> getAllVesselTypes() {
        List<VesselType> vesselTypes = new ArrayList<>();
        vesselTypeRepository.findAll().forEach(vesselTypes::add);
        return vesselTypes;
    }
}
