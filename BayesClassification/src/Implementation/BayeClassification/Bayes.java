package Implementation.BayeClassification;

import java.util.*;

public class Bayes {
    private HashMap<String, List<String[]>> _trainingSet;
    private int _trainingSetSize;

    public Bayes(List<String[]> trainingSet) {
        _trainingSet = new HashMap<>();
        for (String[] s : trainingSet) {
            String key = s[s.length - 1];
            s = Arrays.copyOf(s, s.length - 1);
            if (!_trainingSet.containsKey(key)) {
                _trainingSet.put(key, new ArrayList<>());
            }
            _trainingSet.get(key).add(s);
        }
        _trainingSetSize = trainingSet.size();
        print();
    }

    private void print() {
        for (String key : _trainingSet.keySet()) {
            System.out.println(">>>" + key + ": ");
            for (String[] list : _trainingSet.get(key)) {
                System.out.print("\t");
                for (int i = 0; i < list.length; i++) {
                    System.out.print(list[i] + " ");
                }
                System.out.println();
            }
        }
    }

    private int count(String key, int col, String value) {
        int res = 0;
        for (String[] values : _trainingSet.get(key)) {
            if (values[col].equals(value)) res++;
        }
        return res;
    }

    private int getDistinct(List<String[]> list, int col) {
        HashSet<String> hs = new HashSet<>();

        for (int i = 0; i < list.size(); i++) {
            hs.add(list.get(i)[col]);
        }
        return hs.size();
    }

    private boolean checkForSmoothing(String[] arr, String key) {

        for (int i = 0; i < arr.length; i++) {
            if (count(key, i, arr[i]) == 0) return true;
        }

        return false;
    }


    public String getConfusionMatrix(List<String[]> data) {
        String result = "";
        HashMap<String, HashMap<String, Integer>> map = new HashMap<>();
        for (String s : _trainingSet.keySet()) {
            map.put(s, new HashMap<>());
            for (String s1 : _trainingSet.keySet()) {
                map.get(s).put(s1, 0);
            }
        }
        for (int i = 0; i < data.size(); i++) {
            String s = getResult(Arrays.copyOf(data.get(i), data.get(i).length - 1));
            map.get(data.get(i)[data.get(i).length - 1]).put(s, map.get(data.get(i)[data.get(i).length - 1]).get(s) + 1);
        }

        double totalPrec = 0;
        double totalRec = 0;
        double accuracy = 0;
        for (String s : map.keySet()) {
            result += s + "={\n";
            for (String s1 : map.get(s).keySet()) {
                result += "\n\t" + s1 + "=" + map.get(s).get(s1);
            }
            result += "\n};\n";
            totalPrec += map.get(s).get(s)/(double)data.size() * (map.get(s).get(s) / sum(map.get(s)));
            totalRec += map.get(s).get(s)/(double)data.size() * (map.get(s).get(s)/actualSum(map,s));
            accuracy += map.get(s).get(s);
        }
        accuracy /= (double)data.size();
        result +="Accuracy="+accuracy+ "\n";
        result +="Precision="+totalPrec + "\n";
        result +="Recall="+totalRec + "\n";
        result +="F-measure="+(2./((1./totalPrec) + (1./totalRec)))+ "\n";
        return result;
    }

    public double actualSum(HashMap<String, HashMap<String, Integer>> map,String key) {
        double res = 0;
        for (String s: map.keySet()) {
            res += map.get(s).get(key);
        }
        return res;
    }

    public double sum(HashMap<String, Integer> map) {
        double res = 0;
        for (String s :
                map.keySet()) {
            res += map.get(s);
        }
        return res;
    }

    public String getResult(String[] arr) {
        double[] probabilities = new double[_trainingSet.keySet().size()];
        int index = 0, outputQuantity;
        for (String key : _trainingSet.keySet()) {

            //System.out.println(key + ":");
            outputQuantity = _trainingSet.get(key).size();
            double prob;
            if (!checkForSmoothing(arr, key)) {
                prob = (double) outputQuantity / (double) _trainingSetSize;
                for (int i = 0; i < arr.length; i++) {
                    //System.out.println(arr[i] + " " + count(key, i, arr[i]));
                    prob *= (double) count(key, i, arr[i]) / (double) outputQuantity;
                }

            } else {
                prob = (double) (outputQuantity + 1) / (double) (_trainingSetSize + _trainingSet.keySet().size());
                for (int i = 0; i < arr.length; i++) {
                    //System.out.println(arr[i] + " " + count(key, i, arr[i]));
                    prob *= (double) (count(key, i, arr[i]) + 1) / (double) (outputQuantity + getDistinct(_trainingSet.get(key), i));
                }
            }
            //System.out.println(prob);
            probabilities[index++] = prob;
        }

        index = 0;
        double maxProb = probabilities[0];
        for (int i = 1; i < probabilities.length; i++) {
            if (maxProb < probabilities[i]) {
                maxProb = probabilities[i];
                index = i;
            }
        }
        String s = (String) _trainingSet.keySet().toArray()[index];
        return s;
    }

}
