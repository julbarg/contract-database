package com.contract.entities.provider;

import java.util.List;

/**
 * Created by julbarra on 5/31/17.
 */
public class Curriculum {

    private List<Services> services;

    private Experience experience;

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public Experience getExperience() {
        return experience != null ? experience : new Experience();
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }
}
