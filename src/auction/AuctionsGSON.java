package auction;

import java.util.List;

public class AuctionsGSON {

    public Boolean success;
    public Integer page;
    public Integer totalPages;
    public Integer totalAuctions;
    public Long lastUpdated;
    public List<Auction> auctions = null;

}
