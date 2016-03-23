/**
 * Created by Jasmine on 3/21/16.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class Minefield {

    private static final int SIZE = 25;
    private static int MINE_NUM = 70;
    private JFrame window = new JFrame();
    private JPanel panel = new JPanel();
    private JPanel panel1 = new JPanel();
    private JTextField mines = new JTextField("70");
    private JLabel label = new JLabel("丑丑哒扫雷程序", JLabel.CENTER);
    private JButton[][] button;
    private int[][] status;
    private int[][] clicked;
    private int uncovered = 0;
    public Minefield() {
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.setSize(500, 600);
        window.setLocation(50, 50);
        window.setTitle("Minefield");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setVisible(true);


        panel.setLayout(new GridLayout(SIZE,SIZE));
        button = new JButton[SIZE][SIZE];
        status = new int[SIZE][SIZE];
        clicked = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; ++i){
            for (int j = 0; j < SIZE; ++j) {
                button[i][j] = new JButton();
                button[i][j].setOpaque(true);
                button[i][j].setMargin(new Insets(0,0,0,0));
                final int _i = i; final int _j = j;
                button[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            clickedBtn(_i,_j);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            if (clicked[_i][_j] == 0) {
                                button[_i][_j].setBackground(Color.BLUE);
                                clicked[_i][_j] = -1;
                                uncovered++;
                            } else if (clicked[_i][_j] == -1) {
                                button[_i][_j].setBackground(Color.white);
                                clicked[_i][_j] = 0;
                                uncovered--;
                            }
                        }
                        if (uncovered>=SIZE*SIZE-MINE_NUM) {
                            JOptionPane.showMessageDialog(window, "算你赢");
                            initialize();
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                });
                panel.add(button[i][j]);
                button[i][j].setOpaque(true);
                button[i][j].setVisible(true);
            }
        }
        initialize();

        panel.setOpaque(true);
        panel.setVisible(true);
        window.add(panel, BorderLayout.CENTER);
        panel1.setLayout(new BorderLayout());
        label.setFont(new Font("Arial", Font.BOLD, 36));
        panel1.add(label, BorderLayout.CENTER);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        JLabel mineLabel = new JLabel("地雷数:  ");
        JLabel label1 = new JLabel();
        label1.setPreferredSize(new Dimension(150,10));
        JLabel label2 = new JLabel();
        label2.setPreferredSize(new Dimension(150,10));
        JButton submitMines = new JButton("更改");
        mineLabel.setFont(new Font("Arial", Font.BOLD, 10));
        panel2.add(label1);
        panel2.add(mineLabel);
        panel2.add(mines);
        panel2.add(submitMines);
        panel2.add(label2);
        panel1.add(panel2, BorderLayout.SOUTH);
        window.add(panel1, BorderLayout.NORTH);

        submitMines.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int new_mine_num = Integer.parseInt(mines.getText());
                    if (new_mine_num <= 0) {
                        JOptionPane.showMessageDialog(window, "别想骗人←_←");
                    } else if (new_mine_num >= SIZE*SIZE) {
                        JOptionPane.showMessageDialog(window, "没那么多格子←_←");
                    } else {
                        MINE_NUM = new_mine_num;
                        initialize();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(window, "不许随便乱输入←_←");
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        panel.revalidate();
    }

    private void initialize() {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                button[i][j].setBackground(Color.WHITE);
                button[i][j].setText("");
                status[i][j] = 0;
                clicked[i][j] = 0;
                uncovered = 0;
            }
        }
        generateRandomMines();

    }


    private void generateRandomMines(){

        Set<Integer> randomList = new HashSet<Integer>();
        Random rand=new Random();
        int listSize = 0;
        while (listSize < MINE_NUM) {
            int rand_num = rand.nextInt(SIZE*SIZE);
            if (randomList.contains(rand_num))
                continue;
            randomList.add(rand_num);
            listSize++;
        }
        for (Integer rand_num : randomList) {
            status[rand_num/SIZE][rand_num%SIZE] = -1;
        }
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                int mine_num = 0;
                if (status[i][j] == -1) {
                    continue;
                }
                if (i != 0) {
                    mine_num += (status[i-1][j] == -1) ? 1 : 0;
                    if (j != 0)
                        mine_num += (status[i-1][j-1] == -1) ? 1 : 0;
                    if (j != SIZE-1)
                        mine_num += (status[i-1][j+1] == -1) ? 1 : 0;
                }
                if (i != SIZE-1) {
                    mine_num += (status[i+1][j] == -1) ? 1 : 0;
                    if (j != 0)
                        mine_num += (status[i+1][j-1] == -1) ? 1 : 0;
                    if (j != SIZE-1)
                        mine_num += (status[i+1][j+1] == -1) ? 1 : 0;
                }
                if (j != 0)
                    mine_num += (status[i][j-1] == -1) ? 1 : 0;
                if (j != SIZE-1)
                    mine_num += (status[i][j+1] == -1) ? 1 : 0;
                status[i][j] = mine_num;
            }
        }
    }

    private void clickedBtn(int i, int j) {
        if (clicked[i][j] != 0) {
            return;
        }
        clicked[i][j] = 1;
        if (status[i][j] == -1) {
            button[i][j].setBackground(Color.red);
            for (int _i = 0; _i < SIZE; ++_i) {
                for (int _j = 0; _j < SIZE; ++_j) {
                    if (status[_i][_j] == -1 && clicked[_i][_j] == 0) {
                        button[_i][_j].setBackground(Color.red);
                    } else if (status[_i][_j] == -1 && clicked[_i][_j] == -1) {
                        button[_i][_j].setBackground(Color.magenta);
                    }
                }
            }
            JOptionPane.showMessageDialog(window, "你输。菜鸡←_←");
            initialize();
        } else if (status[i][j] != 0) {
            uncovered++;
            button[i][j].setBackground(Color.lightGray);
            button[i][j].setFont(new Font("Arial", Font.BOLD, 10));
            button[i][j].setText(Integer.toString(status[i][j]));
        } else {
            uncovered++;
            button[i][j].setBackground(Color.lightGray);
            if (i != 0) {
                clickedBtn(i-1,j);
                if (j != 0)
                    clickedBtn(i-1,j-1);
                if (j != SIZE-1)
                    clickedBtn(i-1,j+1);
            }
            if (i != SIZE-1) {
                clickedBtn(i+1,j);
                if (j != 0)
                    clickedBtn(i+1,j-1);
                if (j != SIZE-1)
                    clickedBtn(i+1,j+1);
            }
            if (j != 0)
                clickedBtn(i,j-1);
            if (j != SIZE-1)
                clickedBtn(i,j+1);
        }
    }


    public static void main(String... args) {
        Minefield minefield = new Minefield();
    }
}
