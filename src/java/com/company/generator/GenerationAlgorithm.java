package com.company.generator;

public enum GenerationAlgorithm {

    PRIM("Алгоритм Прима");

    private final String alg;

    GenerationAlgorithm(String alg) {
        this.alg = alg;
    }

    @Override
    public String toString() {
        return alg;
    }
}
