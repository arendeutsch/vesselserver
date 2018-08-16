package aegirdynamics.com.joystickserver.vessel;

import aegirdynamics.com.joystickserver.thruster.Thruster;
import aegirdynamics.com.joystickserver.thruster.ThrusterService;
import aegirdynamics.com.joystickserver.thrusterType.ThrusterType;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.LinearMultivariateRealFunction;
import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.OptimizationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
public class VesselService {

    private static String Q_GET_ALL_VESSELS = "select v.name, v.hull, v.date, v.length, v.width, vt.type from Vessel v join VesselType vt on v.type = vt.id";

    @Autowired
    private VesselRepository vesselRepository;
    @Autowired
    private ThrusterService thrusterService;
    @PersistenceContext
    private EntityManager em;

    public List<Vessel> getAllVessels() {
        List<Vessel> vessels = new ArrayList<>();
        vesselRepository.findAll().forEach(vessels::add);
        return vessels;
    }

    public List getAllVesselsWithTypes() {
        Query query = em.createQuery(Q_GET_ALL_VESSELS);
        return query.getResultList();
    }

    public Optional<Vessel> getVessel(Integer id) {
        return vesselRepository.findById(id);
    }

    public Vessel addVessel(Vessel vessel) {
        return vesselRepository.save(vessel);
    }

    public Vessel updateVessel(Vessel vessel, Integer id) {
        Optional<Vessel> vesselOptional = this.getVessel(id);
        if (vesselOptional.isPresent()) {
            Vessel ret = vesselOptional.get();
            ret.setStageAnchorPoints(vessel.getStageAnchorPoints());
            return vesselRepository.save(ret);
        }
        return null;
    }

    public void deleteVessel(Integer id) {
        vesselRepository.deleteById(id);
    }

    public HashMap<String, Object[]> allocateThrust(int vesselId, Map<String, String> cmd) {
        Double surge = Double.parseDouble(cmd.get("surge"));
        Double sway = Double.parseDouble(cmd.get("sway"));
        // TODO: get yaw rotation (z axis)
        double[] force = {surge, sway, 0.0};
        try {
            double[] sol = this.calculateSolution(vesselId, force);
            HashMap<String, Object[]> responseSolution = new HashMap<>();

            ArrayList<Double> thrust = new ArrayList<>();
            ArrayList<Double> angle = new ArrayList<>();
            ArrayList<Integer> type = new ArrayList<>();

            List<Thruster> thrusters = thrusterService.getAllThrusters(vesselId); // List all of vessel thrusters with the vessel it self
            int index = 0;
            for (Thruster thruster : thrusters) {
                switch (thruster.getType()) {
                    case ThrusterType.TUNNEL:
                        type.add(ThrusterType.TUNNEL);
                        thrust.add(sol[index]);
                        angle.add(0.0);
                        index++;
                        break;
                    case ThrusterType.AZIMUTH:
                        type.add(ThrusterType.AZIMUTH);
                        thrust.add(Math.sqrt(Math.pow(sol[index], 2) + Math.pow(sol[index+1], 2)));
                        angle.add((Math.atan2(sol[index+1],sol[index]) * 180/Math.PI));
                        index+=2;
                        break;
                    case ThrusterType.RUDDER:
                        //TODO
                        break;
                }
            }
//            double angle1 = Math.atan2(sol[3],sol[2]);
//            double angle2 = Math.atan2(sol[5],sol[4]);
//            double thrust3 = Math.sqrt(Math.pow(sol[2], 2)+Math.pow(sol[3], 2));
//            double thrust4 = Math.sqrt(Math.pow(sol[4], 2)+Math.pow(sol[5], 2));
//
//            System.out.println("\nThruster 1 [%]: " + sol[0] +
//                    "\nThruster 2 [%]: " + sol[1]+
//                    "\nThruster 3 [%]: " + thrust3 + " angle = " + angle1 * 180 / Math.PI +
//                    "\nThruster 4 [%]: " + thrust4 + " angle = " + angle2 * 180 / Math.PI);

//            System.out.println("\nThruster 1 [%]: " + sol[0] / 2300 * 100 +
//                    "\nThruster 2 [%]: " + sol[1] / 2300 * 100 +
//                    "\nThruster 3 [%]: " + thrust3 / 2500 * 100 + " angle = " + angle1 * 180 / Math.PI +
//                    "\nThruster 4 [%]: " + thrust4 / 2500 * 100 + " angle = " + angle2 * 180 / Math.PI);

            Double[] thrustArray = new Double[thrust.size()];
            thrustArray = thrust.toArray(thrustArray);

            Double[] angleArray = new Double[angle.size()];
            angleArray = angle.toArray(angleArray);

            Integer[] typeArray = new Integer[type.size()];
            typeArray = type.toArray(typeArray);

            Double[] cmdArr = new Double[force.length];
            cmdArr[0] = (Double)force[0];
            cmdArr[1] = (Double)force[1];
            cmdArr[2] = (Double)force[2];

            responseSolution.put("cmd", cmdArr);
            responseSolution.put("thrust", thrustArray);
            responseSolution.put("angle", angleArray);
            responseSolution.put("thruster_type", typeArray);

            return responseSolution;
        } catch (JOptimizerException e) {
            e.printStackTrace();
        }

        return null;
    }

