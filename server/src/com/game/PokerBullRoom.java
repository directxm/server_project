package com.game;

import com.x.wechat.data.User;

import java.io.Serializable;
import java.util.*;

/**
 * Created by fatum on 2017/2/14.
 */
public class PokerBullRoom extends Room
{
	static int NEED_CARD_NUMBER = 5;
	static int MIN_PLAYER_NUMBER = 2;
	static int MAX_PLAYER_NUMBER = 6;

	static class Pair implements Serializable
	{
		public int count;
		public int value;

		public Pair(int count, int value)
		{
			this.count = count;
			this.value = value;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;

			Pair pair = (Pair)o;

			if(count != pair.count) return false;
			return value == pair.value;

		}

		@Override
		public int hashCode()
		{
			int result = count;
			result = 31 * result + value;
			return result;
		}
	}

	public static class BullScore
	{
		public static BullScore Zero = new BullScore();

		public Constant.BullType type = Constant.BullType.BullNone;
		public int score = 0;
		public int major = 0;
		public int minor = 0;

		public BullScore()
		{
		}

		public BullScore(Constant.BullType type, int score)
		{
			this.type = type;
			this.score = score;
		}

		public BullScore(Constant.BullType type, int major, int minor)
		{
			this.type = type;
			this.major = major;
			this.minor = minor;
		}


		//public int a;
	}

	protected String banker = null;
	protected long remainingValue;
	protected Map<String, Long> bettingValue = new HashMap<>();
	protected Map<String, Integer> odds = new HashMap<>();
	protected Map<String, Integer> priorityValue = new HashMap<>();
	protected Map<String, List<Integer>> currentCards = new HashMap<>();

	protected List<Integer> remaining = new LinkedList<>();
	protected List<Integer> used = new ArrayList<>();

	protected boolean isPlay = false;

	public void shuffle()
	{
		this.remaining.addAll(this.used);
		Collections.shuffle(this.remaining);
		this.used.clear();
	}

	public void dealOrderly()
	{
		for(int i = 0; i < NEED_CARD_NUMBER; ++i)
		{
			for(String id : players.keySet())
			{
				if(!this.currentCards.containsKey(id))
					this.currentCards.put(id, new ArrayList<>());

				List<Integer> list = currentCards.get(id);
				Integer obj = this.remaining.get(0);
				this.remaining.remove(obj);
				list.add(obj);
				this.used.add(obj);
			}
		}
	}

	public void deal()
	{

	}

	/*
	 *
	 */
	public BullScore judgeResult(List<Integer> five)
	{
		BullScore result = BullScore.Zero;

		if(five != null && five.size() == NEED_CARD_NUMBER)
		{
			int major = Constant.PokerCardType.CardInvalid.getKey();
			int minor = Constant.PokerCardType.CardInvalid.getKey();
			int sum = 0;
	        Map<Integer, List<Integer>> map = new HashMap<>();
		    for(int i : five)
		    {
			    int decade = Constant.PokerCardType.parseDecade(i) % 10;
		        sum += decade;
			    //int count = 0;
			    if(!map.containsKey(decade))
				    map.put(decade, new ArrayList<>());

			    List<Integer> list = map.get(decade);
			    list.add(i);

			    minor = major == Constant.PokerCardType.CardInvalid.getKey() ? Constant.PokerCardType.CardInvalid.getKey() : Math.min(major, Math.max(minor, i));
				major = Math.max(major, i);
		    }
	        int point = sum % 10;

			if(isBoomBull(null, result))
				return result;
			if(isSuitBull(null, result))
				return result;

			boolean exists = false;
		    for(int i : map.keySet())
		    {
		        int other = (10 + point - i) % 10;
		        if (map.containsKey(other))
		        {
			        List<Integer> list = map.get(other);
			        int count = list.size();
		            if ((other == i && count >= 2) || (other != i && count >= 1))
		            {
			            int one =  Collections.max(list);
			            int two = Collections.max(map.get(i));
			            System.out.println("--------------- major = " + Constant.PokerCardType.parseInteger(Math.max(one, two)) + ":" + Math.max(one, two)
					            + " --------------- minor = " + Constant.PokerCardType.parseInteger(Math.min(one, two)) + ":" + Math.min(one, two));
			            break;
		            }
		        }
		    }

			/*int major = Constant.PokerCardType.CardInvalid.getKey();
			int minor = Constant.PokerCardType.CardInvalid.getKey();
			int sum = 0;
	        Map<Pair, Integer> map = new HashMap<>();
		    for(int i : five)
		    {
			    int decade = Constant.PokerCardType.parseDecade(i);
		        sum += decade;
			    int count = 0;
			    if(map.containsKey(decade))
				    count = map.get(decade);

			    map.put(new Pair(decade, i), ++count);

			    minor = major == Constant.PokerCardType.CardInvalid.getKey() ? Constant.PokerCardType.CardInvalid.getKey() : Math.min(major, Math.max(minor, i));
				major = Math.max(major, i);
		    }
	        int point = sum % 10;

		    for(Pair i : map.keySet())
		    {
		        int other = (10 + point - i.decade) % 10;
		        if (map.containsKey(other))
		        {
			        int count = map.get(other);
		            if ((other == i.decade && count >= 2) || (other != i.decade && count >= 1))
		            {
			            return new BullScore(Constant.BullType.parse(point == 0 ? 10 : point), Math.max(other, i.value), Math.min(other, i.value));
		            }
		        }
		    }
			return new BullScore(Constant.BullType.BullNone, major, minor);*/
		}

		return result;
	}

