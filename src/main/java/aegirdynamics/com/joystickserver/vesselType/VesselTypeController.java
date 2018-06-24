package aegirdynamics.com.joystickserver.vesselType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VesselTypeController {

    @Autowired
    private VesselTypeService vesselTypeService;

    @RequestMapping(value = "/vesselTypes", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public List<VesselType> getAllThrusterTypes() {
        return vesselTypeService.getAllVesselTypes();
    }
}
