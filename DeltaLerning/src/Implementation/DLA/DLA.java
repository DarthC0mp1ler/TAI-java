package Implementation.DLA;

import java.util.List;
import java.util.Random;

class Perceptron{

    Random random = new Random();
    double _weights[];
    public Perceptron(int toRand,int length){
        double weights[] = new double[length];
        for (int i = 0; i < length; i++) {
            weights[i] = random.nextInt(toRand + toRand);
        }
        //weights[length-1] = toRand*toRand;
        _weights = weights;
    }

    public int getOutput(double [] arr){
        double s = 0;
        for (int i = 0; i < _weights.length-1; i++) {
            s+=_weights[i]*arr[i];
        }
        s-=_weights[_weights.length-1]*1;
        if(s >= 0){
            return 1;
        }
        else return 0;
    }

    public int getWeightsCount(){
        return _weights.length;
    }

    public void setWeight(double weight,int index){
        _weights[index] = weight;
    }

    public double getWeight(int index){
        return _weights[index];
    }
}

public class DLA {

    private Perceptron _perceptron;
    private List<double[]> _trainingSet;

    public DLA(List<double[]> trainingSet){
        _trainingSet = trainingSet;
        int i = (int)trainingSet.get(0)[0];
        if(i == 0)i++;
        if(i < 0) i = Math.abs(i)+1;
        _perceptron = new Perceptron(i,trainingSet.get(0).length-1);
        adjustWeights();
    }

    private void adjustWeights(){
        boolean end = true;
        while(end){
            end = false;
            for (double[] arr:_trainingSet) {
                int y = _perceptron.getOutput(arr);
                while( y != arr[arr.length-1]){
                    end = true;
                    for (int i = 0; i < arr.length-2; i++) {
                        double w = _perceptron.getWeight(i) + 0.1*(arr[arr.length-1] - y) * arr[i];
                        _perceptron.setWeight(w,i);
                    }
                    y = _perceptron.getOutput(arr);
                }
            }
        }
        for (int i = 0; i < _perceptron._weights.length; i++) {
            System.out.println(_perceptron._weights[i]);
        }
    }

    public int getResult(double[] arr){

        return _perceptron.getOutput(arr);
    }

}
