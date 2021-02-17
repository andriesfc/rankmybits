package org.andriesfc.rankmeb

import org.andriesfc.rankmeb.core.LeagueRankBuilder
import org.andriesfc.rankmeb.mapper.mapLineToScoreCard
import picocli.CommandLine
import java.io.BufferedReader
import java.io.File
import java.io.Reader
import kotlin.system.exitProcess

@CommandLine.Command(
    description = [
        "This little app will rank league scores either from a file or standard in. "
    ],
    name = "rankmeb",
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

    override fun run() {

        val input = leagueCompetitionScoresInput?.open()
            ?: throw  CommandLine.ParameterException(spec?.commandLine(), "Nothing to do!")

        val rankings = input.use { reader ->
            LeagueRankBuilder().run {
                for (scoreCard in reader.lines().map(::mapLineToScoreCard)) {
                    updateRanking(scoreCard)
                }
                build()
            }
        }

        rankings.forEach { println(it) }
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

