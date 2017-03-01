package dyn.form;

import dyn.model.Character;
import dyn.model.Family;

import javax.validation.Valid;

/**
 * Created by OM on 01.03.2017.
 */
public class FamilyForm {
    @Valid
    private Family family;

    private Character founder;

    private Character foundress;

    public FamilyForm() {

    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Character getFounder() {
        return founder;
    }

    public void setFounder(Character founder) {
        this.founder = founder;
    }

    public Character getFoundress() {
        return foundress;
    }

    public void setFoundress(Character foundress) {
        this.foundress = foundress;
    }
}
