package com.game.room;

import com.game.Constant;
import com.x.lang.*;
import com.x.lang.Random;
import com.x.wechat.data.User;

import java.io.Serializable;
import java.util.*;

/**
 * Created by fatum on 2017/2/14.
 */
public class PokerBullRoom extends Room
{
	static int NEED_CARD_NUMBER = 5;
	static int PREVIOUS_CARD_NUMBER = 5;
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

	public static class BullResult
	{
		public static BullResult None = new BullResult();

		public Constant.BullType type = Constant.BullType.BullNone;
		public int score = 0;
		public int major = 0;
		public int minor = 0;

		public BullResult()
		{
		}

		public BullResult(Constant.BullType type, int score)
		{
			this.type = type;
			this.score = score;
		}

		public BullResult(Constant.BullType type, int major, int minor)
		{
			this.type = type;
			this.major = major;
			this.minor = minor;
		}

		public int compareTo(BullResult other)
		{
			if(this.type == other.type)
				return this.major - other.major;

			return this.type.getKey() - other.type.getKey();
		}

		//public int a;
	}

	public static class Judgement
	{
		List<Integer> decad = new ArrayList<>();
		List<Integer> bull = new ArrayList<>();

		//Constant.BullType type = Constant.BullType.BullNone;
		//int score = 0;
		//int major = 0;
		//int minor = 0;

		public static Judgement create(List<Integer> decad, List<Integer> bull)
		{
			if(decad.size() != 3)
				return null;

			if(bull.size() != 2)
				return null;

			int total = 0;
			for(Integer i : decad)
			{
				if(bull.contains(i))
					return null;

				total += Constant.PokerCardType.parseDecade(i);
			}

			if(total % 10 != 0)
				return null;

			Judgement j = new Judgement();
			j.decad = decad;
			j.bull = bull;

			return j;
		}

		private Judgement()
		{
		}
		//BullScore score = new BullScore();

		public boolean equals(List<Integer> values)
		{
			if((this.decad.size() + this.bull.size()) != values.size())
				return false;

			for(Integer i : this.decad)
			{
				if(!values.contains(i))
					return false;
			}

			for(Integer i : this.bull)
			{
				if(!values.contains(i))
					return false;
			}

			return true;
		}
	}

	protected String banker = null;
	protected Map<Integer, List<String>> callingOdds = new HashMap<>();
	protected long remainingValue;
	protected Map<String, Long> bettingValue = new HashMap<>();
	protected Map<String, Integer> bettingOdds = new HashMap<>();
	protected Map<String, Integer> priorityValue = new HashMap<>();
	protected Map<String, List<Integer>> currentCards = new HashMap<>();
	protected Map<String, Judgement> previousJudgements = new HashMap<>();

	/// cards
	protected List<Integer> remaining = new LinkedList<>();
	protected List<Integer> used = new ArrayList<>();

	protected boolean isPlay = false;

	// ����ĳ��״̬����Щ��Ϣ/������Ҫ����

	enum State implements Room.State
	{
		WAIT(0, 10),
		//SHUFFLE(0, 10),
		DEAL(1, 10),
		CALL(2, 10),
		RANDOM_CALL(3, 10),
		BET(4, 10),
		FINAL_DEAL(5, 10),
		PRIZE(6, 10),
		OVER(7, 10),
		;

		private int key;
		@Override
		public int getKey()
		{
			return key;
		}
		private int cooldown;
		public int getCooldown()
		{
			return cooldown;
		}

		State(int key, int cooldown)
		{
			this.key = key;
			this.cooldown = cooldown;
		}
	}

	public PokerBullRoom()
	{
		super(111111L, null);
	}

	public void shuffle()
	{
		this.remaining.addAll(this.used);
		Collections.shuffle(this.remaining);
		this.used.clear();
	}

	public void dealOrderly()
	{
		for(int i = 0; i < PREVIOUS_CARD_NUMBER; ++i)
		{
			for(String id : this.players.keySet())
			{
				if(!this.currentCards.containsKey(id))
					this.currentCards.put(id, new ArrayList<>());

				List<Integer> list = this.currentCards.get(id);
				Integer obj = this.remaining.get(0);
				this.remaining.remove(obj);
				list.add(obj);
				this.used.add(obj);
			}
		}
		// default odds values
		for(User o : this.players.values())
		{
			setBettingOdds(o, this.regulation.defaultBettingOdds());
			//setCallingOdds(o, this.regulation.defaultCallingOdds());
		}
	}

	/*public void deal()
	{

	}*/

	public void dealFinally()
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

	public void call()
	{
		int previous = -1;
		List<String> list = null;
		for(Map.Entry<Integer, List<String>> entry : this.callingOdds.entrySet())
		{
			if(previous < entry.getKey())
			{
				previous = entry.getKey();
				list = entry.getValue();
			}
		}

		if(list == null || list.isEmpty())
		{
			list = new ArrayList<>();
			list.addAll(this.players.keySet());
		}

		String key = list.get(Random.randomInt(100) % list.size());
		if(key == null)
		{
			/// error
		}

		User player = this.players.get(key);
		if(player == null)
		{
			/// error
		}

		this.banker = key;
	}

	/*public void callRandomly(List<User> players)
	{
		User player = players.get(com.x.lang.Random.randomInt(100) % players.size());
		if(player == null)
		{
			// error
		}

		this.banker = player.getKey();
	}*/

