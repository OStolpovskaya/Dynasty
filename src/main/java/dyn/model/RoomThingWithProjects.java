package dyn.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 30.03.2017.
 */
public class RoomThingWithProjects {
    private RoomThing roomThing;
    private Project currentProject;
    private List<Project> availableProjects;

    public RoomThingWithProjects(RoomThing roomThing) {
        this.roomThing = roomThing;
    }

    public RoomThing getRoomThing() {
        return roomThing;
    }

    public void setRoomThing(RoomThing roomThing) {
        this.roomThing = roomThing;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public List<Project> getAvailableProjects() {
        return availableProjects;
    }

    public void setAvailableProjects(List<Project> availableProjects) {
        this.availableProjects = availableProjects;
    }

    public String getStringForModal() {
        StringBuilder sb = new StringBuilder();
        sb.append(roomThing.getThing().getId()).append(",");
        sb.append("'").append(roomThing.getThing().getName()).append("',");
        sb.append(roomThing.getId()).append(",");
        sb.append("knownThing").append(",");
        sb.append(currentProject == null ? "0" : currentProject.getId()).append(",");
        sb.append("'").append(currentProject == null ? "Нет вещи" : currentProject.getName()).append("',");
        List<String> availableProjectIds = new ArrayList<>();
        List<String> availableProjectNames = new ArrayList<>();
        List<String> availableProjectViews = new ArrayList<>();
        for (Project availableProject : availableProjects) {
            availableProjectIds.add(availableProject.getId().toString());
            availableProjectNames.add("'" + availableProject.getName() + "'");
            availableProjectViews.add("'" + availableProject.getEncodedView() + "'");
        }
        sb.append("[").append(String.join(",", availableProjectNames)).append("],");
        sb.append("[").append(String.join(",", availableProjectIds)).append("],");
        sb.append("[").append(String.join(",", availableProjectViews)).append("]");
        return sb.toString();
    }
}
