/*
 *   Copyright 2022 Krzysztof W. Hoszowski
 *
 *   This file is part of CE Amplifier Calculator.
 *
 *   CE Amplifier Calculator is free software: you can redistribute it and/or
 *   modify it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CE Amplifier Calculator is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 *   Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with CE Amplifier Calculator. If not, see <https://www.gnu.org/licenses/>.
 */
package ce.amplifier.calculator;

/**
 * Performing calculations for DC analysis of a a specific CE amplifier circuit
 * (sketch included in the Maven project)
 *
 * @author krzysztofh
 * @version 1
 */
public class Analysis_DC {

    // Initializations
    private int VCEsat, Voutmax, VRE2;
    private double beta1, beta2, RB, VB, RII, RL, VCC;
    private double IBQ1, ICQ1, IEQ1, RC1, RE1, VBEQ1, VCEQ1, VRC1, VRE1;
    private double ICQ2, IEQ2, RC2, RE2, RE21, RE22, VBEQ2, VCEQ2, VRC2;

    public void Perform() {

        // Given data
        Voutmax = 5;
        RL = 3E+3;

        // Assumptions
        beta1 = beta2 = 458.7;
        VCEsat = 1;
        RC2 = 3E+3;
        RII = Electronics_Theorems.parallel(RC2, RL);

        // Q2
        VCEQ2 = Voutmax + VCEsat;
        ICQ2 = Voutmax / RII;

        // Establishing VCC
        VRC2 = ICQ2 * RC2;
        VRE2 = 2;  // Roughly between 10–20% of VCC
        VCC = VRE2 + VCEQ2 + VRC2;

        // Q1
        IEQ2 = ICQ2;  // Assumption
        VRE1 = .06;  // Assumption
        VBEQ1 = VBEQ2 = .7;  // Assumption
        RE2 = VRE2 / IEQ2;
        RE21 = 1 * RE2 / 3;  // Assumption
        RE22 = RE2 - RE21;
        ICQ1 = IEQ1 = ICQ2 / 10;
        VCEQ1 = VBEQ2 + VRE2 - VRE1;
        VRC1 = VCC - VCEQ1 - VRE1;
        RC1 = VRC1 / ICQ1;
        RE1 = VRE1 / IEQ1;

        System.out.println("Q1: (" + VCEQ1 + "V, " + ICQ1 + "A)");
        System.out.println("Q2: (" + VCEQ2 + "V, " + ICQ2 + "A)");

        // Q1 Base resistor
        VB = VBEQ1 + VRE1 - (VRE2 * RE21 / RE2);
        IBQ1 = ICQ1 / beta2;
        RB = VB / IBQ1;
        // System.out.println("Results: VCC = " + VCC + "V; RC2 = " + RC2 +
        //        "Ω; RE21 = " + RE21 + "Ω; RE22 = " + RE22 + "Ω; RB = " + RB +
        //s       "Ω; RC1 = " + RC1 + "Ω; " + "RE1 = " + RE1 + "Ω");

    }

    @Override
    public String toString() {
        return "Analysis_DC{"
                + "VCEsat=" + VCEsat
                + ", Voutmax=" + Voutmax
                + ", VRE2=" + VRE2
                + ", beta1=" + beta1
                + ", beta2=" + beta2
                + ", RB=" + RB
                + ", VB=" + VB
                + ", RII=" + RII
                + ", RL=" + RL
                + ", IBQ1=" + IBQ1
                + ", ICQ1=" + ICQ1
                + ", IEQ1=" + IEQ1
                + ", RC1=" + RC1
                + ", RE1=" + RE1
                + ", VBEQ1=" + VBEQ1
                + ", VCEQ1=" + VCEQ1
                + ", VRC1=" + VRC1
                + ", VRE1=" + VRE1
                + ", ICQ2=" + ICQ2
                + ", IEQ2=" + IEQ2
                + ", RE2=" + RE2
                + ", RE21=" + RE21
                + ", RE22=" + RE22
                + ", VBEQ2=" + VBEQ2
                + ", VCC=" + VCC
                + ", VCEQ2=" + VCEQ2
                + ", VRC2=" + VRC2
                + ", RC2=" + RC2
                + '}';
    }

    public double getRE21() {
        return RE21;
    }

    public double getRC2() {
        return RC2;
    }

    public double getRC1() {
        return RC1;
    }

    public double getRE1() {
        return RE1;
    }

    public double getRB() {
        return RB;
    }

    public double getICQ1() {
        return ICQ1;
    }

    public double getVCEQ1() {
        return VCEQ1;
    }

    public double getICQ2() {
        return ICQ2;
    }

    public double getVCEQ2() {
        return VCEQ2;
    }

    public double getRL() {
        return RL;
    }

    public double getbeta1() {
        return beta1;
    }

    public double getbeta2() {
        return beta2;
    }
}