	public boolean isBoomBull(Map<Integer, List<Integer>> map, BullScore result)
	{
		if(map != null && !map.isEmpty() && result != null)
		{
			int major = Constant.PokerCardType.CardInvalid.getKey();
			int minor = Constant.PokerCardType.CardInvalid.getKey();
			int count = 0;
			for(Map.Entry<Integer, List<Integer>> entry : map.entrySet())
			{
				if(entry.getValue().size() == Constant.PokerCardSuit.values().length)
					major = entry.getKey();
				else
					minor = entry.getKey();

				count += entry.getValue().size();
			}

			if(count == NEED_CARD_NUMBER && major != Constant.PokerCardType.CardInvalid.getKey() && minor != Constant.PokerCardType.CardInvalid.getKey())
			{
				result.type = Constant.BullType.BoomBull;
				result.major = major;
				result.minor = minor;
				return true;
			}
		}
		return false;
	}

	public boolean isSuitBull(Map<Integer, List<Integer>> map, BullScore result)
	{
		if(map != null && map.containsKey(0) && result != null)
		{
			int major = Constant.PokerCardType.CardInvalid.getKey();
			int minor = Constant.PokerCardType.CardInvalid.getKey();

			List<Integer> list = map.get(0);
			for(Integer i : list)
			{
				int value = Constant.PokerCardType.parseInteger(i);
				if(value <= 10)
					return false;

				minor = major == Constant.PokerCardType.CardInvalid.getKey() ? Constant.PokerCardType.CardInvalid.getKey() : Math.min(major, Math.max(minor, i));
				major = Math.max(major, i);
			}

			if(major != Constant.PokerCardType.CardInvalid.getKey() && minor != Constant.PokerCardType.CardInvalid.getKey() && major != minor)
			{
				result.type = Constant.BullType.SuitBull;
				result.major = major;
				result.minor = minor;
				return true;
			}
		}
		return false;
	}

	public List<Integer> generateLow(List<Integer> ref)
	{
		return new ArrayList<>();
	}

	public List<Integer> generateHigh(List<Integer> ref)
	{
		return new ArrayList<>();
	}

	@Override
	public long elapse()
	{
		return 0;
	}

	@Override
	public void tick()
	{

	}

	@Override
	public int getMaxNumber()
	{
		return 0;
	}

	@Override
	public void clear()
	{

	}

	@Override
	public void onEnter(User player)
	{

	}

	@Override
	public void onLeave(User player)
	{

	}

	public boolean isPlay()
	{
		return isPlay;
	}

	public static int next(int x)
	{
		int s, r;
		s = x & (-x);
		r = s + x;
		x = r | (((x ^ r) >> 2) / s);
		return x;
	}

	public static int testNN(List<Integer> five)
	{
        int major = Constant.PokerCardType.CardInvalid.getKey();
		int minor = Constant.PokerCardType.CardInvalid.getKey();
		int sum = 0;
        Map<Integer, List<Integer>> map = new HashMap<>();
	    for(int i : five)
	    {
		    int decade = Constant.PokerCardType.parseDecade(i) % 10;
	        sum += decade;
		    //int count = 0;
		    if(!map.containsKey(decade))
			    map.put(decade, new ArrayList<>());

		    List<Integer> list = map.get(decade);
		    list.add(i);

		    minor = major == Constant.PokerCardType.CardInvalid.getKey() ? Constant.PokerCardType.CardInvalid.getKey() : Math.min(major, Math.max(minor, i));
			major = Math.max(major, i);
	    }
        int point = sum % 10;

		boolean exists = false;
	    for(int i : map.keySet())
	    {
	        int other = (10 + point - i) % 10;
	        if (map.containsKey(other))
	        {
		        List<Integer> list = map.get(other);
		        int count = list.size();
	            if ((other == i && count >= 2) || (other != i && count >= 1))
	            {
		            int one =  Collections.max(list);
		            int two = Collections.max(map.get(i));
		            System.out.println("--------------- major = " + Constant.PokerCardType.parseInteger(Math.max(one, two)) + ":" + Math.max(one, two)
				            + " --------------- minor = " + Constant.PokerCardType.parseInteger(Math.min(one, two)) + ":" + Math.min(one, two));
		            break;
	            }
	        }
	    }

	    return exists ? point : -1;
	}

	public static void main(String[] args)
	{
		System.out.println("ÖÐÎÄ");

		// test enum
		for(int i = 0; i < 200; ++i)
		{
			Constant.PokerCardType t = Constant.PokerCardType.parse(com.x.lang.Random.randomInt(1, 13) << 10);
			Constant.PokerCardSuit s = Constant.PokerCardSuit.parse(1 << com.x.lang.Random.randomInt(0, 3));
			int value = Constant.PokerCardType.toPokerCard(t, s);
			System.out.println("value :" + value + " " + s.toString() + t.toString());
		}

		int a = PokerBullRoom.next(2);

		int c1 = Constant.PokerCardType.CardA.getKey();
		int c2 = Constant.PokerCardType.Card2.getKey();
		int c3 = Constant.PokerCardType.CardK.getKey();
		int c4 = Constant.PokerCardSuit.Diamond.getKey();

		List<Integer> five = new ArrayList<>();
		five.add(Constant.PokerCardType.toPokerCard(Constant.PokerCardType.CardK, Constant.PokerCardSuit.Club));
		five.add(Constant.PokerCardType.toPokerCard(Constant.PokerCardType.CardQ, Constant.PokerCardSuit.Club));
		five.add(Constant.PokerCardType.toPokerCard(Constant.PokerCardType.Card10, Constant.PokerCardSuit.Heart));
		five.add(Constant.PokerCardType.toPokerCard(Constant.PokerCardType.CardJ, Constant.PokerCardSuit.Club));
		five.add(Constant.PokerCardType.toPokerCard(Constant.PokerCardType.CardJ, Constant.PokerCardSuit.Spade));

		int b = testNN(five);



	}
}
