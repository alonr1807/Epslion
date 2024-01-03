import auction.Auction;
import com.google.gson.Gson;
import auction.AuctionsGSON;
import com.google.gson.GsonBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

public class Main {
    public static HashMap<String, Auction> currAuctions = new HashMap<String, Auction>();


    public static void main(String[] args) {
        System.out.println("hi");

        initialize();
        continuous();
    }

    public static void continuous() {
        while (true) {
            try {

                HashMap<String, Auction> oldAuctions = new HashMap<String, Auction>(currAuctions);
                currAuctions = new HashMap<String, Auction>();
                Thread.sleep(60000);
                fetchAuctions();
                HashMap<String, Auction> newAuctions = new HashMap<String, Auction>(currAuctions);
                Iterator<String> iterator = newAuctions.keySet().iterator();
                while (iterator.hasNext()) {
                    String id = iterator.next();
                    if (oldAuctions.containsKey(id)) {
                        iterator.remove();
                    }
                }
                //System.out.println("new\n\n\n" + newAuctions);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String filePath = "output.txt";
                // Write ArrayList content to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    newAuctions.keySet().forEach(key -> {
                        System.out.println("{/viewauction " + key);
                        try {
                            String jsonString = gson.toJson(newAuctions.get(key));
                            byte[] decodedBytes = Base64.getDecoder().decode(newAuctions.get(key).item_bytes);

                            // Convert decoded bytes to a String (assuming it was originally a UTF-8 encoded string)
                            String decodedString = new String(decodedBytes);
                            writer.write(decodedString);
                            writer.newLine();
                            writer.write(jsonString);
                            writer.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                }
            } catch (Exception e) {

            }

        }
    }

    public static void fetchAuctions() {
        try {
            URL url = new URL("https://api.hypixel.net/v2/skyblock/auctions");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            String responseBody = "";
            AuctionsGSON auctionGSON = null;
            if (responseCode == 200) {

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBody += inputLine;
                }

                auctionGSON = new Gson().fromJson(responseBody, AuctionsGSON.class);

                System.out.println("TotalPages: " + auctionGSON.totalPages);

                subPage[] pageThreads = new subPage[auctionGSON.totalPages + 1];

                for (int i = 0; i <= auctionGSON.totalPages; i++) {
                    final int index = i;
                    pageThreads[index] = new subPage(index);
                    pageThreads[index].start();
                }

                for (subPage page : pageThreads) {
                    page.join();
                }
            }
        } catch (Exception e) {
        }
    }

    public static void initialize() {
        fetchAuctions();
        System.out.println("initializing done");
    }
}
