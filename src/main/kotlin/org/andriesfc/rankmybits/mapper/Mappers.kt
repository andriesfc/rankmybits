package org.andriesfc.rankmybits.mapper

import org.andriesfc.rankmybits.core.ScoreCard
import org.andriesfc.rankmybits.core.TeamScore


/**
 * Converts a single line:
 *
 * ```
 * 1: Lions 3, Snakes 3
 * 2: Tarantulas 1, FC Awesome 0
 * 3: Lions 1, FC Awesome 1
 * 4: Tarantulas 3, Snakes 1
 * 5: Lions 4, Grouches 0
 * ```
 */
fun mapLineToScoreCard(scoreCardLine: String): ScoreCard {

    fun failNoScoreFound(s: String? = scoreCardLine): Nothing = throw IllegalArgumentException(
        "No Score card found in score found [$s}]"
    )

    val (teamLeftColumns, teamRightColumns) = scoreCardLine.trim().split(",")
        .take(2)
        .takeUnless { it.size < 2 }
        ?: failNoScoreFound()

    val scoreRegex = "([a-zA-z\\s]+)(\\d+)".toRegex()

    val (teamLeft, teamLeftScore) = scoreRegex.find(teamLeftColumns)?.destructured
        ?: failNoScoreFound(teamLeftColumns)

    val (teamRight, teamRightScore) = scoreRegex.find(teamRightColumns)?.destructured
        ?: failNoScoreFound(teamRightColumns)

    return ScoreCard(
        left = TeamScore(
            team = teamLeft.trim(),
            score = teamLeftScore.toInt()
        ),
        right = TeamScore(
            team = teamRight.trim(),
            score = teamRightScore.toInt()
        )
    )
}


