package server;

import java.net.*;
import java.io.*;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ca.pfv.spmf.algorithms.frequentpatterns.aprioriTID.AlgoAprioriTID;
import ca.pfv.spmf.patterns.itemset_array_integers_with_tids.*;

public class Handler implements Runnable {
    private final int eventSize = 3; // Number of fields per event

    private Socket socket;
    private BufferedReader input;
    private PrintStream output;

    private int minOccurrences;

    private EventLog eventLog;
    private Encoder encoder;

    public Handler(Socket socket) throws IOException {
        System.out.println("New connection from " + socket.getInetAddress());

        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
        output = new PrintStream(socket.getOutputStream());

        encoder = new Encoder();
        eventLog = new EventLog(encoder);
    }

    public void run() {
        try {
            getRequest();
            mineData();
            output.println();
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        close();
    }

    private void getRequest() throws IOException {
        String line;
        boolean isFirst = true;

        minOccurrences = Integer.parseInt(input.readLine());
        while(!(line = input.readLine()).isEmpty()) {
            if(!isFirst) {
                eventLog.addEvent(line);
            } else {
                isFirst = false;
            }
        }
    }

    private void mineData() throws IOException {
        // Write the data into a temporary file
        String inputFile = "input." + ThreadLocalRandom.current().nextInt(0,999) + ".in";
        File tmpFile = new File(inputFile);
        if(!tmpFile.createNewFile()) throw new IOException("Temporary file already exists");
        eventLog.writeToFile(inputFile);

        // Run the mining algorithm
        AlgoAprioriTID algo = new AlgoAprioriTID();
        double minSupport = (double) minOccurrences / eventLog.size();
        List<Itemset> result = algo.runAlgorithm(inputFile, null, minSupport).getLevels().get(eventSize);

        // Send mined data
        result.sort(Comparator.comparing(Itemset::getAbsoluteSupport).reversed());
        for(Itemset element : result) {
           Event event = Event.getDecoded(element.getItems(), encoder);
           output.println(event.serialize() + " " + element.getAbsoluteSupport() + " times.");
        }

        // Delete temporary file
        if(!tmpFile.delete()) throw new IOException("Could not delete temporary file");
    }

    private void close() {
        try {
            if(socket != null) { socket.close(); }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

