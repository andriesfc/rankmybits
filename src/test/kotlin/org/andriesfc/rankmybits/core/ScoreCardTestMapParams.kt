@file:Suppress("unused")

package org.andriesfc.rankmybits.core

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.Arguments.of as testParametersOf


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ScoreCardTestMapParams {

    @ParameterizedTest
    @MethodSource("testScoreCards")
    fun testScoreCardOutcome(
        givenScoreCard: ScoreCard,
        expectedLeftOutCome: Outcome,
        expectedRightOutcome: Outcome
    ) {
        assertAll(
            { assertEquals(expectedLeftOutCome, givenScoreCard.leftOutcome) },
            { assertEquals(expectedRightOutcome, givenScoreCard.rightOutcome) }
        )
    }

    @Test
    fun testScoreCardDoesNotAllowTeamSelfScore() {
        val actual = assertThrows<IllegalArgumentException> {
            val selfScore1 = TeamScore("team_1", 10)
            val selfScore2 = TeamScore(selfScore1.team, 20)
            ScoreCard(selfScore1, selfScore2)
        }
        println(actual.message)
    }

    companion object {

        /**
         * ```
         * 1: Lions 3, Snakes 3
         * 2: Tarantulas 1, FC Awesome 0
         * 3: Lions 1, FC Awesome 1
         * 4: Tarantulas 3, Snakes 1
         * 5: Lions 4, Grouches 0
         * ```
         */
        @JvmStatic
        fun testScoreCards() = listOf(
            testParametersOf(
                ScoreCard(
                    TeamScore("Lions", 3),
                    TeamScore("Snakes", 3)
                ),
                Outcome.TIE,
                Outcome.TIE
            ),
            testParametersOf(
                ScoreCard(
                    TeamScore("Tarantulas", 1),
                    TeamScore("FC Awesome", 0)
                ),
                Outcome.WON,
                Outcome.LOST
            ),
            testParametersOf(
                ScoreCard(
                    TeamScore("Lions", 1),
                    TeamScore("FC Awesome", 1),
                ),
                Outcome.TIE,
                Outcome.TIE
            ),
            testParametersOf(
                ScoreCard(
                    TeamScore("Tarantulas", 3),
                    TeamScore("Snakes", 1),
                ),
                Outcome.WON,
                Outcome.LOST
            ),
            testParametersOf(
                ScoreCard(
                    TeamScore("Snakes", 0),
                    TeamScore("Team B",1)
                ),
                Outcome.LOST,
                Outcome.WON
            )
        )

    }

}
