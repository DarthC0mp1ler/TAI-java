package Implementation_01.kNN;

import java.io.*;
import java.util.*;

class Case{

    String _result;
    List<Double> _attributes;
    double _distance;

    public Case(String result,List<Double> attributes){
        _result = result;
        _attributes =attributes;
    }

    public void setDistance(double d){
        _distance = d;
    }
}

public class KNN  implements Comparator<Case>{

    private final int _K;
    private File _file;
    private List<Case> _cases;
    private int _caseNumber;


    public KNN(File file,int k) {
        _K = k;
        _file = file;
        _cases = new ArrayList<>();
        read();
    }

    private int read() {
        int classified = 0;
        int cases;
        try {
            BufferedReader in = new BufferedReader(new FileReader(_file));

            String line;
            loop:while ((line = in.readLine()) != null) {
                String tmp[] = line.trim().replaceAll("\\s+", " ").replaceAll(",", ".").split(" ");
                List<Double> list = new ArrayList<>();
                for (int i = 0; i < tmp.length - 1; i++) {

                    try {
                        list.add(Double.parseDouble(tmp[i]));
                    } catch (NumberFormatException e) {
                        continue loop;
                    }
                }
                cases = tmp.length - 1;
                _caseNumber = cases;
                classified ++;
                _cases.add(new Case(tmp[tmp.length-1],list));

            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classified;
    }

    @Override
    public int compare(Case aCase, Case t1) {
        if(aCase._distance > t1._distance) return 1;
        if(aCase._distance < t1._distance) return -1;
        return 0;
    }

    private double squaredDistance(List<Double> list, double[] arr) {
        double calc = 0;
        for (int i = 0; i < arr.length; i++) {
            calc += Math.pow((arr[i] - list.get(i)), 2);
        }
        return calc;
    }

    public String processValue(double[] attributes) {
        if (attributes.length != _caseNumber)
            return "Inconsistent number of attributes";
        for (Case c: _cases) {
            c.setDistance(Math.sqrt(squaredDistance(c._attributes, attributes)));
        }

        _cases.sort(this);

        for (int i = 0; i < _K; i++) {
            System.out.println(_cases.get(i)._distance);
        }

        HashMap<String,Integer> map = new HashMap<>();
        for (int i = 0; i < _K; i++) {
            if(map.containsKey(_cases.get(i)._result)){
                map.put(_cases.get(i)._result,map.get(_cases.get(i)._result)+1);
            }else{
                map.put(_cases.get(i)._result,0);
            }
        }
        String result = "";
        int count = -1;
        for (String s: map.keySet()) {
            if(count < map.get(s)){
                result = s;
                count = map.get(s);
            }
        }
        return result;
    }

    public int get_caseNumber() {
        return _caseNumber;
    }

    public int get_K() {
        return _K;
    }
}