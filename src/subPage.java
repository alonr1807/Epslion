import auction.Auction;
import auction.AuctionsGSON;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class subPage extends Thread {
    public int pageIndex;
    public AuctionsGSON auctionGSON;
    public URL pageURL;

    public subPage(int pageIndex) {
        this.pageIndex = pageIndex;
        try {
            pageURL = new URL("https://api.hypixel.net/v2/skyblock/auctions?page=" + pageIndex);
        } catch (Exception e) {
            System.err.println("Error creating URL: " + e.getMessage());
        }
    }

    public void getGSON() {
        try {
            URLConnection conn = pageURL.openConnection();
            conn.connect();
            String responseBody = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                    .lines()
                    .collect(Collectors.joining());
            auctionGSON = new Gson().fromJson(responseBody, AuctionsGSON.class);

        } catch (Exception e) {
            System.err.println("Error creating URL: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            getGSON();

            for(Auction a: auctionGSON.auctions) {
                Main.currAuctions.put((a.uuid), a);
            }

        } catch (Exception e) {
            System.err.println("Error creating URL: " + e.getMessage());
        }
    }

}
