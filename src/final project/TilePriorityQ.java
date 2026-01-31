package finalproject;

import java.util.ArrayList;
import java.util.Arrays;


import finalproject.system.Tile;

public class TilePriorityQ {
	// TODO level 3: Add fields that can help you implement this data type
	private ArrayList<Tile> Q;

	private Tile first;

	private Graph graph;

	// TODO level 3: implement the constructor for the priority queue
	public TilePriorityQ (ArrayList<Tile> vertices) {
		Q = new ArrayList<>(25);
		ArrayList<Tile> temp = new ArrayList<>(25);
		Q.add(0, null);
		for (Tile t:vertices){
			if (t.costEstimate != 0){
				Q.add(t);
			}
			else if (t.costEstimate == 0){
				Q.add(t);
				first = t;
			}
		}
		for (int i = Q.size()/2; i>= 1; i--){
			downheap(Q, i, Q.size());
		}
	}

	public void downheap(ArrayList<Tile> array, int cur, int total) {
		Tile t = array.get(cur);
		if (2 * cur + 1 < array.size() && (array.get(cur).costEstimate > array.get(2 * cur).costEstimate || array.get(cur).costEstimate > array.get(2 * cur + 1).costEstimate)) {
			if (array.get(2 * cur).costEstimate > array.get(2 * cur + 1).costEstimate) {
				Tile child = array.get(2 * cur + 1);
				array.set(2 * cur + 1, t);
				array.set(cur, child);
				downheap(array, array.indexOf(t), array.size());
			} else {
				Tile child = array.get(2 * cur);
				array.set(2 * cur, t);
				array.set(cur, child);
				downheap(array, array.indexOf(t), array.size());
			}
		}
		if (2 * cur < array.size() && array.get(cur).costEstimate > array.get(2 * cur).costEstimate){
			Tile child = array.get(2 * cur);
			array.set(2 * cur, t);
			array.set(cur, child);
			downheap(array, array.indexOf(t), array.size());
		}
	}
	
	// TODO level 3: implement remove min as seen in class
	public Tile removeMin() {
		Tile begin = Q.get(1);
		if (Q.size() == 2){
			Q.remove(begin);
			return begin;
		}
		Q.set(1, Q.remove(Q.size()-1));
		Tile cur = Q.get(1);
		Tile temp;
		int curPos = Q.indexOf(cur);
		if (curPos == 1 && Q.size() == 3){
			if (cur.costEstimate > Q.get(2* curPos).costEstimate){
				temp = Q.get(2* curPos);
				Q.set(2*curPos, cur);
				Q.set(curPos, temp);
				curPos = Q.indexOf(cur);
				return begin;
			}
		}
		while(2* curPos < Q.size()-1 && (cur.costEstimate > Q.get(2* curPos).costEstimate || cur.costEstimate > Q.get(2* curPos + 1).costEstimate)){
			if (Q.get(2 * curPos).costEstimate > Q.get(2 * curPos + 1).costEstimate){
				temp = Q.get(2* curPos + 1);
				Q.set(2*curPos+1, cur);
				Q.set(curPos, temp);
				curPos = Q.indexOf(cur);
			}
			else {
				temp = Q.get(2* curPos);
				Q.set(2*curPos, cur);
				Q.set(curPos, temp);
				curPos = Q.indexOf(cur);
			}
		}
		return begin;
	}
	
	// TODO level 3: implement updateKeys as described in the pdf
	public void updateKeys(Tile t, Tile newPred, double newEstimate) {
		double oldEst = t.costEstimate;
		if (oldEst > newEstimate){
			t.costEstimate = newEstimate;
			arrangeUpKeys(Q, t, newPred);
		}
		else {
			t.costEstimate = newEstimate;
			arrangeDownKeys(Q, t, newPred);
		}
	}
	public void arrangeUpKeys(ArrayList<Tile> array, Tile t, Tile newPred){
		int tPos = array.indexOf(t);
		if (tPos/2 != 0 && array.get(tPos/2).costEstimate > t.costEstimate){
			Tile parent = array.get(tPos/2);
			if (parent == newPred){
				return;
			}
			array.set(tPos, parent);
			array.set(tPos/2, t);
			if (parent != newPred){
				arrangeUpKeys(array, t, newPred);
			}
		}
	}
	public void arrangeDownKeys(ArrayList<Tile> array, Tile t, Tile newPred){
		int tPos = array.indexOf(t);

		if (2 * tPos + 1 < array.size() && (array.get(tPos).costEstimate > array.get(2 * tPos).costEstimate || array.get(tPos).costEstimate > array.get(2 * tPos + 1).costEstimate)) {
			if (array.get(2 * tPos).costEstimate > array.get(2 * tPos + 1).costEstimate) {
				Tile child = array.get(2 * tPos + 1);
				array.set(2 * tPos + 1, t);
				array.set(tPos, child);
				if (child != newPred){
					arrangeDownKeys(array, t, newPred);
				}
			} else {
				Tile child = array.get(2 * tPos);
				array.set(2 * tPos, t);
				array.set(tPos, child);
				if (child != newPred){
					arrangeDownKeys(array, t, newPred);
				}
			}
		}
		if (2 * tPos < array.size() && array.get(tPos).costEstimate > array.get(2 * tPos).costEstimate){
			Tile child = array.get(2 * tPos);
			array.set(2 * tPos, t);
			array.set(tPos, child);
			if (child != newPred){
				arrangeDownKeys(array, t, newPred);
			}
		}
	}
	public ArrayList<Tile> getQ(){
		return this.Q;
	}
	
}
