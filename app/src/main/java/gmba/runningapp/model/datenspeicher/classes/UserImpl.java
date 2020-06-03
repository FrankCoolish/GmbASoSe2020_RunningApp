package gmba.runningapp.model.datenspeicher.classes;

import java.util.List;

public class UserImpl implements User {
    private int id;
    private String name;
    private int anzahlRuns;
    private List<Run> history;

    public UserImpl(int id, String name, int anzahlRuns, List<Run> history ){
        this.id = id;
        this.name = name;
        this.anzahlRuns = anzahlRuns;
        this.history = history;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAnzahlRuns() {
        return anzahlRuns;
    }

    @Override
    public List<Run> getHistory() {
        return history;
    }

    @Override
    public void setHistory(List<Run> newHistory) {
        this.history = newHistory;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
