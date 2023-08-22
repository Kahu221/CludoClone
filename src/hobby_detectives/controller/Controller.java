package hobby_detectives.controller;
import hobby_detectives.gui.GameFrame;
import hobby_detectives.game.*;

import java.awt.event.*;
public class Controller {
    private GameFrame gameView;
    private Game game;

    public Controller(GameFrame gameView, Game game){
        this.game = game;
        this.gameView = gameView;

        //TODO need to add a listner here which needs to be implemented within the gameView;
    }

    //action listner which will listen for mouse clicks to talk to the Game object (will be useful after rending is done)
    class GameListner implements ActionListener{
        public void actionPerformed(ActionEvent arg0){

        }

    }

}
