package org.andriesfc.rankmybits.core


fun interface RankingRule {
    fun scoreOutcome(outcome: Outcome): Int
}

val defaultRankingRule = RankingRule { outcome ->
    when (outcome) {
        Outcome.LOST -> 0
        Outcome.TIE -> 1
        Outcome.WON -> 3
    }
}

class LeagueRankBuilder(private val rule: RankingRule = defaultRankingRule) {

    private val teamRankings = mutableMapOf<String, Int>()

    fun updateRanking(scoreCard: ScoreCard) {
        scoreCard.forEach { (ranked, outcome) ->
            updateRanking(ranked.team, outcome)
        }
    }

    private fun updateRanking(team: String, outcome: Outcome) {
        val current = teamRankings[team] ?: 0
        teamRankings[team] = current + rule.scoreOutcome(outcome)
    }

    fun build(): List<RankedTeam> {
        return teamRankings.map { (team, ranking) -> RankedTeam(team, ranking) }.sortedBy(RankedTeam::team)
    }

    fun reset() {
        teamRankings.clear()
    }

}
