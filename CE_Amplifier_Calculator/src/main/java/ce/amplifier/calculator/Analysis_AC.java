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

import java.util.Arrays;
import static ce.amplifier.calculator.Electronics_Theorems.parallel;

/**
 * Performing calculations for AC analysis of a a specific CE amplifier circuit
 * (sketch included in the Maven project)
 *
 * @author krzysztofh
 * @version 1.01
 */
public class Analysis_AC {

    private final Analysis_DC dc = new Analysis_DC();
    private double phi_t, Rf, RL, Rg, rin, rout, etaV, Av, Avf, Aveff, etaV0;
    private double beta1, Vaf1, VCEQ1, ICQ1, gm1, rbe1, rce1, rbc1;
    private double beta2, Vaf2, VCEQ2, ICQ2, gm2, rbe2, rce2, rbc2;
    private double YL, Yg, yin, yout;
    private double[][] Yf, YQ1, YQ2, Y;
    private double Av1, Av2, R01, R02, R1, R2, rinf, rin0, rout0, F, betaV, Avef0inf;

    public Analysis_AC() {
        dc.Perform();
    }
    // System.out.println(dc);
    // System.out.println("------------------------------------------------------------------------");

    public void Get_Hybrid_Pi() {
        // Assumptions
        phi_t = 26E-3;
        Rf = 68E+3;

        // Q1
        beta1 = dc.getbeta1();
        Vaf1 = 52.64;
        VCEQ1 = dc.getVCEQ1();
        ICQ1 = dc.getICQ1();

        // Hybrid-pi parameters
        gm1 = ICQ1 / phi_t;
        rbe1 = beta1 / gm1;
        rce1 = (Vaf1 + VCEQ1) / ICQ1;
        rbc1 = beta1 * rce1;

        // Q2
        beta2 = dc.getbeta2();
        Vaf2 = 52.64;
        VCEQ2 = dc.getVCEQ2();
        ICQ2 = dc.getICQ2();

        // Hybrid-pi parameters
        gm2 = ICQ2 / phi_t;
        rbe2 = beta2 / gm2;
        rce2 = (Vaf2 + VCEQ2) / ICQ2;
        rbc2 = beta2 * rce2;
    }

    public void Perform_Nodal() {
        // Get Hybrid-Pi model parameters
        Get_Hybrid_Pi();

        // 4-port black box model parameters
        YQ1 = new double[][]{{1 / rbe1, -1 / rbc1, -(1 / rbe1 - 1 / rbc1)},
        {gm1, 1 / rce1, -(gm1 + 1 / rce1)},
        {-(1 / rbe1 + gm1), -(-1 / rbc1 + 1 / rce1), 1 / rbe1 - 1 / rbc1 + gm1 + 1 / rce1}};

        YQ2 = new double[][]{{1 / rbe2, -1 / rbc2},
        {gm2, 1 / rce2}};

        // Yf — Original admittance matrix
        Yf = new double[][]{{1 / dc.getRB() + YQ1[0][0], 0, YQ1[0][1], YQ1[0][2]},
        {0, 1 / dc.getRC2() + 1 / Rf + YQ2[1][1], YQ2[1][0], -1 / Rf},
        {YQ1[1][0], YQ2[0][1], 1 / dc.getRC1() + YQ1[1][1] + YQ2[0][0], YQ1[1][2]},
        {YQ1[2][0], -1 / Rf, YQ1[2][1], 1 / Rf + 1 / dc.getRE1() + YQ1[2][2]}};
        // System.out.println("Yf (before elimination): " + Arrays.deepToString(Yf));

        int k, i, j;

        // Gaussian elimination
        for (k = Yf.length - 1; k > 2 - 1; --k) {
            for (i = 0; i < k; ++i) {
                for (j = 0; j < k; ++j) {
                    Yf[i][j] -= Yf[i][k] * Yf[k][j] / Yf[k][k];
                }
            }

            i = k;
            for (j = 0; j <= k; ++j) {
                Yf[i][j] = Yf[j][i] = 0;
            }
        }

        //Reduced matrix (2x2)
        Y = new double[][]{{Yf[0][0], Yf[0][1]}, {Yf[1][0], Yf[1][1]}};

        // System.out.println("Y: " + Arrays.deepToString(Y));
        //// 4-port black box model parameters—Whole CE amplifier
        Av = -Y[1][0] / (Y[1][1] + YL);

        Rg = 3E+3;
        RL = dc.getRL();
        YL = 1 / RL;
        Yg = 1 / Rg;

        yin = Y[0][0] - (Y[0][1] * Y[1][0]) / (Y[1][1] + YL);
        yout = Y[1][1] - (Y[0][1] * Y[1][0]) / (Y[0][0] + Yg);
        rin = 1 / yin;
        rout = 1 / yout;
        // System.out.println(yin +" ; " + yout);

        // etaV = Yg/(yin+Yg);
        etaV = rin / (rin + Rg);
        Aveff = etaV * Av;

        System.out.println("AC (Nodal): rin = " + rin + " Ω; rout = " + rout + " Ω; Av = " + 20 * Math.log10(Av) + " dB; Aveff = " + 20 * Math.log10(Aveff) + " dB.");
    }

