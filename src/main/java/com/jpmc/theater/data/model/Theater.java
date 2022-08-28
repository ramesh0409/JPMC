package com.jpmc.theater.data.model;

import java.util.Objects;
import java.util.Set;

public class Theater {

    private final String id;
    private final String name;
    private final int capacity;
    private Set<Integer> currentShows;

    public Theater(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public Set<Integer> getCurrentShows() {
        return currentShows;
    }

    public void setCurrentShows(Set<Integer> currentShows) {
        this.currentShows = currentShows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Theater)) return false;
        Theater theater = (Theater) o;
        return getCapacity() == theater.getCapacity() && Objects.equals(getId(), theater.getId()) && Objects.equals(getName(), theater.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCapacity());
    }
}
