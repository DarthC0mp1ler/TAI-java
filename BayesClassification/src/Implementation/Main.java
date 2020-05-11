package Implementation;

import Implementation.BayeClassification.Bayes;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JPanel {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main(){
        JFrame frame = new JFrame("Perceptron");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.setLocation(200,200);
        process();
        frame.pack();
    }

    public void reDesign(int rows,int cols){
        removeAll();
        revalidate();
        setBackground(Color.BLACK);
        setLayout(new GridLayout(rows,cols));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    public <T extends Component> void paint(T t){
        t.setBackground(Color.BLACK);
        t.setForeground(new Color(20, 179, 78));
        t.setFont(new Font("DIALOG",Font.PLAIN,20));
    }

    public String trimPath(String s){
        return s.substring(0,s.length()/4) + "..." + s.substring(s.lastIndexOf("\\"));
    }

    public String choosePath(){
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().getAbsolutePath();
        } else {
            return "NOT CHOSEN";
        }
    }

    String trPath = "src/Resources/iris_training.txt";
    String tPath = "src/Resources/iris_test.txt";

    public void process(){
        setPreferredSize(new Dimension(600,700));
        setBackground(Color.BLACK);
        setLayout(new GridLayout(8,1));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));




        //training set
        Label trLabel= new Label(trPath);
        paint(trLabel);
        trLabel.setBackground(Color.GREEN);
        JButton trSet = new JButton("Choose training data");

        trSet.addActionListener(e->{
            String s = choosePath();
            trLabel.setForeground(Color.BLACK);
            if(s.startsWith("NOT CHOSEN")){ trLabel.setBackground(Color.red);
                trLabel.setText(s);
            }
            else{trLabel.setBackground(Color.green); trLabel.setText(trimPath(s));trPath = s;}

        });


        paint(trSet);
        //test set
        Label tLabel = new Label(tPath);
        paint(tLabel);
        tLabel.setBackground(Color.GREEN);
        JButton tSet = new JButton("Choose test data");
        tSet.addActionListener(e-> {
            tLabel.setForeground(Color.BLACK);
            String s = choosePath();
            if(s.startsWith("NOT CHOSEN")){
                tLabel.setText(s);tLabel.setBackground(Color.red);
            }
            else{tLabel.setBackground(Color.green); tLabel.setText(trimPath(s));tPath = s;}

        });
        paint(tSet);
        //calc data
        JButton stats = new JButton("Calculate stats");
        stats.addActionListener(e->{
            if(trLabel.getBackground() == Color.GREEN && tLabel.getBackground() == Color.GREEN){
                stats(trPath,tPath);
            }
        });
        paint(stats);
        //input single
        JButton input = new JButton("Input single(only training required)");
        input.addActionListener(e->{
            if(trLabel.getBackground() == Color.GREEN ){
                inputSingle(trPath);
            }
        });
        paint(input);

        JButton button = new JButton("EXIT");
        button.addActionListener(e->System.exit(0));
        paint(button);

        add(trLabel);
        add(trSet);
        add(tLabel);
        add(tSet);
        add(stats);
        add(input);
        add(button);
    }

    public void stats(String trainingSet,String testSet){
        List<String[]> train = read(trainingSet);
        List<String[]> test = read(testSet);

        Bayes bayes = new Bayes(train);

        removeAll();
        revalidate();
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setText(bayes.getConfusionMatrix(test));
        textArea.setFont(new Font("serif",Font.PLAIN,30));
        add(textArea,BorderLayout.CENTER);

        JButton button = new JButton("Chose another dataset");
        paint(button);
        button.addActionListener(e->process());
        add(button,BorderLayout.SOUTH);

        button = new JButton("Single input");
        paint(button);
        button.addActionListener(e->inputSingle(trainingSet));
        add(button,BorderLayout.EAST);

        button= new JButton("Exit");
        paint(button);
        button.addActionListener(e->System.exit(0));
        add(button,BorderLayout.WEST);
    }

    public void inputSingle(String trainingSet){

        Bayes bayes = new Bayes(read(trainingSet));
        reDesign(5,1);

        Label answerLabel = new Label("Answer will be here");
        paint(answerLabel);
        add(answerLabel);

        JTextField textField = new JTextField();
        add(textField);

        JButton button = new JButton("Calculate");
        paint(button);
        button.addActionListener(e->{
            if(!textField.getText().equals("")){
                String arr[] = convertRow(textField.getText());
                if(arr == null)
                    answerLabel.setText("bad input");
                else{

                        answerLabel.setText(bayes.getResult(arr));
                }
            }
        });
        add(button);


        button = new JButton("Chose another dataset");
        paint(button);
        button.addActionListener(e->{reDesign(8,1); removeAll(); revalidate();process();});
        add(button);

        button= new JButton("Exit");
        paint(button);
        button.addActionListener(e->System.exit(0));
        add(button);
    }

    public String[] convertRow(String line){
        String tmp[] = line.trim().replaceAll("\\s+", " ").replaceAll(",", ".").split(" ");
        return tmp;
    }

    public List<String[]>read(String path){
        List<String[]> list = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            String line;
            while ((line = in.readLine()) != null) {
                String[] arr = convertRow(line);
                if(arr != null)
                list.add(arr);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
