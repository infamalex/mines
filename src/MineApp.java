import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

/**
 * Created by Alex on 09/02/2018.
 */
public final class MineApp extends JFrame {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        SwingUtilities.invokeLater(MineApp::new);
    }

    private final JPanel grid;

    public MineApp(){
        this(10,10,10);
    }
    public MineApp(int w, int h, int b){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel resetPanel = new JPanel();
        //resetPanel.setLayout(new BoxLayout(resetPanel, BoxLayout.LINE_AXIS));
        JButton resetButton = new JButton("R");
        resetButton.setFocusable(false);
        resetButton.setAlignmentX(SwingConstants.CENTER);
        resetButton.addActionListener(e->reset(w,h,b));
        resetPanel.add(resetButton);
        getContentPane().add(resetPanel,BorderLayout.NORTH);

        grid = new JPanel(new GridLayout(w,h));
        getContentPane().add(grid,BorderLayout.CENTER);
        reset(w,h,b);

        grid.setBorder(new EmptyBorder(5,15,15,15));
        setVisible(true);
        pack();
    }

    public void reset(int x,int y, int b){

        grid.removeAll();
        List<Button> buttons = Stream.concat(Stream.generate(Button::bomb).limit(b),Stream.generate(Button::clue)).
                limit(x*y).
                collect(toCollection(()-> new ArrayList<>(x*y)));
        Collections.shuffle(buttons);

        for (int i = 0; i < buttons.size();i++) {

            Button o = buttons.get(i);
            grid.add(o);
            if(o.getType() == Button.Type.BOMB) continue;
            //IntConsumer clear = in-> surrounding(buttons,x,in, bu->bu.getType() == Button.Type.BOMB)
            int count = (int)surrounding(buttons,x,i,bu->bu.getType() == Button.Type.BOMB).count();
            o.setCount(count);
        }
        repaint();
        pack();
    }

    private static boolean between(int mini, int i, int maxi){
        return mini<=i && i <= maxi;
    }

    private static <T> Stream<? extends T> surrounding(List<? extends T> items,int w, int index, Predicate<T> test){
        int[] indexs = {index-1,index+1,index-w,index-w-1,index-w+1,index+w,index+w-1,index+w+1};
        return Arrays.stream(indexs).
                filter(n->between(0,n,items.size()-1) && between(-1,n%w - index%w,1)).
                mapToObj(items::get).
                filter(test);
    }

    //private
}
