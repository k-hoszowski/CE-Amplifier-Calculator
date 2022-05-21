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
 * Finds in an array a number closest to target value
 *
 * @author krzysztofh
 * @version 1
 */
public class Find_Closest_Number {

    public static double findClosest(double[] arr, double target) {
        int n = arr.length;

        if (target <= arr[0]) {
            return arr[0];
        }
        if (target >= arr[n - 1]) {
            return arr[n - 1];
        }

        int i = 0, j = n, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (arr[mid] == target) {
                return arr[mid];
            }

            if (target < arr[mid]) {

                if (mid > 0 && target > arr[mid - 1]) {
                    return getClosest(arr[mid - 1],
                            arr[mid], target);
                }

                j = mid;
            } else {
                if (mid < n - 1 && target < arr[mid + 1]) {
                    return getClosest(arr[mid],
                            arr[mid + 1], target);
                }
                i = mid + 1;
            }
        }
        return arr[mid];
    }

    public static double getClosest(double val1, double val2, double target) {
        if (target - val1 >= val2 - target) {
            return val2;
        } else {
            return val1;
        }
    }
}
