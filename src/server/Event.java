package server;

import java.util.Arrays;

public class Event {
    private String context;
    private String name;
    private String address;

    public Event(String context, String name, String address) {
        this.context = context;
        this.name = name;
        this.address = address;
    }

    public String getEncoded(Encoder encoder) {
        int[] numbers = {
                encoder.encode(context, "context"),
                encoder.encode(name, "name"),
                encoder.encode(address, "address")
        };
        Arrays.sort(numbers);
        return numbers[0] + " " + numbers[1] + " " + numbers[2];
    }

    public static Event getDecoded(int[] numbers, Encoder encoder) {
        String context = "undefined", name = "undefined", address = "undefined";
        for(int i = 0; i < 3; i++) {
            switch(encoder.getType(numbers[i])) {
                case "context":
                    context = encoder.decode(numbers[i]);
                    break;
                case "name":
                    name = encoder.decode(numbers[i]);
                    break;
                case "address":
                    address = encoder.decode(numbers[i]);
            }
        }

        return new Event(context, name, address);
    }

    public String serialize() {
        return "User with IP " + address + " performed action " + name + " on " + context;
    }
}
