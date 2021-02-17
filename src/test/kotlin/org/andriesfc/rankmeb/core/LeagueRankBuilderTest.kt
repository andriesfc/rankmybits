package org.andriesfc.rankmeb.core

import org.andriesfc.rankmeb.mapper.mapLineToScoreCard
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.assertAll


@TestInstance(Lifecycle.PER_CLASS)
internal class LeagueRankBuilderTest {

    private lateinit var rankingBuilder: LeagueRankBuilder
    private lateinit var gameScoreCards: Set<ScoreCard>
    private lateinit var expectedRanking: List<RankedTeam>

    @Test
    fun testBuildRankingWithDefaultRule() {
        giveRankBuilderWithDefaultRule()
        givenSampleScoreCards()
        givenExpectedSampleRanking()
        thenRankScores()
    }

    /**
     * ```
     * 1. Tarantulas, 6 pts
     * 2. Lions, 5 pts
     * 3. FC Awesome, 1 pt
     * 3. Snakes, 1 pt
     * 5. Grouches, 0 pts
    ```
     */
    private fun givenExpectedSampleRanking() {
        expectedRanking = listOf(
            RankedTeam("Tarantulas", 6),
            RankedTeam("Lions", 5),
            RankedTeam("FC Awesome", 1),
            RankedTeam("Snakes", 1),
            RankedTeam("Grouches", 0)
        ).sortedBy(RankedTeam::team)
    }

    private fun thenRankScores() {
        rankingBuilder.reset()
        gameScoreCards.forEach(rankingBuilder::updateRanking)
        val actualRanking = rankingBuilder.build()

        assertEquals(
            expectedRanking.size,
            actualRanking.size,
        ) { "expectedRanking.size(${expectedRanking.size}) != actualRaking.size(${actualRanking.size})" }

        val assertions = mutableListOf<() -> Unit>()

        expectedRanking.withIndex().map { (i, expected) ->
            {
                val actual = actualRanking.getOrNull(i)
                assertEquals(expected, actual) {
                    "Expected ranking at ${i + 1}"
                }
            }
        }.forEach { assertions += it }

        assertAll(assertions)
    }

    /**
     * ```
     * Lions 3, Snakes 3
     * Tarantulas 1, FC Awesome 0
     * Lions 1, FC Awesome 1
     * Tarantulas 3, Snakes 1
     * Lions 4, Grouches 0
     * ```
     */
    private fun givenSampleScoreCards() {
        gameScoreCards = """
            Lions 3, Snakes 3
            Tarantulas 1, FC Awesome 0
            Lions 1, FC Awesome 1
            Tarantulas 3, Snakes 1
            Lions 4, Grouches 0
        """.trimIndent().lineSequence().map { mapLineToScoreCard(it) }.toSet()
    }

    private fun giveRankBuilderWithDefaultRule() {
        rankingBuilder = LeagueRankBuilder(defaultRankingRule)
    }

}
