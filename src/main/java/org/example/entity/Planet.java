package org.example.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "planet")
public class Planet {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    public Planet() {}

    public Planet(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Planet p)) return false;
        return id != null && Objects.equals(id, p.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Planet{id='" + id + "', name='" + name + "'}";
    }
}