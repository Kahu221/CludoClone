package hobby_detectives.gui.views.transitive;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.player.Player;

import javax.swing.*;
import java.awt.*;

public class WaitingForPlayerView extends JPanel {
    public WaitingForPlayerView(GameModel model, GameController controller) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        var welcome = new JLabel("Waiting for " + model.getCurrentPlayer().getCharacter().toString());
        welcome.setFont(new Font("Arial", Font.PLAIN, 30));

        JButton acknowledgePresence = new JButton(
                "Please pass the tablet to " + model.getCurrentPlayer().getCharacter().toString()
        );
        acknowledgePresence.addActionListener(onClick -> {
            controller.confirmPlayerChange();
        });
        this.add(welcome);
        this.add(acknowledgePresence);
    }
}
