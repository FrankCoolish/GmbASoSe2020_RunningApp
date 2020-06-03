package gmba.runningapp.model.datenspeicher.classes;

import java.util.List;

public interface User {
    int getId();
    String getName();
    int getAnzahlRuns();
    List<Run> getHistory();
    void setHistory(List<Run> newHistory);

}
