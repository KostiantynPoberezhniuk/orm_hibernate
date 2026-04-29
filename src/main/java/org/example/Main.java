package org.example;

import org.example.dao.ClientDao;
import org.example.dao.PlanetDao;
import org.example.dao.TicketDao;
import org.example.dao.impl.ClientDaoImpl;
import org.example.dao.impl.PlanetDaoImpl;
import org.example.dao.impl.TicketDaoImpl;
import org.example.entity.Client;
import org.example.entity.Planet;
import org.example.entity.Ticket;
import org.example.service.ClientService;
import org.example.service.PlanetService;
import org.example.service.TicketService;
import org.example.service.impl.ClientServiceImpl;
import org.example.service.impl.PlanetServiceImpl;
import org.example.service.impl.TicketServiceImpl;
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

            ClientDao clientDao = new ClientDaoImpl(sessionFactory);
            ClientService clientService = new ClientServiceImpl(clientDao);

            PlanetDao planetDao = new PlanetDaoImpl(sessionFactory);
            PlanetService planetService = new PlanetServiceImpl(planetDao);

            TicketDao ticketDao = new TicketDaoImpl(sessionFactory);
            TicketService ticketService = new TicketServiceImpl(ticketDao, clientDao, planetDao);

            runPlanetCrudDemo(planetService);
            runClientCrudDemo(clientService);
            runTicketCrudDemo(ticketService);

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
                .addAnnotatedClass(Ticket.class)
                .buildSessionFactory();
    }

    private static void runPlanetCrudDemo(PlanetService service) {
        System.out.println("\n=============== PLANET CRUD ===============");
        System.out.println("Planets before: " + service.getAll().size());

        Planet neptune = service.create("NEPTUNE", "Neptune");
        System.out.println("CREATE: " + neptune);

        Optional<Planet> found = service.getById("NEPTUNE");
        System.out.println("READ by id:  " + found.orElse(null));

        List<Planet> all = service.getAll();
        System.out.println("READ all (" + all.size() + "): " + all);

        Planet updated = service.rename("NEPTUNE", "Neptune (Ice Giant)");
        System.out.println("UPDATE: " + updated);
        System.out.println("Verify after update: " + service.getById("NEPTUNE").orElse(null));

        boolean deleted = service.delete("NEPTUNE");
        System.out.println("DELETE: success=" + deleted);
        System.out.println("Verify after delete: " + service.getById("NEPTUNE").orElse(null));

        System.out.println("Planets after: " + service.getAll().size());
    }

    private static void runClientCrudDemo(ClientService service) {
        System.out.println("\n=============== CLIENT CRUD ===============");
        System.out.println("Clients before: " + service.getAll().size());

        Client newClient = service.create("Test Client");
        Long newId = newClient.getId();
        System.out.println("CREATE: " + newClient + " (assigned id=" + newId + ")");

        Optional<Client> found = service.getById(newId);
        System.out.println("READ by id:  " + found.orElse(null));

        System.out.println("READ all count: " + service.getAll().size());

        Client updated = service.rename(newId, "Renamed Test Client");
        System.out.println("UPDATE: " + updated);
        System.out.println("Verify after update: " + service.getById(newId).orElse(null));

        boolean deleted = service.delete(newId);
        System.out.println("DELETE: success=" + deleted);
        System.out.println("Verify after delete: " + service.getById(newId).orElse(null));

        boolean deleteMissing = service.delete(999_999L);
        System.out.println("DELETE non-existent: success=" + deleteMissing + " (expected false)");

        System.out.println("Clients after: " + service.getAll().size());
    }

    private static void runTicketCrudDemo(TicketService service) {
        System.out.println("\n=============== TICKET CRUD ===============");
        System.out.println("Tickets before: " + service.getAll().size());

        Ticket created = service.create(1L, "EARTH", "MARS");
        Long id = created.getId();
        System.out.println("CREATE: " + created);

        System.out.println("READ by id: " + service.getById(id).orElse(null));
        System.out.println("READ all count: " + service.getAll().size());

        Ticket updated = service.changeRoute(id, "MARS", "JUP");
        System.out.println("UPDATE: " + updated);

        boolean deleted = service.delete(id);
        System.out.println("DELETE: success=" + deleted);

        System.out.println("Tickets after: " + service.getAll().size());

        runNegativeCases(service);
    }

    private static void runNegativeCases(TicketService service) {
        System.out.println("\n--- Negative cases ---");

        expectFail(() -> service.create(null, "EARTH", "MARS"),
                "null clientId");
        expectFail(() -> service.create(999_999L, "EARTH", "MARS"),
                "non-existent client");
        expectFail(() -> service.create(1L, null, "MARS"),
                "null fromPlanetId");
        expectFail(() -> service.create(1L, "EARTH", null),
                "null toPlanetId");
        expectFail(() -> service.create(1L, "NO_SUCH", "MARS"),
                "non-existent fromPlanet");
        expectFail(() -> service.create(1L, "EARTH", "NO_SUCH"),
                "non-existent toPlanet");
        expectFail(() -> service.create(1L, "EARTH", "EARTH"),
                "same from and to");
    }

    private static void expectFail(Runnable action, String label) {
        try {
            action.run();
            System.out.println("  [FAIL] " + label + " — exception expected");
        } catch (RuntimeException e) {
            System.out.println("  [OK]   " + label + " — "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void startH2ConsoleAndWait() {
        Server server = null;
        try {
            server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("\nH2 console: http://localhost:8082");
            System.out.println("   JDBC URL: " + JDBC_URL);
            System.out.println("   User: " + DB_USER + " (no password)");
            System.out.println("Press Enter to exit...");
            System.in.read();
        } catch (Exception e) {
            System.err.println("Failed to start H2 console: " + e.getMessage());
        } finally {
            if (server != null) {
                server.stop();
            }
        }
    }
}