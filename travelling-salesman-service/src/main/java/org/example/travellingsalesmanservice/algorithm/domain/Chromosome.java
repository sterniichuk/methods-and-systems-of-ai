package org.example.travellingsalesmanservice.algorithm.domain;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Chromosome(int[] x, int[] y) {
    public static Chromosome ofLength(int length) {
        return new Chromosome(new int[length], new int[length]);
    }

    public void fillWith(int start, Chromosome source, int sourceStart, int length) {
        System.arraycopy(source.x, sourceStart, x, start, length);
        System.arraycopy(source.y, sourceStart, y, start, length);
    }

    public Point getPoint(int i) {
        return new Point(x[i], y[i]);
    }

    public int size() {
        return x.length;
    }

    public boolean equals(int i, Chromosome p) {
        return p.x[i] == x[i] && p.y[i] == y[i];
    }

    public boolean equalsPoints(int i, int j) {
        return x[j] == x[i] && y[j] == y[i];
    }

    public int indexOf(int xP, int yP) {
        return IntStream.range(0, x.length)
                .filter(i -> x[i] == xP && y[i] == yP)
                .findAny().orElse(-1);
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "x=" + Arrays.toString(x) +
                ", y=" + Arrays.toString(y) +
                '}';
    }

    @SuppressWarnings("preview")
    public String toString(int N, int chromosomeLength) {
        assert N <= x.length;
        return STR. """
                Chromosome [\{ N } elements] {
                x={\{ collectWholeChromosome(x, N, chromosomeLength) }}
                y={\{ collectWholeChromosome(y, N, chromosomeLength) }}
                }
                """ ;
    }

    private String collectWholeChromosome(int[] array, int N, int chromosomeLength) {
        return IntStream.range(0, N / chromosomeLength)
                .mapToObj(i -> toStringChromosome(array, chromosomeLength * i, chromosomeLength))
                .collect(Collectors.joining(","));
    }

    private String toStringChromosome(int[] array, int start, int length) {
        String collected = IntStream.range(start, start + length)
                .mapToObj(i -> array[i] + "")
                .collect(Collectors.joining(","));
        return STR. "[\{ collected }]" ;
    }

    public void setFrom(int i, Chromosome parent) {
        x[i] = parent.x[i];
        y[i] = parent.y[i];
    }

    public void swapByIndex(int i, int j) {
        swapByIndex(i, j, x);
        swapByIndex(i, j, y);
    }

    private static void swapByIndex(int i, int j, int[] array) {
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public boolean equalsSubChromosomes(int i, int j, int chromosomeLength) {
        for (int k = 1; k < chromosomeLength; k++) {
            if (!this.equalsPoints(k + i, k + j)) {
                return false;
            }
        }
        return true;
    }

    public void setPoint(int i, Point p2) {
        x[i] = p2.x();
        y[i] = p2.y();
    }

    @SuppressWarnings("unused")
    public void fillWith(int i) {
        Arrays.fill(x, i);
        Arrays.fill(y, i);
    }

    public void insertList(Iterable<Chromosome> bestList) {
        int index = 0;
        for (Chromosome c : bestList) {
            for (int j = 0; j < c.size(); j++) {
                this.setFrom(index, j, c);
                index++;
            }
        }
    }

    private void setFrom(int target, int source, Chromosome parent) {
        x[target] = parent.x[source];
        y[target] = parent.y[source];
    }
}
