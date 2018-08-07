package aegirdynamics.com.joystickserver.vessel;
import aegirdynamics.com.joystickserver.thrusterType.ThrusterType;

import aegirdynamics.com.joystickserver.thrusterType.ThrusterType;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Vessel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String hull;
    private int type;
    private String date;
    private String length;
    private String width;
    private DoubleMatrix2D H;
    private DoubleMatrix2D Aeq;

    private String stageAnchorPoints;

    public Vessel() {
    }

    public Vessel(Integer id, String name, String hull, int type, String date, String length, String width, String stageAnchorPoints) {
        super();
        this.id = id;
        this.name = name;
        this.hull = hull;
        this.type = type;
        this.date = date;
        this.length = length;
        this.width = width;
        this.stageAnchorPoints = stageAnchorPoints;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHull() {
        return hull;
    }

    public void setHull(String hull) {
        this.hull = hull;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getStageAnchorPoints() {
        return stageAnchorPoints;
    }

    public void setStageAnchorPoints(String stageAnchorPoints) {
        this.stageAnchorPoints = stageAnchorPoints;
    }

    public void setHMatrix(int numberofThrusters, ArrayList<Double> powerCoefficient) {
        this.H = new DenseDoubleMatrix2D(numberofThrusters, numberofThrusters);
        this.H.assign(0);

        for (int i = 0; i < powerCoefficient.size(); i++) {
            this.H.set(i,i, powerCoefficient.get(i));
        }
    }

    public DoubleMatrix2D getHMatrix() {
        return H;
    }

    public void setEqualityConstraintsMatrix(ArrayList<Integer> thrusters, ArrayList<Double> thrusterPositionFromCG) {
        // tunnel = 1 column
        // azimuth = 2 columns
        // rudder = 2 columns

        this.Aeq = new DenseDoubleMatrix2D(3, thrusterPositionFromCG.size());
        this.Aeq.assign(0);
        int column = 0;
        for (int i = 0; i < thrusters.size(); i++) {
            switch (thrusters.get(i)) {
                case ThrusterType.TUNNEL:
                    setTunnel(column, thrusterPositionFromCG.get(i));
                    column++;
                    break;
                case ThrusterType.AZIMUTH:
                    setAzimuth(column, thrusterPositionFromCG.get(column), thrusterPositionFromCG.get(column+1));
                    column+=2;
                    break;
                case ThrusterType.RUDDER:
                    // TODO
                    break;
                default:
                    throw new IllegalArgumentException("Invalid thruster type: " + thrusters.get(i));
            }
        }
    }

    public DoubleMatrix2D getEqualityConstraintsMatrix(){
        return Aeq;
    }

    public double calculateCostFunction(double[] sol) {
        DoubleMatrix2D solution = new DenseDoubleMatrix2D(sol.length, 1);
        for (int i = 0; i < sol.length; i++) {
            solution.set(i, 0, sol[i]);
        }
        DoubleMatrix2D Wu = new DenseDoubleMatrix2D(sol.length, 1);
        DoubleMatrix2D u_transpose = new DenseDoubleMatrix2D(1, sol.length);
        DenseDoubleMatrix2D costFunction = new DenseDoubleMatrix2D(1,1);

        this.getHMatrix().zMult(solution, Wu);
        u_transpose = solution.viewDice();
        u_transpose.zMult(Wu, costFunction);
        return costFunction.get(0,0);
    }

    private void setAzimuth(int thrusterNumber, double PositionX, double positionY) {
        this.Aeq.set(0, thrusterNumber, 1);			this.Aeq.set(0, thrusterNumber+1, 0);
        this.Aeq.set(1, thrusterNumber, 0);			this.Aeq.set(1, thrusterNumber+1, 1);
        this.Aeq.set(2, thrusterNumber, PositionX);	    this.Aeq.set(2, thrusterNumber+1, positionY);
    }

    private void setTunnel(int thrusterNumber, double position){
        this.Aeq.set(0, thrusterNumber, 0);
        this.Aeq.set(1, thrusterNumber, 1);
        this.Aeq.set(2, thrusterNumber, position);
    }
}
