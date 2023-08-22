package hobby_detectives.gui.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StatusPanelModel {
    private final PropertyChangeSupport observable = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.observable.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.observable.removePropertyChangeListener(listener);
    }

    private String characterName;

    public String getCharacterName() { return this.characterName; }
    public void setCharacterName(String newCharacterName) {
        String oldName = this.characterName;
        this.characterName = newCharacterName;
        this.observable.firePropertyChange("characterName", oldName, newCharacterName);
    }
}
