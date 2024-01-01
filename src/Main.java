import auction.Auction;
import com.google.gson.Gson;
import auction.AuctionsGSON;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        System.out.println("hi");

        initialize();
    }

    public static void initialize() {
        try {
            URL url = new URL("https://api.hypixel.net/v2/skyblock/auctions");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            System.out.println(responseCode);



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
            System.out.println(subPage.auctions.size());
            for(Auction a: subPage.auctions) {
                System.out.println(a.item_name);
            }


        } catch (Exception e) {

        }
    }
}