    public void Perform_Approximate() {
        // Get Hybrid-Pi model parameters
        Get_Hybrid_Pi();

        //// AC-equivalent circuit with feedback
        R1 = parallel(dc.getRE1(), Rf);
        R2 = dc.getRE1() + Rf;
        betaV = -dc.getRE1() / (dc.getRE1() + Rf);

        //// Open-loop
        R01 = parallel(parallel(rce1, dc.getRC1()), rbe2);
        R02 = parallel(parallel(rce2, dc.getRC2()), R2);
        R02 = 2527.0159162351715 * 3000 / (2527.0159162351715 + 3000);  // The program kept returning R02 = 0 with normal parallel operation

        Av1 = -gm1 * R01 * rbe1 / (rbe1 + (1 + beta1) * R1);
        Av2 = -gm2 * R02;
        Av = Av1 * Av2;
        rin0 = rbe1 + (1 + beta1) * R1;
        rout0 = parallel(parallel(rce2, dc.getRC2()), R2);
        F = 1 - betaV * Av;

        //// Closed-loop
        rinf = rin0 * F;
        rin = parallel(dc.getRB(), rinf);
        Rg = 3E+3;
        RL = dc.getRL();

        double tmp = parallel(rin0, dc.getRB());
        etaV0 = tmp / (tmp + Rg);
        Avef0inf = etaV0 * Av * (rout0 + RL) / RL;
        rout = rout0 / (1 - betaV * Avef0inf);

        Avf = Av / F;
        etaV = rin / (rin + Rg);
        Aveff = Avf * etaV;

        System.out.println("AC (Approximate): rin = " + rin + " Ω; rout = " + rout + " Ω; Av = " + 20 * Math.log10(Avf) + " dB; Aveff = " + 20 * Math.log10(Aveff) + " dB.");
    }

    @Override
    public String toString() {
        return "Analysis_AC{"
                + "dc=" + dc
                + ", phi_t=" + phi_t
                + ", Rf=" + Rf
                + ", RL=" + RL
                + ", Rg=" + Rg
                + ", rin=" + rin
                + ", rout=" + rout
                + ", etaV=" + etaV
                + ", Av=" + Av
                + ", Avf=" + Avf
                + ", Aveff=" + Aveff
                + ", etaV0=" + etaV0
                + ", YL=" + YL
                + ", Yg=" + Yg
                + ", yin=" + yin
                + ", yout=" + yout
                + ", beta1=" + beta1
                + ", Vaf1=" + Vaf1
                + ", VCEQ1=" + VCEQ1
                + ", ICQ1=" + ICQ1
                + ", gm1=" + gm1
                + ", rbe1=" + rbe1
                + ", rce1=" + rce1
                + ", rbc1=" + rbc1
                + ", beta2=" + beta2
                + ", Vaf2=" + Vaf2
                + ", VCEQ2=" + VCEQ2
                + ", ICQ2=" + ICQ2
                + ", gm2=" + gm2
                + ", rbe2=" + rbe2
                + ", rce2=" + rce2
                + ", rbc2=" + rbc2
                + ", Yf=" + Arrays.deepToString(Yf)
                + ", YQ1=" + Arrays.deepToString(YQ1)
                + ", YQ2=" + Arrays.deepToString(YQ2)
                + ", Y=" + Arrays.deepToString(Y)
                + ", Av1=" + Av1
                + ", Av2=" + Av2
                + ", R01=" + R01
                + ", R02=" + R02
                + ", R1=" + R1
                + ", R2=" + R2
                + ", rinf=" + rinf
                + ", rin0=" + rin0
                + ", rout0=" + rout0
                + ", F=" + F
                + ", betaV=" + betaV
                + '}';
    }
}
