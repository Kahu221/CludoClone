package hobby_detectives.gui.views.panels;

import hobby_detectives.data.CharacterType;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.views.GameView;
import hobby_detectives.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SetupView extends JPanel {
    public List<Player> configuredPlayers = new ArrayList<>();
    private final int totalPlayers;
    public int currentPlayerIndex = -1;
    private final GameController controller;
    private final GameView parent;
    public SetupView(GameView parent, int totalPlayers, GameController controller) {
        this.totalPlayers = totalPlayers;
        this.parent = parent;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.startSetupForNextCharacter();
        this.controller = controller;
    }

    public void startSetupForNextCharacter() {
        this.currentPlayerIndex++;
        this.removeAll();

        var welcome = new JLabel("Welcome to Hobby Detectives");
        welcome.setFont(new Font("Arial", Font.PLAIN, 30));
        this.add(welcome);
        this.add(new PlayerSetupPanel(this.currentPlayerIndex));

        this.revalidate();
        this.repaint();
    }

    private void confirm(PlayerSetupPanel panel) {
        this.configuredPlayers.add(new Player(panel.selected, new ArrayList<>(), ""));

        if (this.configuredPlayers.size() == totalPlayers) {
            this.controller.startGame(configuredPlayers);
            this.parent.finishSetup();
        } else {
            this.startSetupForNextCharacter();
        }
    }

    class PlayerSetupPanel extends JPanel {
        private CharacterType selected;
        private final JButton confirm;

        public PlayerSetupPanel(int index) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel message = new JLabel("Player " + (index + 1) + ": Who would you like to play as?");
            ButtonGroup options = new ButtonGroup();

            confirm = new JButton("Confirm");
            confirm.setEnabled(false);
            confirm.addActionListener(c -> { confirm(this); });
            this.add(message);

            for (CharacterType type : CharacterType.values()) {
                JRadioButton button = new JRadioButton(type.name());
                if (configuredPlayers.stream().anyMatch(e -> e.getCharacter().equals(type))) {
                    button.setEnabled(false);
                }
                button.addActionListener(d -> {
                    this.selected = type;
                    confirm.setEnabled(true);
                });
                this.add(button);
                options.add(button);
            }
            this.add(confirm);
        }
    }
}
