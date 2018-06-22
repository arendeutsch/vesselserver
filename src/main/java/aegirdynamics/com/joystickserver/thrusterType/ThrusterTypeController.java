package aegirdynamics.com.joystickserver.thrusterType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ThrusterTypeController {

    @Autowired
    private ThrusterTypeService thrusterTypeService;

    @RequestMapping(value = "/thrusterTypes", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public List<ThrusterType> getAllThrusterTypes() {
        return thrusterTypeService.getAllThrusterTypes();
    }
}

