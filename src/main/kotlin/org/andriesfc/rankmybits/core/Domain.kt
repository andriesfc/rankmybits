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

    private val rankings = mutableMapOf<String, Int>()

    fun updateRanking(scoreCard: ScoreCard) {
        update(scoreCard.left.team, rule.scoreOutcome(scoreCard.leftOutcome))
        update(scoreCard.right.team, rule.scoreOutcome(scoreCard.rightOutcome))
    }

    private fun update(team: String, ranking: Int) {
        rankings[team] = ranking + rankings.getOrDefault(team, 0)
    }

    fun build(): List<RankedTeam> {
        return rankings.map { (team, ranking) -> RankedTeam(team, ranking) }.sortedBy(RankedTeam::team)
    }

    fun reset() {
        rankings.clear()
    }

}
