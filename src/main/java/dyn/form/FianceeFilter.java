package dyn.form;

import dyn.model.Race;
import dyn.model.career.Vocation;

/**
 * Created by OM on 10.04.2017.
 */
public class FianceeFilter {
    private Race race;

    private Vocation vocation;

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Vocation getVocation() {
        return vocation;
    }

    public void setVocation(Vocation vocation) {
        this.vocation = vocation;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FianceeFilter{");
        sb.append("race=").append(race == null ? "null" : race.getName());
        sb.append("vocation=").append(vocation == null ? "null" : vocation.getName());
        sb.append('}');
        return sb.toString();
    }

    public boolean isEmpty() {
        return race == null && vocation == null;

    }
}
