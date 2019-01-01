package server;

import java.io.*;
import java.util.ArrayList;

public class EventLog {
    private ArrayList<Event> log;
    private Encoder encoder;

    public EventLog(Encoder encoder) {
        log = new ArrayList<Event>();
        this.encoder = encoder;
    }

    public void addEvent(String line) {
        String[] columns = line.split(",");
        if(columns.length == 8) {
            log.add(new Event(columns[2].trim(), columns[4].trim(), columns[7].trim()));
        }
    }

    public void writeToFile(String filename) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filename));

        for(Event element : log) {
            writer.println(element.getEncoded(encoder));
        }
        writer.flush();
    }

    public int size() {
        return log.size();
    }
}
