package aegirdynamics.com.joystickserver.seeders;

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
    private ThrusterTypeRepository thrusterTypeRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseSeeder(
            VesselTypeRepository vesselTypeRepository,
            ThrusterTypeRepository thrusterTypeRepository,
            VesselRepository vesselRepository,
            JdbcTemplate jdbcTemplate) {
        this.vesselTypeRepository = vesselTypeRepository;
        this.thrusterTypeRepository = thrusterTypeRepository;
        this.vesselRepository = vesselRepository;
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
            vessel.setHull("H1202");
            vessel.setDate("2010-05-12");
            vessel.setLength("92");
            vessel.setWidth("11");
            vessel.setStageAnchorPoints("[39,207,166,10,222,4,335,214,334,217,336,310,331,370,328,371,267,393,73,399,38,384,36,382,37,315,38,207]");
            vessel.setType(1);
            vesselRepository.save(vessel);

            vessel.setId(2);
            vessel.setName("Edda Ferd");
            vessel.setHull("H784");
            vessel.setDate("2012-08-22");
            vessel.setLength("86");
            vessel.setWidth("13");
            vessel.setType(1);
            vesselRepository.save(vessel);
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
            System.out.println("Seeded");
        } else {
            System.out.println("Seeding Not Required");
        }
    }
}
