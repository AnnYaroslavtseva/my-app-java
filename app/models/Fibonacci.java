package models;

public class Fibonacci {


        private int parameterN;
       // private int[] fibonacciSequence;
       private StringBuffer sequence;

        private StringBuffer computation  (int parameterN) {
            int a = 1;
            int b = 1;
            int n;
            int sum_fib;
            n = parameterN;
            if (n == 1 | n ==2)return new StringBuffer(1);
            StringBuffer sequence = new StringBuffer(a+" "+b+" ");
            for (int i = 0; i < n; i++) {
                sum_fib = a + b;
                a = b;
                b = sum_fib;

                sequence.append(sum_fib + " ");
            }
            return sequence;


        }

    public Fibonacci(int parameterN) {
        this.parameterN = parameterN;
        this.sequence = computation(parameterN);
    }

    public int getParameterN() {
        return parameterN;
    }

    public void setParameterN(int parameterN) {
        this.parameterN = parameterN;
    }

/*
    public int[] getFibonacciSequence() {
        return fibonacciSequence;
    }
*/

    /*public void setFibonacciSequence(int[] fibonacciSequence) {
        this.fibonacciSequence = fibonacciSequence;
    }*/

    public StringBuffer getSequence() {
        return sequence;
    }

    public void setSequence(StringBuffer sequence) {
        this.sequence = sequence;
    }
}
