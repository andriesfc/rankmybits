@file:Suppress("unused")

package org.andriesfc.rankmybits.parser

import org.andriesfc.rankmybits.core.ScoreCard
import org.andriesfc.rankmybits.core.TeamScore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


@TestInstance(Lifecycle.PER_CLASS)
internal class ParsingTests {

    @ParameterizedTest
    @MethodSource("mapLineToScoreCardTest")
    fun mapLineToScoreCardTest(cardTestMapParams: ScoreCardTestMapParams) {
        val actualScoreCard = parseScoreCardLine(cardTestMapParams.givenScoreCardLine)
        assertEquals(cardTestMapParams.expectedScoreCard, actualScoreCard)

    }

    internal data class ScoreCardTestMapParams(
        val givenScoreCardLine: String,
        val expectedScoreCard: ScoreCard
    )

    fun mapLineToScoreCardTest(): List<ScoreCardTestMapParams> {
        return listOf(
            ScoreCardTestMapParams(
                givenScoreCardLine = "Lions 3, Snakes 3",
                expectedScoreCard = ScoreCard(
                    left = TeamScore(
                        team = "Lions",
                        score = 3
                    ),
                    right = TeamScore(
                        team = "Snakes",
                        score = 3
                    )
                )
            ),
            ScoreCardTestMapParams(
                givenScoreCardLine = "Tarantulas 1, FC Awesome 0",
                expectedScoreCard = ScoreCard(
                    left = TeamScore(
                        team = "Tarantulas",
                        score = 1
                    ),
                    right = TeamScore(
                        team = "FC Awesome",
                        score = 0
                    )
                )
            ),
            ScoreCardTestMapParams(
                givenScoreCardLine = "  Lions 4, Grouches 0",
                expectedScoreCard = ScoreCard(
                    left = TeamScore(
                        team = "Lions",
                        score = 4
                    ),
                    right = TeamScore(
                        team = "Grouches",
                        score = 0
                    )
                )
            ),
        )
    }

}
