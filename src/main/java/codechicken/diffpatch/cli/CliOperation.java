package codechicken.diffpatch.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Created by covers1624 on 11/8/20.
 */
public abstract class CliOperation<T> {

    protected final PrintStream logger;

    private final Consumer<PrintStream> helpCallback;
    @Deprecated
    protected boolean verbose;
    protected final Level level;

    @Deprecated
    protected CliOperation(PrintStream logger, Consumer<PrintStream> helpCallback, boolean verbose) {
        this(logger, helpCallback, verbose ? Level.ALL : Level.WARNING);
    }

    protected CliOperation(PrintStream logger, Consumer<PrintStream> helpCallback, Level level) {
        this.logger = logger;
        this.helpCallback = helpCallback;
        this.level = level;
        this.verbose = this.level == Level.ALL;
    }

    public abstract Result<T> operate() throws IOException;

    public final void printHelp() throws IOException {
        helpCallback.accept(logger);
    }

    @Deprecated
    public final void log(String str, Object... args) {
        log(Level.INFO, str, args);
    }

    @Deprecated
    public final void verbose(String str, Object... args) {
        log(Level.FINE, str, args);
    }

    public final void log(Level level, String str, Object... args) {
        if (this.level.intValue() <= level.intValue()) {
            logger.println(String.format(str, args));
        }
    }

    public static class Result<T> {

        public final int exit;
        public final T summary;

        public Result(int exit) {
            this(exit, null);
        }

        public Result(int exit, T summary) {
            this.exit = exit;
            this.summary = summary;
        }

        public void throwOnError() {
            if (exit != 0) {
                throw new RuntimeException("Operation has non zero exit code: " + exit);
            }
        }
    }

}
