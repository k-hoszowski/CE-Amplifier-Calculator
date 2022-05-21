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
 * Includes: Parallel equivalent operation (eg. on resistors, capacitors)
 *
 * @author krzysztofh
 * @version 1
 */
public class Electronics_Theorems {

    public static double parallel(double a, double b) {
        return a * b / (a + b);
    }
}
