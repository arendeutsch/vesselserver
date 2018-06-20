package aegirdynamics.com.joystickserver.thruster;

import aegirdynamics.com.joystickserver.vessel.Vessel;

import javax.persistence.*;

@Entity
public class Thruster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private int type;
    private String x_cg;
    private String y_cg;
    @ManyToOne
    private Vessel vessel;

    public Thruster() {
    }

    public Thruster(String id, int type, String x_cg, String y_cg, Integer vesselId) {
        super();
        this.id = id;
        this.type = type;
        this.x_cg = x_cg;
        this.y_cg = y_cg;
        this.vessel = new Vessel(vesselId, "", "", 0, "", "", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getXcg() {
        return x_cg;
    }

    public void setXcg(String x_cg) {
        this.x_cg = x_cg;
    }

    public String getYcg() {
        return y_cg;
    }

    public void setYcg(String y_cg) {
        this.y_cg = y_cg;
    }

    public Vessel getVessel() {
        return vessel;
    }

    public void setVessel(Vessel vessel) {
        this.vessel = vessel;
    }
}
