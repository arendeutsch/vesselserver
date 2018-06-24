package aegirdynamics.com.joystickserver.thruster;

import aegirdynamics.com.joystickserver.vessel.Vessel;

import javax.persistence.*;

@Entity
public class Thruster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer number;
    private Integer type;
    private String x_cg;
    private String y_cg;
    private String effect;
    @Lob
    private String stageNode;
    @ManyToOne
    private Vessel vessel;

    public Thruster() {
    }

    public Thruster(Integer id, int number, int type, String x_cg, String y_cg, String effect, String stageNode, Integer vesselId) {
        super();
        this.id = id;
        this.number = number;
        this.type = type;
        this.x_cg = x_cg;
        this.y_cg = y_cg;
        this.effect = effect;
        this.stageNode = stageNode;

        this.vessel = new Vessel(vesselId, "", "", 0, "", "", "", "");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Vessel getVessel() {
        return vessel;
    }

    public void setVessel(Vessel vessel) {
        this.vessel = vessel;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getX_cg() {
        return x_cg;
    }

    public void setX_cg(String x_cg) {
        this.x_cg = x_cg;
    }

    public String getY_cg() {
        return y_cg;
    }

    public void setY_cg(String y_cg) {
        this.y_cg = y_cg;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getStageNode() {
        return stageNode;
    }

    public void setStageNode(String stageNode) {
        this.stageNode = stageNode;
    }
}