    private double[] calculateSolution(int vesselId, double[] force) throws JOptimizerException {
        long startTime = System.currentTimeMillis();
        List<Thruster> thrusters = thrusterService.getAllThrusters(vesselId); // List all of vessel thrusters with the vessel it self

        Vessel vessel = thrusters.get(0).getVessel();

//        double[] powerCoeffecients = {1,1,0.533,0.533,0.533,0.533};
        ArrayList<Double> powerCoefficients = new ArrayList<>();
        ArrayList<Integer> thrusterType = new ArrayList<>();
        ArrayList<Double> thrusterPositionFromCG = new ArrayList<>();
        for (Thruster thruster : thrusters) {
            thrusterType.add(thruster.getType());
            switch (thruster.getType()) {
                case ThrusterType.TUNNEL:
                    powerCoefficients.add(1.0);
                    thrusterPositionFromCG.add(Double.parseDouble(thruster.getY_cg()));
                    break;
                case ThrusterType.AZIMUTH:
                    powerCoefficients.add(0.533);
                    powerCoefficients.add(0.533);
                    thrusterPositionFromCG.add(Double.parseDouble(thruster.getX_cg()));
                    thrusterPositionFromCG.add(Double.parseDouble(thruster.getY_cg()));
                    break;
                case ThrusterType.RUDDER:
                    //TODO
                    break;
            }
        }
        vessel.setHMatrix(powerCoefficients.size(), powerCoefficients);
        vessel.setEqualityConstraintsMatrix(thrusterType, thrusterPositionFromCG);

//        DoubleMatrix1D beq = new DenseDoubleMatrix1D(new double[]{1350, 888.2873, 54987.42});
        DoubleMatrix1D beq = new DenseDoubleMatrix1D(force);

        int[] s = {0,0}; // decision variable
        //inequalities
        ConvexMultivariateRealFunction[] inequalities = this.setInequalities(thrusters, 24, s);

        //optimization problem
        OptimizationRequest or = new OptimizationRequest();

        //optimization
        JOptimizer opt = new JOptimizer();
        PDQuadraticMultivariateRealFunction objective = new PDQuadraticMultivariateRealFunction(vessel.getHMatrix().toArray(), null, 0);

        //optimization problem
        OptimizationRequest orr = new OptimizationRequest();
        orr.setF0(objective);
        orr.setA(vessel.getEqualityConstraintsMatrix().toArray());
        orr.setB(beq.toArray());
//        orr.setInitialPoint(new double[] {0.1,0.9});
        orr.setFi(inequalities); //if you want x>0 and y>0
        orr.setToleranceFeas(1.E-6);
        orr.setTolerance(1.E-6);
        opt.setOptimizationRequest(orr);
        opt.optimize();

        double[] sol = opt.getOptimizationResponse().getSolution();
        // incase of inequalities, need to check if angle is within forbidden zone.
        // if this is the case we need to reallocate with different decision variable s
        double angle = Math.atan2(sol[5], sol[4]) * 180 / Math.PI;
        if (angle > 85 && angle < 105) {
            s[0] = 1;
            s[1] = 0;
            inequalities = this.setInequalities(thrusters, 24, s);
            orr.setFi(inequalities);
            opt.setOptimizationRequest(orr);
            opt.optimize();
            sol = opt.getOptimizationResponse().getSolution();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time with QP " + (endTime - startTime) + " milliseconds");

        return sol;
    }

    private ConvexMultivariateRealFunction[] setInequalities(List<Thruster> thrusters, int N, int[] s) {
        int numberOfColumns = 0;
        int numberOfRows = 0;
        for (Thruster thruster : thrusters) {
            switch (thruster.getType()) {
                case ThrusterType.TUNNEL:
                    numberOfRows +=1;
                    //TODO
                    break;
                case ThrusterType.AZIMUTH:
                    numberOfRows +=2;
                    if (thruster.getForbiddenZonesActive()) {
                        numberOfColumns+= N+2;
                    } else {
                        numberOfColumns+=N;
                    }
                    break;
                case ThrusterType.RUDDER:
                    //TODO
                    break;
            }
        }
        ConvexMultivariateRealFunction[] inequalities = new ConvexMultivariateRealFunction[numberOfColumns];

        int rowCounter = 0;
        int columnCounter = 0;
        double h[] = new double[numberOfRows];
        for (Thruster thruster : thrusters) {
            switch (thruster.getType()) {
                case ThrusterType.TUNNEL:
                    rowCounter +=1;
                    //TODO
                    break;
                case ThrusterType.AZIMUTH:
                    rowCounter +=2;
                    double r = Double.valueOf(thruster.getEffect()) * Math.cos(Math.PI/N);
                    for (int k = 0; k < N; k++) {
                        Arrays.fill(h, 0);
                        double theta = (2*k + 1)*Math.PI/N;
                        h[rowCounter-2] = -Math.cos(theta);
                        h[rowCounter-1] = -Math.sin(theta);
                        inequalities[columnCounter + k] = new LinearMultivariateRealFunction(h, -r);
                        columnCounter++;
                    }
                    if (thruster.getForbiddenZonesActive()) {
                        Double M = Double.valueOf(thruster.getEffect()); //big-M constant
                        // angles are counterclockwise
                        Double startAngle = Double.valueOf(thruster.getForbiddenZoneEnd());
                        Double endAngle = Double.valueOf(thruster.getForbiddenZoneStart());

                        double forbiddenStart = startAngle * Math.PI/180;
                        double forbiddenEnd = endAngle * Math.PI/180;
                        h[rowCounter-2] = Math.sin(forbiddenStart);
                        h[rowCounter-1] = -Math.cos(forbiddenStart);
                        inequalities[columnCounter] = new LinearMultivariateRealFunction(h, -M*(1 - s[0]));
                        columnCounter++;
                        h[rowCounter-2] = -Math.sin(forbiddenEnd);
                        h[rowCounter-1] = Math.cos(forbiddenEnd);
                        inequalities[columnCounter] = new LinearMultivariateRealFunction(h, -M*(1 - s[1]));
                        columnCounter++;
                    }
                    break;
                case ThrusterType.RUDDER:
                    //TODO
                    break;
            }
        }

        // Thruster 3 - azimuth

        // Thruster 4 - azimuth
//        for (int k = 0; k < N; k++) {
//            double theta = (2*k + 1)*Math.PI/N;
//            inequalities[k+N] = new LinearMultivariateRealFunction(new double[]{0, 0, 0, 0, -Math.cos(theta), -Math.sin(theta)}, -r);
//        }

        // adding forbidden zones to thruster 4, angles are counterclockwise
//        if (forbiddenZones) {
//            Double M = Double.valueOf(thrusters.get(3).getEffect()); //big-M constant
//            double startAngle = 105;
//            double endAngle = 85;
//
//            double forbiddenStart = startAngle * Math.PI/180;
//            double forbiddenEnd = endAngle * Math.PI/180;
//            inequalities[size-2] = new LinearMultivariateRealFunction(new double[]{0, 0, 0, 0, Math.sin(forbiddenStart), -Math.cos(forbiddenStart)}, -M*(1 - s[0]));
//            inequalities[size-1] = new LinearMultivariateRealFunction(new double[]{0, 0, 0, 0, -Math.sin(forbiddenEnd), Math.cos(forbiddenEnd)}, -M*(1 - s[1]));
//        }

        return inequalities;
    }
}