	public void bet(User player, int odds)
	{
		this.bettingOdds.put(player.getKey(), odds);
	}

	public void prize()
	{
		BullResult result = BullResult.None;
		Judgement judgement = this.previousJudgements.get(this.banker);
		if(judgement != null)
		{
			result = judgeResult(judgement);
		}
		else
		{
			List<Integer> list = this.currentCards.get(this.banker);
			result = judgeResult(list);
		}

		// settlement
		for(Map.Entry<String, List<Integer>> entry : this.currentCards.entrySet())
		{
			if(entry.getKey() != this.banker)
			{
				BullResult r = BullResult.None;
				Judgement j = this.previousJudgements.get(this.banker);
				if(j != null)
				{
					r = judgeResult(j);
				}
				else
				{
					List<Integer> list = this.currentCards.get(this.banker);
					r = judgeResult(list);
				}

				int o = this.bettingOdds.get(entry.getKey());
				int c = result.compareTo(r);

				if(c > 0) // banker win
				{
					int v = regulation.prize(true, o, r.type.getKey());
				}
				else // banker lose
				{
					int v = regulation.prize(true, o, r.type.getKey());
				}

			}
		}
	}

	public void over()
	{
		this.banker = null;
		this.currentCards.clear();
		this.previousJudgements.clear();

		shuffle();
	}

	public BullResult judgeResult(Judgement judgement)
	{
		int major = Constant.PokerCardType.CardInvalid.getKey();
		int minor = Constant.PokerCardType.CardInvalid.getKey();
		int score = -1;
		for(int i : judgement.decad)
		{
			minor = major == Constant.PokerCardType.CardInvalid.getKey() ? Constant.PokerCardType.CardInvalid.getKey() : Math.min(major, Math.max(minor, i));
			major = Math.max(major, i);
		}
		for(int i : judgement.bull)
		{
			minor = major == Constant.PokerCardType.CardInvalid.getKey() ? Constant.PokerCardType.CardInvalid.getKey() : Math.min(major, Math.max(minor, i));
			major = Math.max(major, i);
			score += Constant.PokerCardType.parseDecade(i);
		}

		BullResult result = BullResult.None;
		result.major = major;
		result.minor = minor;
		result.score = score;

		if(isBoomBull(judgement))
		{
			result.type = Constant.BullType.BoomBull;
		}
		else if(isSuitBull(null, result))
		{
			result.type = Constant.BullType.SuitBull;
		}

		result.type = Constant.BullType.parse(score);

		return result;
	}

	/*
	 *
	 */
	public BullResult judgeResult(List<Integer> five)
	{
		BullResult result = BullResult.None;

		if(five != null && five.size() == NEED_CARD_NUMBER)
		{
			int major = Constant.PokerCardType.CardInvalid.getKey();
			int minor = Constant.PokerCardType.CardInvalid.getKey();
			int sum = 0;
	        Map<Integer, List<Integer>> map = new HashMap<>();
		    for(int i : five)
		    {
			    int decade = Constant.PokerCardType.parseDecade(i) % 10; // ����Ϊ�˰�10���0
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

	public boolean isBoomBull(Judgement judgement)
	{
		return false;
	}

	public boolean isBoomBull(Map<Integer, List<Integer>> map, BullResult result)
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

	public boolean isSuitBull(Judgement judgement)
	{
		return false;
	}

	public boolean isSuitBull(Map<Integer, List<Integer>> map, BullResult result)
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
		this.countingDelta++;
		// ������Щ�������ĳ�״̬���ܺ�
		if(this.state == State.WAIT && this.countingDelta >= State.WAIT.getCooldown())
		{
			this.countingDelta = 0;
			this.state = State.DEAL;
			dealOrderly();
		}
		else if(this.state == State.DEAL && this.countingDelta >= State.DEAL.getCooldown())
		{
			this.countingDelta = 0;
		}
		else if(this.state == State.CALL && this.countingDelta >= State.CALL.getCooldown())
		{
			this.countingDelta = 0;
		}
		else if(this.state == State.RANDOM_CALL && this.countingDelta >= State.RANDOM_CALL.getCooldown())
		{
			this.countingDelta = 0;
		}
		else if(this.state == State.BET && this.countingDelta >= State.BET.getCooldown())
		{
			this.countingDelta = 0;
		}
		else if(this.state == State.FINAL_DEAL && this.countingDelta >= State.FINAL_DEAL.getCooldown())
		{
			this.countingDelta = 0;
		}
		else if(this.state == State.PRIZE && this.countingDelta >= State.PRIZE.getCooldown())
		{
			this.countingDelta = 0;
		}
		else if(this.state == State.OVER && this.countingDelta >= State.OVER.getCooldown())
		{
			this.countingDelta = 0;
		}

	}

	@Override
	public int getMaxNumber()
	{
		return 0;
	}

	@Override
	public boolean canPlay()
	{
		return getPlayingNumber() > 2;
	}

	@Override
	public void clear()
	{
		super.clear();
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

	// ��ʼ��Ϸÿ������һ��
	public void setBettingOdds(User player, int odds)
	{
		this.bettingOdds.put(player.getKey(), odds);
	}

	/*public void setCallingOdds(User player, int odds)
	{
		this.callingOdds.put(player.getKey(), odds);
	}*/

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
		System.out.println("����");

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
