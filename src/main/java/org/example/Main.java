package org.example;

import org.example.entity.Client;
import org.example.entity.Planet;
import org.example.service.ClientCrudService;
import org.example.service.PlanetCrudService;
import org.flywaydb.core.Flyway;
import org.h2.tools.Server;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Main {

    private static final String JDBC_URL = "jdbc:h2:mem:spacetravel;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) throws IOException, SQLException {
        runFlyway();

        try (SessionFactory sessionFactory = buildSessionFactory()) {
            System.out.println("Hibernate SessionFactory started successfully.");

            ClientCrudService clientService = new ClientCrudService(sessionFactory);
            PlanetCrudService planetService = new PlanetCrudService(sessionFactory);

            runPlanetCrudDemo(planetService);
            runClientCrudDemo(clientService);

            startH2ConsoleAndWait();
        }

        System.out.println("SpaceTravel project stopped.");
    }

    private static void runFlyway() {
        Flyway.configure()
                .dataSource(JDBC_URL, DB_USER, DB_PASSWORD)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        System.out.println("Flyway migrations applied successfully.");
    }

    private static SessionFactory buildSessionFactory() {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(Planet.class)
                .buildSessionFactory();
    }

    private static void runPlanetCrudDemo(PlanetCrudService service) {
        System.out.println("\n=============== PLANET CRUD ===============");
        System.out.println("Planets before: " + service.findAll().size());

        Planet neptune = service.save(new Planet("NEPTUNE", "Neptune"));
        System.out.println("CREATE: " + neptune);

        Optional<Planet> found = service.findById("NEPTUNE");
        System.out.println("READ by id:  " + found.orElse(null));

        List<Planet> all = service.findAll();
        System.out.println("READ all (" + all.size() + "): " + all);

        neptune.setName("Neptune (Ice Giant)");
        Planet updated = service.update(neptune);
        System.out.println("UPDATE: " + updated);
        System.out.println("Verify after update: " + service.findById("NEPTUNE").orElse(null));

        boolean deleted = service.deleteById("NEPTUNE");
        System.out.println("DELETE: success=" + deleted);
        System.out.println("Verify after delete: " + service.findById("NEPTUNE").orElse(null));

        System.out.println("Planets after: " + service.findAll().size());
    }

    private static void runClientCrudDemo(ClientCrudService service) {
        System.out.println("\n=============== CLIENT CRUD ===============");
        System.out.println("Clients before: " + service.findAll().size());

        Client newClient = service.save(new Client("Test Client"));
        Long newId = newClient.getId();
        System.out.println("CREATE: " + newClient + " (assigned id=" + newId + ")");

        Optional<Client> found = service.findById(newId);
        System.out.println("READ by id:  " + found.orElse(null));

        System.out.println("READ all count: " + service.findAll().size());

        newClient.setName("Renamed Test Client");
        Client updated = service.update(newClient);
        System.out.println("UPDATE: " + updated);
        System.out.println("Verify after update: " + service.findById(newId).orElse(null));

        boolean deleted = service.deleteById(newId);
        System.out.println("DELETE: success=" + deleted);
        System.out.println("Verify after delete: " + service.findById(newId).orElse(null));

        boolean deleteMissing = service.deleteById(999_999L);
        System.out.println("DELETE non-existent: success=" + deleteMissing + " (expected false)");

        System.out.println("Clients after: " + service.findAll().size());
    }

    private static void startH2ConsoleAndWait() throws SQLException, IOException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        System.out.println("\nH2 console: http://localhost:8082");
        System.out.println("   JDBC URL: " + JDBC_URL);
        System.out.println("   User: " + DB_USER + " (no password)");
        System.out.println("Press Enter to exit...");
        System.in.read();
    }
}