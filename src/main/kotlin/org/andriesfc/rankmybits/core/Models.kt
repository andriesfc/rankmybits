package org.andriesfc.rankmybits.core


enum class Outcome {
    LOST,
    TIE,
    WON;
}

data class TeamScore(val team: String, val score: Int) : Comparable<TeamScore> {
    override fun compareTo(other: TeamScore): Int {
        return score.compareTo(other.score)
    }
}

data class ScoreCard(val left: TeamScore, val right: TeamScore) : AbstractCollection<Pair<TeamScore, Outcome>>() {

    constructor(leftTeamWithScore: Pair<String, Int>, rightTeamWithScore: Pair<String, Int>) : this(
        TeamScore(
            team = leftTeamWithScore.first,
            score = leftTeamWithScore.second
        ),
        TeamScore(
            team = rightTeamWithScore.first,
            score = rightTeamWithScore.second
        )
    )

    private val leftOutcome: Outcome
    private val rightOutcome: Outcome
    override val size: Int = 2

    init {

        require(left.team != right.team) {
            "Team cannot be scored against itself."
        }

        when {
            left > right -> {
                leftOutcome = Outcome.WON
                rightOutcome = Outcome.LOST
            }
            left < right -> {
                leftOutcome = Outcome.LOST
                rightOutcome = Outcome.WON
            }
            else -> {
                leftOutcome = Outcome.TIE
                rightOutcome = Outcome.TIE
            }
        }
    }

    override operator fun iterator(): Iterator<Pair<TeamScore, Outcome>> = iterator {
        yield(get(0))
        yield(get(1))
    }

    operator fun get(team: String): Pair<Int, Outcome>? {
        return firstOrNull { (t, _) -> t.team == team }?.let { (t, o) -> t.score to o }
    }

    operator fun get(position: Int): Pair<TeamScore, Outcome> {
        return when (position) {
            0 -> left to leftOutcome
            1 -> right to rightOutcome
            else -> throw NoSuchElementException()
        }
    }

}

data class RankedTeam(val team: String, val points: Int) : Comparable<RankedTeam> {
    override fun toString(): String = "$team, $points ${if (points == 1) "pt" else "pts"}"
    override fun compareTo(other: RankedTeam): Int = points.compareTo(other.points)
}
