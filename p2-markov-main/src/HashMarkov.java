import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMarkov implements MarkovInterface {
    protected String[] myWords;		
	protected Random myRandom;		
	protected int myOrder;	
    protected HashMap<Object, List<String>> myMap;

    public HashMarkov(int order){
		myOrder = order;
		myRandom = new Random();
		myMap = new HashMap<>();
	}

    @Override
	public void setTraining(String text){
		myWords = text.split("\\s+");
        myMap.clear();
        for (int i = 0; i < myWords.length - myOrder; i ++) {
        	WordGram wgrm = new WordGram(myWords, i, myOrder);
			if (!myMap.containsKey(wgrm)) {
				myMap.put(wgrm, new ArrayList<>());
			}
			myMap.get(wgrm).add(myWords[i + myOrder]);
		}
    }

    @Override 
	public List<String> getFollows(WordGram wgram) {
		List<String> follows = myMap.getOrDefault(wgram, new ArrayList<String>());
		return follows;
	}

	private String getNext(WordGram wgram) {
		List<String> follows = getFollows(wgram);
		if (follows.size() == 0) {
			int randomIndex = myRandom.nextInt(myWords.length);
			follows.add(myWords[randomIndex]);
		}
		int randomIndex = myRandom.nextInt(follows.size());
		return follows.get(randomIndex);
	}

	@Override
	public String getRandomText(int length){
		ArrayList<String> randomWords = new ArrayList<>(length);
		int index = myRandom.nextInt(myWords.length - myOrder + 1);
		WordGram current = new WordGram(myWords,index,myOrder);
		randomWords.add(current.toString());

		for(int k=0; k < length-myOrder; k += 1) {
			String nextWord = getNext(current);
			randomWords.add(nextWord);
			current = current.shiftAdd(nextWord);
		}

		return String.join(" ", randomWords);
	}

    @Override
	public int getOrder() {
		return myOrder;
	}

    @Override
	public void setSeed(long seed) {
		myRandom.setSeed(seed);
	}
}
