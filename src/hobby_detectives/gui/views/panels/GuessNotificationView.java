package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GuessNotificationView {
    private final GameView parent;

    public GuessNotificationView(GameView parent) {
        this.parent = parent;
        var frame = new JDialog(this.parent, "Would you like to make a guess?");
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Would you like to make a guess or end your turn?");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        frame.add(title);
        frame.pack();
        frame.setVisible(true);
    }

}
