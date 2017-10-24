package dyn.form;

import dyn.model.Race;
import dyn.model.career.Vocation;

import java.util.List;

/**
 * Created by OM on 10.04.2017.
 */
public class FianceeFilter {
    private Race race;

    private Vocation vocation;

    private List<String> appearance;

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

    public List<String> getAppearance() {
        return appearance;
    }

    public void setAppearance(List<String> appearance) {
        this.appearance = appearance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FianceeFilter{");
        sb.append("race=").append(race == null ? "null" : race.getName()).append(", ");
        sb.append("vocation=").append(vocation == null ? "null" : vocation.getName()).append(", ");
        sb.append("appearance=").append(appearance == null ? "null" : appearance.toString());
        sb.append('}');
        return sb.toString();
    }

    public boolean isEmpty() {
        return race == null && vocation == null && appearance == null;

    }
}
