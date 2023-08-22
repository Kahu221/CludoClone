package hobby_detectives;

import hobby_detectives.gui.controller.GameController;

import javax.swing.*;

public class Main {

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            try{
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }catch(Exception e){
                e.printStackTrace();
            }
            var controller = new GameController();
            controller.start();
        });
    }
}
