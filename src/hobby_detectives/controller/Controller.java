package hobby_detectives.controller;
import hobby_detectives.gui.views.GameView;
import hobby_detectives.game.*;
import hobby_detectives.gui.models.StatusPanelModel;

import java.awt.event.*;

public class Controller {
    private GameView gameView;
    private Game game;

    private StatusPanelModel statusPanelModel = new StatusPanelModel();

    public Controller(){
        this.game = new Game();
        this.gameView = new GameView(this.statusPanelModel);
        this.gameView.start();
    }

    public void start() {
        this.statusPanelModel.setCharacterName("bingus");

    }

}