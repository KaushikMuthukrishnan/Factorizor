package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    private static ArrayList<Float> rootFinder(int deg, int[] coEff) {

        boolean zeroIsRoot = false;
        var rootsFinal = new ArrayList<Float>();
        int p = coEff[deg]; //last coefficient (constant)
        int q = coEff[0]; // leading coefficient

        int i = 0;
        while (p == 0) { //reassigns p to second to last coEff and adds 0 as a root;
            p = coEff[deg - ++i]; //prefix increment to ensure array element is inbounds
            zeroIsRoot = true;
        }


        var pFactors = factorFinder(p);
        var qFactors = factorFinder(q);

        var rootsToTest = possibleRoots(pFactors, qFactors);
        rootsToTest = removeDuplicates(rootsToTest);
        rootsFinal = syntheticDivider(coEff, rootsToTest); //returns roots that synthetic divide to zero (aka rational roots)
        Collections.sort(rootsFinal); //sorts roots
        if (zeroIsRoot)
            rootsFinal.add(0f);

        return rootsFinal;
    }

    private static ArrayList<Float> syntheticDivider(int[] coEffs, ArrayList<Float> possibleRoots) {

        var arr = new ArrayList<Float>();
        for (Float f : possibleRoots) {
            float remainder = 0;

            for (int i : coEffs) {
                i += remainder;
                remainder = i * f;
            }
            if (remainder == 0)
              arr.add(f);
        }
        return arr;
    }

    private static ArrayList<Float> possibleRoots(ArrayList<Integer> p, ArrayList<Integer> q) {

        var possibleRoots = new ArrayList<Float>();
        for (float x : p) {
            for (float y : q) {
                possibleRoots.add(x/y);
                possibleRoots.add(-x/y); //rational root theorem is +- factors of constant over factors of leading coefficient
            }
        }
        return possibleRoots;
    }

    private static ArrayList<Integer> factorFinder(int number) {

        var factors = new ArrayList<Integer>();
        for (int i = 1; i <= Math.abs(number); i++) { //ensures factors of negative numbers are counted as well
            if (number % i == 0)
                factors.add(i);
        }
        return factors;
    }

    private static ArrayList<Float> removeDuplicates(ArrayList<Float> arr) {

        for (int i = 0; i < arr.size(); i++) {
            float currFloat = arr.get(i);
            int lastInd = arr.lastIndexOf(currFloat);
            while (lastInd != i) {
                arr.remove(lastInd);
                lastInd = arr.lastIndexOf(currFloat); //reassignment needed in case of 2+ duplicates
            }
        }
        return arr;
    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.print("Enter the degree of the polynomial: ");
        int deg = s.nextInt();
        while (deg < 1) {
            System.out.println("Please enter a degree greater than 0. Remember, terms with negative powers are not polynomial");
            deg = s.nextInt();
        }

        System.out.print("Enter the coefficients in order of decreasing degree including zero coefficients\n");
        int[] coefficients = new int[deg + 1]; //total terms is one more than degree

        for (int i = 0; i < coefficients.length; i++) {
            System.out.printf("x^%d: ", deg - i);
            coefficients[i] = s.nextInt();
        }

        var roots = rootFinder(deg, coefficients);
        if (roots.size() > 0) {
            System.out.print("Rational roots of this polynomial: \n");
            for (float i : roots) {
                //removes comma on last root
                if (roots.indexOf(i) < roots.size() - 1)
                    System.out.printf("%.3f, ", i);
                else
                    System.out.printf("%.3f", i);
            }
        } else
            System.out.println("No rational roots exist for this polynomial");

    }
}
