package dyn.model.appearance;

/**
 * Created by OM on 21.02.2017.
 */
public interface Appearance {
    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    String getTitle();

    void setTitle(String title);

    AppearanceType getType();

    void setType(AppearanceType type);
}
