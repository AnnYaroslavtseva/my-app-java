package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FibonacciStore {
    private static FibonacciStore instance;


    public static FibonacciStore getInstance() {
        if (instance == null) {
            instance = new FibonacciStore();
        }
        return instance;
    }


    private Map<Integer, Fibonacci> fsequence = new HashMap<>();

        public Fibonacci addNote(Fibonacci fseq) {
            int id = fsequence.size();
             fsequence.put(id, fseq);
            return fseq;
        }

        public Fibonacci getNote(int id) {
            return fsequence.get(id);
        }

        public Set<Fibonacci> getAllNotes() {
            return new HashSet<>(fsequence.values());
        }

        public Fibonacci updateNote(Fibonacci fseq) {
            int parameterN = fseq.getParameterN();
            if (fsequence.containsKey(parameterN)) {
                fsequence.put(parameterN, fseq);
                return fseq;
            }
            return null;
        }

        public boolean deleteNote(int id) {
            return fsequence.remove(id) != null;
        }
    }

