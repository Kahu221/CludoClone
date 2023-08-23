package hobby_detectives;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;

public class Main {

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            try{
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }catch(Exception e){
                e.printStackTrace();
            }
            var model = new GameModel();
            var controller = new GameController(model);
            var view = new GameView(model, controller);
            view.setVisible(true);
        });
    }
}
