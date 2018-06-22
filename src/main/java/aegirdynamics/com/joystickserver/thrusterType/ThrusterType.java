package aegirdynamics.com.joystickserver.thrusterType;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ThrusterType {

    @Id
    private Integer id;
    private String type;

    public ThrusterType(){

    }

    public ThrusterType(Integer id, String type) {
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

