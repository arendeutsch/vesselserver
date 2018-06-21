package aegirdynamics.com.joystickserver.vesselType;

import aegirdynamics.com.joystickserver.vessel.Vessel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class VesselType {

    @Id
    private Integer id;
    private String type;
    @OneToOne
    private Vessel vessel;

    public VesselType(){

    }

    public VesselType(Integer id, String type) {
        super();
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
