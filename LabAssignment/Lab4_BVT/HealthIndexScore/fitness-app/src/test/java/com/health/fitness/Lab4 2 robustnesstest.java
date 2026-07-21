package com.health.fitness;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Lab4.2 - HealthIndexScore Robustness Testing")
class Lab4_2_RobustnessTest {
    @Nested
    @DisplayName("Invalid inputs (TC001-TC006)")
    class InvalidInputs {

        @Test
        @DisplayName("TC001: vo2Max = min- (-1) throws IllegalArgumentException")
        void tc001_vo2MaxBelowMin() {
            assertThrows(IllegalArgumentException.class,
                    () -> new HealthIndexScore(-1, 70, 20));
        }

        @Test
        @DisplayName("TC002: vo2Max = max+ (101) SHOULD throw IllegalArgumentException - EXPECTED FAIL (DEF-001)")
        void tc002_vo2MaxAboveRealisticMax() {
            assertThrows(IllegalArgumentException.class,
                    () -> new HealthIndexScore(101, 70, 20));
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
        @DisplayName("TC006: hrr = max+ (41) SHOULD throw IllegalArgumentException - EXPECTED FAIL (DEF-002)")
        void tc006_hrrAboveRealisticMax() {
            assertThrows(IllegalArgumentException.class,
                    () -> new HealthIndexScore(45, 70, 41));
        }
    }


    @Nested
    @DisplayName("Valid inputs (TC007-TC018)")
    class ValidInputs {

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