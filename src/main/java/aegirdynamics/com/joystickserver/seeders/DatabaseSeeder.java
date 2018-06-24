package aegirdynamics.com.joystickserver.seeders;

import aegirdynamics.com.joystickserver.thruster.Thruster;
import aegirdynamics.com.joystickserver.thruster.ThrusterRepository;
import aegirdynamics.com.joystickserver.thrusterType.ThrusterType;
import aegirdynamics.com.joystickserver.thrusterType.ThrusterTypeRepository;
import aegirdynamics.com.joystickserver.vessel.Vessel;
import aegirdynamics.com.joystickserver.vessel.VesselRepository;
import aegirdynamics.com.joystickserver.vesselType.VesselType;
import aegirdynamics.com.joystickserver.vesselType.VesselTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder {

    private VesselTypeRepository vesselTypeRepository;
    private VesselRepository vesselRepository;
    private ThrusterRepository thrusterRepository;
    private ThrusterTypeRepository thrusterTypeRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseSeeder(
            VesselTypeRepository vesselTypeRepository,
            ThrusterTypeRepository thrusterTypeRepository,
            VesselRepository vesselRepository,
            ThrusterRepository thrusterRepository,
            JdbcTemplate jdbcTemplate) {
        this.vesselTypeRepository = vesselTypeRepository;
        this.thrusterTypeRepository = thrusterTypeRepository;
        this.vesselRepository = vesselRepository;
        this.thrusterRepository = thrusterRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedVesselTypeTable();
        seedThrusterTypeTable();
        seedVesselTable();
    }

    private void seedThrusterTypeTable() {
        String sql = "SELECT id, type FROM thruster_type tt WHERE tt.id = 1 LIMIT 1";
        List<ThrusterType> list = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if(list.size() <= 0) {
            ThrusterType tt = new ThrusterType();
            tt.setId(1);
            tt.setType("Tunnel");
            thrusterTypeRepository.save(tt);
            tt.setId(2);
            tt.setType("Azimuth");
            thrusterTypeRepository.save(tt);
            tt.setId(3);
            tt.setType("Rudder");
            thrusterTypeRepository.save(tt);
            System.out.println("Done seeding thruster type table");
        } else {
            System.out.println("Seeding Not Required");
        }
    }

    private void seedVesselTypeTable() {
        String sql = "SELECT id, type FROM vessel_type vt WHERE vt.id = 1 OR vt.type = \"Tunnel\" LIMIT 1";
        List<VesselType> list = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if(list.size() <= 0) {
            VesselType vt = new VesselType();
            vt.setId(1);
            vt.setType("OSV");
            vesselTypeRepository.save(vt);
            vt.setId(2);
            vt.setType("Cargo");
            vesselTypeRepository.save(vt);
            vt.setId(3);
            vt.setType("Tug");
            vesselTypeRepository.save(vt);
            vt.setId(4);
            vt.setType("Passenger");
            vesselTypeRepository.save(vt);
            vt.setId(5);
            vt.setType("Fishing");
            vesselTypeRepository.save(vt);
            System.out.println("Done seeding vessel type table");
        } else {
            System.out.println("Seeding Not Required");
        }
    }

    private void seedVesselTable() {
        String sql = "SELECT id FROM vessel v WHERE v.id = 1 LIMIT 1";
        List<Vessel> vessels = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if(vessels.size() <= 0) {
            Vessel vessel = new Vessel();
            vessel.setId(1);
            vessel.setName("Olympic Electra");
            vessel.setHull("H1201");
            vessel.setDate("2011-05-12");
            vessel.setLength("76.66");
            vessel.setWidth("16.41");
            vessel.setStageAnchorPoints("[41,133,164,13,224,10,328,145,325,165,326,265,326,366,325,383,229,398,125,401,40,389,38,374,35,281,36,153]");
            vessel.setType(1);
            vesselRepository.save(vessel);
            System.out.println("Done seeding vessel table");
            this.seedThrusterTable(vessel);
        } else {
            System.out.println("Seeding Not Required");
        }
    }

    private void seedThrusterTable(Vessel vessel) {
        String sql = "SELECT id FROM thruster t WHERE t.id = 1";
        List<Thruster> thrusters = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (thrusters.size() <= 0) {
            Thruster t1 = new Thruster();
            t1.setNumber(1);
            t1.setEffect("800");
            t1.setType(1);
            t1.setX_cg("7");
            t1.setY_cg("34");
            t1.setStageNode("{\"attrs\":{\"draggable\":true,\"x\":228,\"y\":81,\"rotation\":90,\"width\":14,\"height\":85,\"cornerRadius\":3,\"stroke\":\"black\",\"strokeWidth\":1.2,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"Rect\"}");
            t1.setVessel(vessel);
            thrusterRepository.save(t1);

            Thruster t2 = new Thruster();
            t2.setNumber(2);
            t2.setEffect("800");
            t2.setType(1);
            t2.setX_cg("7");
            t2.setY_cg("31");
            t2.setStageNode("{\"attrs\":{\"draggable\":true,\"x\":229,\"y\":129,\"rotation\":90,\"width\":14,\"height\":85,\"cornerRadius\":3,\"stroke\":\"black\",\"strokeWidth\":1.2,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"Rect\"}");
            t2.setVessel(vessel);
            thrusterRepository.save(t2);

            Thruster t3 = new Thruster();
            t3.setNumber(3);
            t3.setEffect("1200");
            t3.setType(2);
            t3.setX_cg("-5");
            t3.setY_cg("-36");
            t3.setStageNode("{\"attrs\":{\"draggable\":true,\"x\":101,\"y\":333,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffset\":{\"x\":8,\"y\":8},\"startScale\":1,\"shadowOffsetX\":null,\"shadowOffsetY\":null},\"className\":\"Group\",\"children\":[{\"attrs\":{\"innerRadius\":40,\"outerRadius\":55,\"stroke\":\"black\",\"strokeWidth\":1.2,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"Ring\"},{\"attrs\":{\"x\":-7,\"y\":-37,\"width\":14,\"height\":74,\"cornerRadius\":3,\"stroke\":\"black\",\"strokeWidth\":1.2,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"Rect\"},{\"attrs\":{\"y\":-45,\"sides\":3,\"radius\":6.5,\"stroke\":\"black\",\"strokeWidth\":1,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"RegularPolygon\"}]}");
            t3.setVessel(vessel);
            thrusterRepository.save(t3);

            Thruster t4 = new Thruster();
            t4.setNumber(4);
            t4.setEffect("1200");
            t4.setType(2);
            t4.setX_cg("5");
            t4.setY_cg("-36");
            t4.setStageNode("{\"attrs\":{\"draggable\":true,\"x\":261,\"y\":330,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffset\":{\"x\":8,\"y\":8},\"startScale\":1,\"shadowOffsetX\":null,\"shadowOffsetY\":null},\"className\":\"Group\",\"children\":[{\"attrs\":{\"innerRadius\":40,\"outerRadius\":55,\"stroke\":\"black\",\"strokeWidth\":1.2,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"Ring\"},{\"attrs\":{\"x\":-7,\"y\":-37,\"width\":14,\"height\":74,\"cornerRadius\":3,\"stroke\":\"black\",\"strokeWidth\":1.2,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"Rect\"},{\"attrs\":{\"y\":-45,\"sides\":3,\"radius\":6.5,\"stroke\":\"black\",\"strokeWidth\":1,\"opacity\":0.8,\"shadowOpacity\":0.6,\"shadowColor\":\"black\",\"shadowBlur\":10,\"shadowOffsetX\":5,\"shadowOffsetY\":5,\"startScale\":1,\"dash\":[0,0]},\"className\":\"RegularPolygon\"}]}");
            t4.setVessel(vessel);
            thrusterRepository.save(t4);

            System.out.println("Done seeding thruster table");

        } else {
            System.out.println("Seeding Not Required");
        }
    }
}
