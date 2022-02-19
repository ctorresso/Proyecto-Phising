import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CvsFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return record.getMillis() +
                "," + record.getLevel() + "," + record.getMessage() + "\n";
    }
}
