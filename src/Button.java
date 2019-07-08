import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by Alex on 09/02/2018.
 */
public final class Button extends JButton {
    private static ImageIcon getImage(String url){
        try{
            return new ImageIcon(ImageIO.read(Button.class.getResourceAsStream(url)));
        } catch (IOException | IllegalArgumentException e) {
            System.err.printf("%s could not be found!%n",url);
            return null;
        }
    }

    private static final ImageIcon flag = getImage("flag.png");
    private static final ImageIcon bomb = getImage("bomb.png");
    public static Button bomb(){return new Button(Type.BOMB);}
    public static Button clue(){return new Button(Type.CLUE);}

    private final Type t;
    private boolean flagged = false;
    private int count;
    private IntConsumer clear;

    enum Type{BOMB,CLUE}
    public Button (Type t){
        this.t = t;
        setPreferredSize(new Dimension(20,20));
        setFocusable(false);
        setMargin(new Insets(0,0,0,0));
        setFont(new Font(Font.MONOSPACED,0,12));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                action(e,true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                action(e,false);
            }

            public void action(MouseEvent e, boolean pressed){
                boolean alt = false;
                switch (e.getButton()){
                    case 3:alt = true; case 1:
                        getModel().setArmed(pressed);
                        getModel().setPressed(pressed);
                }
                if(!pressed)
                    clicked(alt);
            }
        });
    }

    public Type getType() {
        return t;
    }

    private void clicked(boolean alt){
        checks:if(alt) {
            flagged = !flagged;
            setIcon(flagged?flag:null);
        }
        else {
            if(flagged) break checks;
            setEnabled(false);
            if(t == Type.BOMB){
                setIcon(bomb);
                setBackground(Color.RED);
            }
            else
                setText(""+count);
        }
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setClear(IntConsumer clear) {
        this.clear = clear;
    }
}
