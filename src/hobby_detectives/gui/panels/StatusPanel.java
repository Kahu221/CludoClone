package hobby_detectives.gui.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class StatusPanel extends JPanel {
    private JLabel currentPlayer = new JLabel("some Player");
    

    public StatusPanel() {
        
    }

    public void setUp(){
        // 20% of the parents width
        //int preferredWidth = (int) (this.getParent().getSize().width * 0.2);
        //100% of the parens height
        //int preferredHeight = this.getParent().getSize().height;

        this.add(new JLabel("Status panel"));
        this.setBackground(Color.GRAY);
        // this.setPreferredSize(new Dimension( preferredWidth, preferredHeight));
        //this.add(new JLabel(Integer.toString(this.getParent().getSize().height)));
    }

}
