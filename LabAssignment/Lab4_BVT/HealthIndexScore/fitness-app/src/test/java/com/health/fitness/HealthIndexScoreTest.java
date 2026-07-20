package com.health.fitness;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CP353201 Software Quality Assurance (1/2569)
 * Lab#4 - Boundary Value Analysis and Robustness Testing
 *
 * This test class mirrors Lab4_BVT.xlsx exactly:
 *   - "Normal"     sheet -> TC001-TC018 -> NormalBoundaryValueTesting
 *   - "Robustness" sheet -> TC001-TC006 -> RobustnessInvalidInputs (invalid, throws)
 *                            TC007-TC018 -> RobustnessValidInputs   (valid boundary values)
 *
 * Design balance (per test case category):
 *   Normal      : 6 Poor / 6 Standard / 6 Excellent                       = 18
 *   Robustness  : 6 invalid (throws) / 4 Poor / 4 Standard / 4 Excellent  = 18
 */
@DisplayName("HealthIndexScore")
class HealthIndexScoreTest {

    // =====================================================================
    // NORMAL SHEET (TC001-TC018) - valid boundary values, 6 Poor/6 Standard/6 Excellent
    // =====================================================================
    @Nested
    @DisplayName("Normal sheet (TC001-TC018)")
    class NormalBoundaryValueTesting {

        @ParameterizedTest(name = "{0}: vo2Max={1}, rhr={2}, hrr={3} -> total={4} ({5})")
        @DisplayName("Total score / FitnessLevel matches Excel 'Normal' sheet")
        @CsvSource({
                // TC,   vo2Max, rhr, hrr, expectedTotal, expectedLevel
                "TC001,  0,      90,  0,   2,  POOR",
                "TC002,  24,     90,  11,  2,  POOR",
                "TC003,  25,     90,  0,   3,  POOR",
                "TC004,  30,     219, 1,   3,  POOR",
                "TC005,  40,     220, 0,   4,  POOR",
                "TC006,  50,     85,  1,   5,  POOR",

                "TC007,  45,     70,  20,  10, STANDARD",
                "TC008,  51,     84,  0,   8,  STANDARD",
                "TC009,  41,     61,  39,  11, STANDARD",
                "TC010,  60,     220, 1,   6,  STANDARD",
                "TC011,  31,     40,  0,   8,  STANDARD",
                "TC012,  100,    85,  1,   7,  STANDARD",

                "TC013,  51,     40,  25,  14, EXCELLENT",
                "TC014,  61,     60,  19,  14, EXCELLENT",
                "TC015,  100,    50,  30,  15, EXCELLENT",
                "TC016,  55,     41,  40,  14, EXCELLENT",
                "TC017,  41,     40,  25,  13, EXCELLENT",
                "TC018,  45,     60,  24,  12, EXCELLENT",
        })
        void normalCases(String tcId, double vo2Max, int rhr, int hrr,
                          int expectedTotal, String expectedLevel) {
            HealthIndexScore h = new HealthIndexScore(vo2Max, rhr, hrr);

            assertEquals(expectedTotal, h.getTotalScore(), tcId + ": total score mismatch");
            assertEquals(HealthIndexScore.FitnessLevel.valueOf(expectedLevel),
                    h.getFitnessLevel(), tcId + ": fitness level mismatch");
        }
    }

    // =====================================================================
    // ROBUSTNESS SHEET - TC001-TC006 - invalid boundary values (min-, max+)
    // =====================================================================
    @Nested
    @DisplayName("Robustness sheet - invalid inputs (TC001-TC006)")
    class RobustnessInvalidInputs {

        @Test
        @DisplayName("TC001: vo2Max = min- (-1) throws IllegalArgumentException")
        void tc001_vo2MaxBelowMin() {
            assertThrows(IllegalArgumentException.class,
                    () -> new HealthIndexScore(-1, 70, 20));
        }

        @Test
        @DisplayName("TC002: vo2Max = max+ (101) - DEFECT: no upper-bound validation, does NOT throw")
        void tc002_vo2MaxAboveRealisticMax() {
            // Per the requirement, VO2 Max should be validated against a realistic
            // upper limit, but validateInputs() only checks vo2Max < 0.
            // This is documented as DEF-001 in the Defect Summary sheet.
            HealthIndexScore h = assertDoesNotThrow(
                    () -> new HealthIndexScore(101, 70, 20));
            assertEquals(5, h.calculateVo2MaxScore());
        }

        @Test
        @DisplayName("TC003: rhr = min- (39) throws IllegalArgumentException")
        void tc003_rhrBelowMin() {
            assertThrows(IllegalArgumentException.class,
                    () -> new HealthIndexScore(45, 39, 20));
        }

        @Test
        @DisplayName("TC004: rhr = max+ (221) throws IllegalArgumentException")
        void tc004_rhrAboveMax() {
            assertThrows(IllegalArgumentException.class,
                    () -> new HealthIndexScore(45, 221, 20));
        }

        @Test
        @DisplayName("TC005: hrr = min- (-1) throws IllegalArgumentException")
        void tc005_hrrBelowMin() {
            assertThrows(IllegalArgumentException.class,
                    () -> new HealthIndexScore(45, 70, -1));
        }

        @Test
        @DisplayName("TC006: hrr = max+ (41) - DEFECT: no upper-bound validation, does NOT throw")
        void tc006_hrrAboveRealisticMax() {
            // Per the requirement, HRR should be validated against a realistic
            // upper limit, but validateInputs() only checks hrr < 0.
            // This is documented as DEF-002 in the Defect Summary sheet.
            HealthIndexScore h = assertDoesNotThrow(
                    () -> new HealthIndexScore(45, 70, 41));
            assertEquals(5, h.calculateHrrScore());
        }
    }

    // =====================================================================
    // ROBUSTNESS SHEET - TC007-TC018 - valid boundary values, 4 Poor/4 Standard/4 Excellent
    // =====================================================================
    @Nested
    @DisplayName("Robustness sheet - valid inputs (TC007-TC018)")
    class RobustnessValidInputs {

        @ParameterizedTest(name = "{0}: vo2Max={1}, rhr={2}, hrr={3} -> total={4} ({5})")
        @DisplayName("Total score / FitnessLevel matches Excel 'Robustness' sheet")
        @CsvSource({
                // TC,   vo2Max, rhr, hrr, expectedTotal, expectedLevel
                "TC007,  0,      90,  0,   2,  POOR",
                "TC008,  25,     90,  0,   3,  POOR",
                "TC009,  40,     220, 0,   4,  POOR",
                "TC010,  50,     85,  1,   5,  POOR",

                "TC011,  45,     70,  20,  10, STANDARD",
                "TC012,  41,     61,  39,  11, STANDARD",
                "TC013,  60,     220, 1,   6,  STANDARD",
                "TC014,  100,    85,  1,   7,  STANDARD",

                "TC015,  51,     40,  25,  14, EXCELLENT",
                "TC016,  100,    50,  30,  15, EXCELLENT",
                "TC017,  55,     41,  40,  14, EXCELLENT",
                "TC018,  45,     60,  24,  12, EXCELLENT",
        })
        void robustnessValidCases(String tcId, double vo2Max, int rhr, int hrr,
                                   int expectedTotal, String expectedLevel) {
            HealthIndexScore h = new HealthIndexScore(vo2Max, rhr, hrr);

            assertEquals(expectedTotal, h.getTotalScore(), tcId + ": total score mismatch");
            assertEquals(HealthIndexScore.FitnessLevel.valueOf(expectedLevel),
                    h.getFitnessLevel(), tcId + ": fitness level mismatch");
        }
    }
}