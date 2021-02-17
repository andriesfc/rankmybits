package org.andriesfc.rankmeb.core


enum class Outcome : Comparable<Outcome> {
    LOST,
    TIE,
    WON;
}


data class TeamScore(val team: String, val score: Int) : Comparable<TeamScore> {
    override fun compareTo(other: TeamScore): Int {
        return score.compareTo(other.score)
    }
}

data class ScoreCard(val left: TeamScore, val right: TeamScore) {

    val leftOutcome: Outcome
    val rightOutcome: Outcome

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
}

data class RankedTeam(val team: String, val points: Int) : Comparable<RankedTeam> {
    override fun toString(): String = "$team, $points pts"
    override fun compareTo(other: RankedTeam): Int = points.compareTo(other.points)
}
