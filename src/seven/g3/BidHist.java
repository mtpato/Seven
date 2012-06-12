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
	private int last;
	private int lastFiveAve;
	private int moneyLeft;//NEED TO KEEP UPDATED
	
	
	public BidHist(int pIndex, String pName) {
		this.pIndex = pIndex;
		this.pName = pName;
		maxBid = -1;
		minBid = 1000000;
		aveBid = 10;
		bidHistory = new ArrayList<Integer>();
		freq = new int[100];//MIGHT NEED TO BE BIGGER IF PEOPLE WILL BET MORE
		moneyLeft = 100;
	}
	
	
	public void addBet(int bet) {
		
		last = bet;//update last 
		bidHistory.add(bet);// add bet to bet list
		
		if(bet > maxBid) maxBid = bet;//update max
		if(bet < minBid) minBid = bet;// update min
		
		getAves();// update averages
		
		freq[bet]++; // update freq
		
		setMode();//update the mode
		
		//STILL NEED std
		
	}
	
	public void setMoney(int money) {
		this.moneyLeft = money;
	}
	
	public void addMoney(int amount) {
		moneyLeft = moneyLeft + amount;
	}


	private void getAves() {
		int sum = 0;		
		
		for(int i = bidHistory.size() - 1; i >= 0; i --) {
			sum = sum + bidHistory.get(i);
			
			if(i == bidHistory.size() - 6) {
				lastFiveAve = sum / 5;
			}
			
		}
		
		if(sum > 0) {
			aveBid = sum / bidHistory.size();
		}
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
	
	public int getLastFiveAve() {
		return lastFiveAve;
	}
	
	public int getLast() {
		return last;
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
