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
	}
	
	
	public void addBet(int bet) {
		// add the new bet
		//calc min max and average and std
		
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
