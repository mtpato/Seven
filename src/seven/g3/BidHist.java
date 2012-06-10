package seven.g3;

import java.util.ArrayList;

public class BidHist {
	private int pIndex;
	private String pName;
	private ArrayList<Integer> bidHistory;
	private int aveBid;
	private double stdBids;
	private int minBid;
	private int maxBid;
	
	
	public BidHist(int pIndex, String pName) {
		this.pIndex = pIndex;
		this.pName = pName;
		maxBid = -1;
		minBid = 1000000;
	}
	
	
	public void addBet(int bet) {
		bidHistory.add(bet);
		
		int sum = 0;
		
		for(int i = 0; i < bidHistory.size(); i ++) {
			if(bidHistory.get(i) > maxBid) maxBid =  bidHistory.get(i);
			if(bidHistory.get(i) < minBid) minBid =  bidHistory.get(i);
			
			sum = sum + bidHistory.get(i);
		}
		
		if(sum > 0) {
			aveBid = sum / bidHistory.size();
		}
		
		//STILL NEED std
		
	}


	public int getpIndex() {
		return pIndex;
	}


	public String getpName() {
		return pName;
	}


	public ArrayList<Integer> getBidHistory() {
		return bidHistory;
	}


	public int getAveBid() {
		return aveBid;
	}
	
	public double getStdBids() {
		return stdBids;
	}


	public int getMinBid() {
		return minBid;
	}


	public int getMaxBid() {
		return maxBid;
	}
	
	
	

}
