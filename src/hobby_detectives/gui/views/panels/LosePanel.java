package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;

public class LosePanel extends JPanel {

    public LosePanel(GameController controller) {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Your guess was incorrect, you will no longer be able to make a solve attempt");
        JButton accept = new JButton("Ok");
        this.add(title);
        this.add(accept);
        this.setVisible(true);

        accept.addActionListener(clicked -> controller.endTurn());
    }
}
