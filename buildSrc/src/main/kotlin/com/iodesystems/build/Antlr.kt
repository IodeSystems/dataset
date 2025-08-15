package com.iodesystems.build

import com.iodesystems.build.Bash.bash
import com.iodesystems.build.Strings.quoteDir
import java.io.File

object Antlr {
  fun antlr(
    packageName: String,
    grammarFile: String,
    inputDir: String,
    outputDir: String,
    compileOnlyDepFiles: String,
  ) {
    val pkgPath = packageName.replace(".", "/")
    File(outputDir).mkdirs()
    val output = "$outputDir/$pkgPath".quoteDir()
    val target = "$inputDir/$grammarFile".quoteDir()

    val cmd = """
      java \
        -classpath $compileOnlyDepFiles \
        org.antlr.v4.Tool \
        -o $output \
        -package $packageName \
        -lib ${outputDir.quoteDir()} \
        $target
    """.trimIndent()
    val bash = cmd.bash()
    if (bash.exitCode != 0) {
      throw RuntimeException(
        """
        | antlr exec ~= $cmd
        | failed with code=${bash.exitCode} because:
        |${bash.output}
        """.trimMargin()
      )
    }
  }
}
