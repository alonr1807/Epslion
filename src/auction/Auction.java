package auction;

import java.util.List;

public class Auction {

    public String uuid;
    public String auctioneer;
    public String profile_id;
    //public List<String> coop = null;
    public Long start;
    public Long end;
    public String item_name;
    public String item_lore;
    public String extra;
    public String category;
    public String tier;
    public Long starting_bid;
    public Long last_updated;
    public String item_bytes;
    public Boolean claimed;
    public List<Object> claimed_bidders = null;
    public Long highest_bid_amount;
    public Boolean bin;
    public List<Object> bids = null;

}
