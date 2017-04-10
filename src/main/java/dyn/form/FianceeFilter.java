package dyn.form;

import dyn.model.Race;

/**
 * Created by OM on 10.04.2017.
 */
public class FianceeFilter {
    private Race race;

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FianceeFilter{");
        sb.append("race=").append(race == null ? "null" : race.getName());
        sb.append('}');
        return sb.toString();
    }
}
