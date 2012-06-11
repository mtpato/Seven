package seven.g3;

import java.util.ArrayList;

public class BidHist {
	private int pIndex;//player index in list
	private String pName;//player name
	private ArrayList<Integer> bidHistory;//player bidhistory
	private int aveBid;//average bid
	private double stdBids;// bid std
	private int minBid;//min bid
	private int maxBid;// max bid
	private int[] freq; // the number of times each bid was made
	private int mode;// most common bid
	
	
	public BidHist(int pIndex, String pName) {
		this.pIndex = pIndex;
		this.pName = pName;
		maxBid = -1;
		minBid = 1000000;
		bidHistory = new ArrayList<Integer>();
		freq = new int[100];//MIGHT NEED TO BE BIGGER IF PEOPLE WILL BET MORE 
	}
	
	
	public void addBet(int bet) {
		bidHistory.add(bet);
		
		if(bet > maxBid) maxBid = bet;
		if(bet < minBid) minBid = bet;
		
		int sum = 0;		
		
		for(int i = 0; i < bidHistory.size(); i ++) {
			sum = sum + bidHistory.get(i);
		}
		
		if(sum > 0) {
			aveBid = sum / bidHistory.size();
		}
		
		freq[bet]++;
		
		setMode();
		
		//STILL NEED std
		
	}


	private void setMode() {
		
		int most = 0;
		
		for(int i = 0; i < freq.length; i ++) {
			if (freq[i] > most) {
				most = freq[i];
			}
		}
		
		mode = most;
		
	}
	
	public int getMode() {
		return mode;
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
