package com.example.cst.model;

import lombok.Data;

@Data
public class Rut {

    private Integer mantissa;
    private Character dv;

    /**
     * Creates a instance with the given mantissa and dv (no validation performed).
     *
     * @param mantissa the mantissa.
     * @param dv the dv.
     */
    public Rut(Integer mantissa, Character dv) {
        if (mantissa == null) {
            throw new IllegalArgumentException("Must specify mantissa");
        }

        if (dv == null) {
            throw new IllegalArgumentException("Must specify dv");
        }

        if (!"0123456789kK".contains(dv.toString())) {
            throw new IllegalArgumentException("Invalid dv");
        }

        this.mantissa = mantissa;
        this.dv = dv.toString().toUpperCase().charAt(0);
    }

    /**
     * Creates a instance with the given mantissa, the dv is calculated.
     *
     * @param mantissa the mantissa.
     */
    public Rut(Integer mantissa) {
        this.mantissa = mantissa;

        Integer dig = dvCalc(this.mantissa);

        if (dig.equals(10)) {
            this.dv = 'K';
        } else {
            this.dv = dig.toString().charAt(0);
        }
    }

    /**
     * Checks is verifier digit corresponds to the provided mantissa.
     *
     * @param mantissa the mantissa to check.
     * @param dv the dv to check.
     * @return true if dv corresponds.
     */
    public static boolean isValid(Integer mantissa, Character dv) {
        if (mantissa == null || dv == null) return false;

        Integer calculated = dvCalc(mantissa);

        if (calculated.equals(10) && (dv.equals('k') || dv.equals('K'))) return true;

        return dv.equals(calculated.toString().charAt(0));
    }

    /**
     * Tries to recover a valid rut from a string.
     *
     * @param rut the string to search in.
     * @return the parsed rut, null if unable to parse.
     */
    public static Rut castRut(String rut) {

        if (rut == null) return null;

        if (!rut.matches("[0-9.]+-?[0-9kK]")) {
            return null;
        }

        String clean = rut.replace("-", "").replace(".", "").toUpperCase();

        Integer mantissa = Integer.parseInt(clean.substring(0, clean.length() -1));
        Character dv = clean.substring(clean.length() -1).charAt(0);

        if (!isValid(mantissa, dv)) {
            return null;
        }

        return new Rut(mantissa, dv);
    }

    /**
     * Calculates the verifier for a given mantissa using 'modulo 11' algorithm.
     *
     * @param mantissa the mantissa.
     * @return the verifier digit, 10 if 'K'.
     */
    public static Integer dvCalc(Integer mantissa) {
        Integer rem = mantissa;

        int sum = 0;
        int curMultiplier = 2;

        while (rem > 0) {
            sum += (rem % 10) * curMultiplier++;
            rem /= 10;

            if (curMultiplier == 8) curMultiplier = 2;
        }

        return (11 - (sum % 11)) % 11;
    }

    @Override
    public String toString() {
        return String.format("%1$d-%2$s", mantissa, dv);
    }
}