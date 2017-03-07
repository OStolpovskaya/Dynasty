package dyn.model;

import javax.persistence.*;

@Entity
@Table(name = "characters_buffs")
public class CharacterBuff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "character_id")
    private Character character;

    @OneToOne
    @JoinColumn(name = "buff_id")
    private Buff buff;

    //==================================================================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Buff getBuff() {
        return buff;
    }

    public void setBuff(Buff buff) {
        this.buff = buff;
    }
}