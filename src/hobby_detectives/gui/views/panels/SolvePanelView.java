package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;

public class SolvePanelView extends JPanel {

    private final GameView parent;
    private final GameModel model;
    private final GameController controller;

    public SolvePanelView(GameView parent, GameModel model, GameController controller) {
        this.parent = parent;
        this.model = model;
        this.controller = controller;
        model.getCurrentPlayer().playerHasSolved();
    }
}
