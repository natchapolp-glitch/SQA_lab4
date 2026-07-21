package com.health.fitness;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Lab4.1 - HealthIndexScore Normal Boundary Value Testing")
class Lab4_1_NormalBoundaryValueTest {

    @ParameterizedTest(name = "{0}: vo2Max={1}, rhr={2}, hrr={3} -> total={4} ({5})")
    @DisplayName("Total score / FitnessLevel matches Excel 'Normal' sheet")
    @CsvSource({
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