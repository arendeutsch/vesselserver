package aegirdynamics.com.joystickserver.seeders;

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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseSeeder(
            VesselTypeRepository vesselTypeRepository,
            VesselRepository vesselRepository,
            JdbcTemplate jdbcTemplate) {
        this.vesselTypeRepository = vesselTypeRepository;
        this.vesselRepository = vesselRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedVesselTypeTable();
        seedVesselTable();
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
            vessel.setType(2);
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
            System.out.println("Users Seeding Not Required");
        }
    }

    private void seedVesselTypeTable() {
        String sql = "SELECT id, type FROM vessel_type vt WHERE vt.id = 1 OR vt.type = \"Tunnel\" LIMIT 1";
        List<VesselType> list = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if(list.size() <= 0) {
            VesselType vt = new VesselType();
            vt.setId(1);
            vt.setType("Cargo");
            vesselTypeRepository.save(vt);
            vt.setId(2);
            vt.setType("OSV");
            vesselTypeRepository.save(vt);
            vt.setId(3);
            vt.setType("Tug");
            vesselTypeRepository.save(vt);
            System.out.println("Users Seeded");
        } else {
            System.out.println("Users Seeding Not Required");
        }
    }
}
