package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;

import javax.swing.*;
import java.awt.*;

public class WinPanel extends JPanel {

    public WinPanel(GameModel model) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Congratulations " + model.getCurrentPlayer() + ", you have won the game!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.PLAIN, 40));
        JButton accept = new JButton("Exit");
        accept.setAlignmentX(CENTER_ALIGNMENT);

        accept.addActionListener(click -> {
            System.exit(0);
        });

        this.add(title);
        this.add(accept);
        this.setVisible(true);
    }
}
