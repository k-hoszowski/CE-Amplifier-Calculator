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
 * Performing calculations for DC and AC analysis of a a specific CE amplifier
 * circuit (sketch included in the Maven project)
 *
 * @author krzysztofh
 * @version 1.01
 */
public class CE_Amplifier_Calculator {

    public static void main(String[] args) {
        Analysis_AC ac = new Analysis_AC();  // Init and DC
        ac.Perform_Nodal();                  // AC - nodal method
         System.out.println(ac);           // Debug
        System.out.println();

        ac = new Analysis_AC();              // Reset
        ac.Perform_Approximate();            // AC - approximate method
         System.out.println(ac);           // Debug
    }
}
