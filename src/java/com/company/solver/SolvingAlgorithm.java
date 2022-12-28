package com.company.solver;

public enum SolvingAlgorithm {

    LEE("Волновой");

    private final String alg;

    SolvingAlgorithm(String alg) {
        this.alg = alg;
    }

    @Override
    public String toString() {
        return alg;
    }
}
