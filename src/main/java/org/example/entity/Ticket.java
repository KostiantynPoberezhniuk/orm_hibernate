package org.example.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false,
            insertable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false,
            foreignKey = @ForeignKey(name = "ticket_client_fk"))
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_planet_id", nullable = false,
            foreignKey = @ForeignKey(name = "ticket_from_planet_fk"))
    private Planet fromPlanet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_planet_id", nullable = false,
            foreignKey = @ForeignKey(name = "ticket_to_planet_fk"))
    private Planet toPlanet;

    public Ticket() {}

    public Ticket(Client client, Planet fromPlanet, Planet toPlanet) {
        this.client = client;
        this.fromPlanet = fromPlanet;
        this.toPlanet = toPlanet;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Planet getFromPlanet() { return fromPlanet; }
    public void setFromPlanet(Planet fromPlanet) { this.fromPlanet = fromPlanet; }

    public Planet getToPlanet() { return toPlanet; }
    public void setToPlanet(Planet toPlanet) { this.toPlanet = toPlanet; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket t)) return false;
        return id != null && Objects.equals(id, t.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Ticket{id=" + id +
                ", createdAt=" + createdAt +
                ", clientId=" + (client != null ? client.getId() : null) +
                ", from=" + (fromPlanet != null ? fromPlanet.getId() : null) +
                ", to=" + (toPlanet != null ? toPlanet.getId() : null) +
                '}';
    }
}