package Implementation_01;

import Implementation_01.kNN.KNN;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;


public class Main extends JPanel {

    private File dataset = null;
    private String res = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {

        JFrame frame = new JFrame("KNN algorithm");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(this);
        frame.setLocation(200, 200);
        run();
        frame.pack();
    }

    public <T extends Component> void setStyle(T t){
        t.setForeground(new Color(20, 179, 78));
        t.setBackground(Color.BLACK);
    }

    public void run() {

        setPreferredSize(new Dimension(500, 500));

        setBackground(Color.BLACK);
        reSet(4,1);

        JLabel label1 = new JLabel("KNN algorithm application");
        setStyle(label1);
        JLabel label2 = new JLabel("Do you want ot start?");
        setStyle(label2);

        add(label1);
        add(label2);

        JButton yesButton = new JButton("YES");
        setStyle(yesButton);
        yesButton.addActionListener(e -> operate());
        add(yesButton);

        JButton noButton = new JButton("NO");
        noButton.addActionListener(e -> operate());
        setStyle(noButton);
        add(noButton);
    }

    public File chooseFile() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        } else {
            return chooseFile();
        }
    }

    public void reSet(int rows, int cols) {
        removeAll();
        revalidate();
        repaint();
        setLayout(new GridLayout(rows, cols));
        setBorder(BorderFactory.createEmptyBorder(100, 50, 200, 50));
    }

    public void operate() {
        reSet(5, 1);
        setBorder(BorderFactory.createEmptyBorder(100, 50, 200, 50));
        JLabel label = new JLabel("Choose training data file");
        setStyle(label);
        add(label);

        JButton button2 = new JButton("Submit");
        button2.setEnabled(false);

        JButton button = new JButton("ChooseFile");
        button.addActionListener(e -> {
            dataset = chooseFile();
            button.setBackground(new Color(20, 179, 78));
            button.setForeground(Color.BLACK);
            button2.setEnabled(true);
            button.setEnabled(false);
            button.setText("Chosen");
        });
        setStyle(button);
        add(button);

        JLabel label2 = new JLabel("Endter k value. ONLY NUMBER!!!");
        setStyle(label2);
        add(label2);

        JTextField field = new JTextField(10);
        add(field);

        setStyle(button2);
        button2.addActionListener(e ->
        {
            if(field.getText() == null || field.getText().equals("") || !field.getText().matches("[0-9]+")) return;
            res = field.getText().trim();
            secondPart();
        });
        add(button2);

    }

    public void secondPart() {
        reSet(5, 1);
        KNN knn = new KNN(dataset, Integer.parseInt(res));

        JLabel label = new JLabel("Choose test data file");
        setStyle(label);
        add(label);

        JButton nextButton = new JButton("Continue");

        JButton button = new JButton("ChooseFile ");

        setStyle(button);
        JTextField field = new JTextField(50);
        button.addActionListener(e -> {
            dataset = chooseFile();
            button.setBackground(new Color(20, 179, 78));
            nextButton.setForeground(Color.BLACK);
            button.setEnabled(false);
        });
        add(button);
        add(field);


        setStyle(nextButton);
        nextButton.addActionListener(e -> {
            if(field.getText().equals(""))
            read(knn);
            else{
                readOne(knn,field.getText());
            }
        });
        add(nextButton);
    }


    public void readOne(KNN knn, String line) {
            String tmp[] = line.replaceAll("\\s+", " ")
                    .replaceAll(",", ".")
                    .trim()
                    .split(" ");

            if (tmp.length < knn.get_caseNumber() || tmp[0].equals(" ")) {
                throw new IllegalArgumentException();
            }

            double[] arr = new double[knn.get_caseNumber()];
            for (int i = 0; i < tmp.length - 1; i++) {

                arr[i] = Double.parseDouble(tmp[i]);
            }
            reSet(7, 1);

            JLabel label = new JLabel("Results:\nParameter k: " + knn.get_K() + " : " + knn.processValue(arr));


            setStyle(label);
            add(label);

            JButton button = new JButton("Exit");
            button.addActionListener(e -> {
                System.exit(0);
            });
            setStyle(button);
            add(button);

            JButton cont = new JButton("Continue");
            cont.addActionListener(e -> {
                operate();
            });
            setStyle(cont);
            add(cont);

    }

    public void read(KNN knn) {
        try {
            String line = null;
            int successful = 0, notSuccessful = 0;

            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataset));
            while ((line = bufferedReader.readLine()) != null) {

                try {
                    String tmp[] = line.replaceAll("\\s+", " ")
                            .replaceAll(",", ".")
                            .trim()
                            .split(" ");

                    if (tmp.length < knn.get_caseNumber() || tmp[0].equals(" ")) {
                        throw new IllegalArgumentException();
                    }

                    double[] arr = new double[knn.get_caseNumber()];
                    for (int i = 0; i < tmp.length - 1; i++) {

                        arr[i] = Double.parseDouble(tmp[i]);
                    }

                    if (knn.processValue(arr).equalsIgnoreCase(tmp[tmp.length - 1])) {
                        successful++;
                    } else notSuccessful++;

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "ERROR!\nChoose another file for test!",
                            "info", JOptionPane.ERROR_MESSAGE,
                            null);
                    System.exit(0);
                }
            }
            reSet(7,1);
            JLabel label = new JLabel("Results:\nParameter k: " + knn.get_K());
            setStyle(label);
            add(label);
            label = new JLabel("Correctly classified samples:"  + successful);
            setStyle(label);
            add(label);
            label = new JLabel("\nPrecision of classification: " + (successful * 100 / (successful + notSuccessful)) + "%");
            setStyle(label);
            add(label);

            JButton button = new JButton("Exit");
            button.addActionListener(e -> {
                System.exit(0);
            });
            setStyle(button);
            add(button);

            JButton cont = new JButton("Continue");
            cont.addActionListener(e -> {
                operate();
            });
            setStyle(cont);
            add(cont);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

