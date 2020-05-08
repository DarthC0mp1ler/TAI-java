package Implementation;

import Implementation.kClustering.KClustering;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

public class Main extends JPanel {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }


    public Main() {


        UIDefaults uiDefaults = UIManager.getDefaults();
        uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.black));
        uiDefaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(new Color(20, 179, 78)));
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Clustering");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Implementation/Resources/UIHere.png");
        frame.setIconImage(icon);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.setLocation(200, 200);
        process();
        frame.pack();
    }

    public void reDesign(int rows, int cols) {
        removeAll();
        revalidate();
        setBackground(Color.BLACK);
        setLayout(new GridLayout(rows, cols));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    public <T extends Component> void paint(T t) {
        t.setBackground(Color.BLACK);
        t.setForeground(new Color(20, 179, 78));
        t.setFont(new Font("Serif", Font.BOLD, 20));
    }

    public String trimPath(String s) {
        return s.substring(0, s.length() / 4)
                + "..."
                + s.substring(s.lastIndexOf("\\"));
    }

    public String choosePath() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().getAbsolutePath();
        } else {
            return "NOT CHOSEN";
        }
    }

    //String path = "src/Implementation/Resources/file.txt";
    String path = "src/Implementation/Resources/iris_training.txt";
    //String path = "src/Implementation/Resources/iris_test.txt";
    //String tPath = "src/Implementation_02/iris_test.txt";

    public void process() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
        setLayout(new GridLayout(8, 1));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        Label trLabel = new Label(path);
        paint(trLabel);
        trLabel.setBackground(Color.GREEN);
        trLabel.setForeground(Color.BLACK);

        JButton trSet = new JButton("Choose data");
        trSet.addActionListener(e -> {
            String s = choosePath();
            trLabel.setForeground(Color.BLACK);
            if (s.startsWith("NOT CHOSEN")) {
                trLabel.setBackground(Color.red);
                trLabel.setText(s);
            } else {
                trLabel.setBackground(Color.green);
                trLabel.setText(trimPath(s));
                path = s;
            }

        });
        paint(trSet);
        Label tLabel = new Label("Input k value");
        paint(tLabel);
        JTextField field = new JTextField(30);
        paint(field);
        field.setFont(new Font("Serif", Font.BOLD, 30));
        JButton stats = new JButton("Calculate stats");
        stats.addActionListener(e -> {
            if (trLabel.getBackground() == Color.GREEN && field.getText().matches("[0-9]+")) {
                calc(Integer.parseInt(field.getText()), path);
            }
        });
        paint(stats);

        JButton button = new JButton("EXIT");
        button.addActionListener(e -> System.exit(0));
        paint(button);

        add(trLabel);
        add(trSet);
        add(tLabel);
        add(field);
        add(stats);
        add(button);
    }

    public void calc(int k, String set) {

        KClustering clustering = new KClustering(k, set);
        removeAll();
        revalidate();
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JButton button;

        JTextArea textArea = new JTextArea("");
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 60, 780, 500);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane,BorderLayout.CENTER);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        print(clustering.getCases(false),textArea);

        button = new JButton("One step");
        paint(button);
        button.addActionListener(e->{
            if(clustering.oneStep())
            print(clustering.getCases(false),textArea);
            else print(clustering.getCases(true),textArea);
        });
        add(button,BorderLayout.EAST);

        button = new JButton("Chose another dataset");
        paint(button);
        button.addActionListener(e -> {
            reDesign(8, 1);
            process();
        });
        add(button,BorderLayout.SOUTH);

        button = new JButton("Exit");
        paint(button);
        button.addActionListener(e -> System.exit(0));
        add(button,BorderLayout.WEST);
    }

    private void print(String s,JTextArea area){
        area.setText(s);
    }

}

