package Implementation.kClustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.*;

public class KClustering {

    class Case{
        private double[] _attributes;
        private String _decision;
        Case(double[] arr, String s){
            _attributes = arr;
            _decision = s;
        }

        public double getAttribute(int index){
            return _attributes[index];
        }

        public double distance(double arr[]){
            double dist = 0;
            for (int i = 0; i < _attributes.length; i++) {
                dist+=pow(_attributes[i]-arr[i],2.);
            }
            return sqrt(dist);
        }

        public String getDecision(){
            return _decision;
        }

        public int length(){
            return _attributes.length;
        }

        public String toString(){
            return _decision + " : " + Arrays.toString(_attributes);
        }
    }

    private List<List<Case>> _cases;
    private double[][] _centers;
    private int _k;
    private double[] _entropy;


    public KClustering(int k, String path){
        _k = k;
        List<Case> list = read(path);
        _centers = new double[k][list.get(0).length()];
        initialize(list);
    }

    public String getCases(boolean entr){
        String res = "";
        for (int i = 0; i < _cases.size(); i++) {
            res += "<Group_" + (i+1) + ">";
            for (int j = 0; j < _cases.get(i).size(); j++) {
                res+="\n\t"+_cases.get(i).get(j);
            }
            res+="\n</Group_ " + (i+1) +">\n";
        }
        if(entr){
            res+="<Entropy>";
            for (int i = 0; i < _entropy.length; i++) {
                res+="\n\t" + _entropy[i];
            }
            res+="\n</Entropy>";
        }
        System.out.println("=======================================");
        return res;
    }

    private void initialize(List<Case> list){
        _cases = new ArrayList<>();
        for (int i = 0; i < _k; i++) {
            _cases.add(new ArrayList<>());
        }
        for (Case aCase : list) {
            _cases.get((int)(random()*_k)).add(aCase);
        }
        for (int i = 0; i < _cases.size(); i++) {
            if(_cases.get(i).size() == 0){
                if(i == 0){
                    _cases.get(i).add(_cases.get(i+1).get(_cases.get(i+1).size()-1));
                    _cases.get(i+1).remove(_cases.get(i+1).size()-1);
                }else{
                    _cases.get(i).add(_cases.get(i-1).get(_cases.get(i-1).size()-1));
                    _cases.get(i-1).remove(_cases.get(i-1).size()-1);
                }
            }
        }
    }


    public boolean oneStep(){
        refactorCenters();
        if(refactorGroups()){
            return true;
        }
        calcEntropy();
        return false;
    }


    private double log(double x, int base)
    {
        return (Math.log10(x) / Math.log10(base));
    }
    private void calcEntropy(){
        _entropy = new double[_k];
        for (int i = 0; i < _entropy.length; i++) {
            HashMap<String,Integer> map = new HashMap<>();
            for (Case c : _cases.get(i)) {
                if (map.containsKey(c.getDecision())) {
                    map.put(c.getDecision(), map.get(c.getDecision()) + 1);
                }
                else {
                    map.put(c.getDecision(), 1);
                }
            }
            double[] prob = new double[map.keySet().size()];
            int ind = 0;
            for (String s:map.keySet()) {
                prob[ind] = (double)map.get(s)/(double)_cases.get(i).size();
                ind++;
            }
            double entropy = 0;
            for (int j = 0; j < prob.length; j++) {
                entropy += (prob[j] * log(prob[j],2));
            }
            entropy *= -1;
            _entropy[i] = entropy;

        }
    }

    private boolean refactorGroups(){
        boolean res = false;
        for (List<Case> list: _cases) {
            for (int i = 0; i < list.size(); i++) {
                int ind = minDist(list.get(i));
                Case c = list.get(i);
                if(!_cases.get(ind).contains(c)) {
                    list.remove(c);
                    _cases.get(ind).add(c);
                    res = true;
                }
            }
        }
        return res;
    }

    private void refactorCenters(){
        for (int i = 0; i < _centers.length; i++) {
            for (int j = 0; j < _centers[i].length; j++) {
                double sum = 0;
                for (int k = 0; k < _cases.get(i).size(); k++) {
                    sum += _cases.get(i).get(k).getAttribute(j);
                }
                sum = sum/_cases.get(i).size();
                _centers[i][j] = sum;

            }
        }
    }

    private List<Case> read(String path){
        List<Case> list = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            String line;
            while ((line = in.readLine()) != null) {
                Case c = processRow(line);
                list.add(c);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Case processRow(String line){
        String tmp[] = line.trim().replaceAll("\\s+", " ").replaceAll(",", ".").split(" ");
        double[] arr = new double[tmp.length-1];
        for (int i = 0; i < tmp.length - 1; i++) {
            try {
                arr[i] = Double.parseDouble(tmp[i]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        Case c = new Case(arr,tmp[tmp.length-1]);
        return c;
    }

    private int minDist(Case c){
        double minD = c.distance(_centers[0]);
        int ind = 0;
        for (int j = 1; j < _centers.length; j++) {
            if (minD > c.distance(_centers[j])) {
                minD = c.distance(_centers[j]);
                ind = j;
            }
        }
        System.out.println(c + " " + c.distance(_centers[ind]));
        return ind;
    }

    private double max(double[] arr){
        double res = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if(res < arr[i]) res = arr[i];
        }
        return res;
    }

}
