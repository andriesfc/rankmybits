@file:Suppress("unused")

package org.andriesfc.rankmybits

import org.andriesfc.rankmybits.core.LeagueRankBuilder
import org.andriesfc.rankmybits.core.RankedTeam
import org.andriesfc.rankmybits.parser.parseScoreCardLine
import picocli.CommandLine
import picocli.CommandLine.Option
import java.io.BufferedReader
import java.io.File
import java.io.Reader
import kotlin.system.exitProcess

@CommandLine.Command(
    description = [
        "This little app will rank league scores either from a file or standard in. "
    ],
    name = "rankmybits",
    header = ["Rank My B!ts"],
    version = ["1.0.0-SNAPSHOT"],
    mixinStandardHelpOptions = true,
    subcommands = [CommandLine.HelpCommand::class]
)
class RankTeamApp : Runnable {

    @CommandLine.Spec
    internal var spec: CommandLine.Model.CommandSpec? = null

    @CommandLine.ArgGroup(
        validate = true,
        exclusive = true,
        heading = "Input league scores either via standard input, or via file.%n"
    )
    internal var leagueCompetitionScoresInput: LeagueCompetitionScoresInput? = null

    @CommandLine.ArgGroup(
        validate = true,
        exclusive = true,
        heading = "Use any of the following ways to sort rankings:%n"
    )
    internal var sorting: RankingSorting? = null

    override fun run() {

        val input = leagueCompetitionScoresInput?.open()
            ?: throw  CommandLine.ParameterException(spec?.commandLine(), "Nothing to do!")

        val rankings = input.use { reader ->
            LeagueRankBuilder().run {
                for (scoreCard in reader.lines().map(::parseScoreCardLine)) {
                    updateRanking(scoreCard)
                }
                build()
            }
        }.let { ranked ->
            when (val c = sorting?.comparator) {
                null -> ranked
                else -> ranked.sortedWith(c)
            }
        }

        rankings.forEach(::println)
    }


    internal class LeagueCompetitionScoresInput {

        @CommandLine.Spec
        internal var spec: CommandLine.Model.CommandSpec? = null

        private var openReader: (() -> Reader)? = null

        @CommandLine.Parameters(
            index = "0",
            arity = "1",
            paramLabel = "FILE",
            description = [
                "The file to read league scores from."
            ]
        )
        fun setInputFile(file: File) {
            openReader = { file.reader() }
        }

        @CommandLine.Option(
            names = ["--stdin"],
            defaultValue = "false",
            required = true,
            description = [
                "Read from ranking file from STDIN"
            ]
        )
        fun setInputStdIn(stdIn: Boolean) {
            if (stdIn) {
                openReader = {
                    System.`in`.reader()
                }
            }
        }

        fun open(): BufferedReader {
            return openReader?.invoke()?.buffered()
                ?: throw CommandLine.ParameterException(spec?.commandLine(), "Please provide source of scores.")
        }
    }

    internal class RankingSorting {

        private var sorting: Comparator<RankedTeam>? = null

        @Option(
            names = ["--sort-by-team"],
            description = [
                "Sorts ranking by team name. Note the (this is the default)"
            ]
        )
        fun setSortByTeam(b: Boolean) {
            if (b) {
                sorting = compareBy(RankedTeam::team)
            }
        }

        @Option(
            names = ["--sort-by-ranking"],
            description = [
                "Sorts by ranking in descending order (highest first)"
            ]
        )
        fun setSortByRank(b: Boolean) {
            if (b) {
                sorting = compareByDescending(RankedTeam::points)
            }
        }


        companion object {
            private val defaultSorting = compareBy<RankedTeam>(RankedTeam::team)
        }

        val comparator: Comparator<RankedTeam>
            get() = sorting ?: defaultSorting
    }

}

fun main(args: Array<String>) {
    exitProcess(
        CommandLine(RankTeamApp())
            .setUsageHelpAutoWidth(true)
            .setToggleBooleanFlags(true)
            .setExpandAtFiles(true)
            .execute(* args)
    )
}

