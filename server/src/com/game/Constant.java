package com.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fatum on 2017/2/15.
 */
public interface Constant
{
	int getKey();

	enum BullType implements Constant
	{
		BullNone(-1),
		BullOne(1),
		BullTwo(2),
		BullThree(3),
		BullFour(4),
		BullFive(5),
		BullSix(6),
		BullSeven(7),
		BullEight(8),
		BullNine(9),
		DoubleBull(10),
		BoomBull(11),
		SuitBull(12),;

		private final static Map<Integer, BullType> map = new HashMap<>();
		static
		{
			for(BullType o : BullType.values())
				map.put(o.key, o);
		}
		public static BullType parse(int key)
		{
			return map.get(key);
		}

		private final int key;
		BullType(int key)
		{
			this.key = key;
		}

		@Override
		public int getKey()
		{
			return this.key;
		}

		@Override
		public String toString()
		{
			if(key == BullNone.getKey())
				return "ûţ";
			else if(key == BullOne.getKey())
				return "ţһ";
			else if(key == BullTwo.getKey())
				return "ţ��";
			else if(key == BullThree.getKey())
				return "ţ��";
			else if(key == BullFour.getKey())
				return "ţ��";
			else if(key == BullFive.getKey())
				return "ţ��";
			else if(key == BullSix.getKey())
				return "ţ��";
			else if(key == BullSeven.getKey())
				return "ţ��";
			else if(key == BullEight.getKey())
				return "ţ��";
			else if(key == BullNine.getKey())
				return "ţ��";
			else if(key == DoubleBull.getKey())
				return "ţţ";
			else if(key == BoomBull.getKey())
				return "ը��ţ";
			else if(key == SuitBull.getKey())
				return "�廨ţ";

			return "δ֪";
		}
	}

	// ��ɫ��С����Ϊ���ҡ����ҡ�÷������Ƭ
	enum PokerCardSuit implements Constant
	{
		Spade(1 << 3), // ����
		Heart(1 << 2), // ����
		Club(1 << 1), // ÷��
		Diamond(1 << 0),// ����
		ALL(Spade.key | Heart.key | Club.key | Diamond.key)
		;

		private final static Map<Integer, PokerCardSuit> map = new HashMap<>();
		static
		{
			for(PokerCardSuit o : PokerCardSuit.values())
				map.put(o.key, o);
		}
		public static PokerCardSuit parse(int key)
		{
			return map.get(key);
		}

		private final int key;
		PokerCardSuit(int key)
		{
			this.key = key;
		}

		@Override
		public int getKey()
		{
			return key;
		}

		@Override
		public String toString()
		{
			if(key == Spade.getKey())
				return "��";
			else if(key == Heart.getKey())
				return "��";
			else if(key == Club.getKey())
				return "��";
			else if(key == Diamond.getKey())
				return "Ƭ";

			return "δ֪";
		}
	}

	enum PokerCardType implements Constant
	{
		CardInvalid(-1),
		CardA(1 << 10),
		Card2(2 << 10),
		Card3(3 << 10),
		Card4(4 << 10),
		Card5(5 << 10),
		Card6(6 << 10),
		Card7(7 << 10),
		Card8(8 << 10),
		Card9(9 << 10),
		Card10(10 << 10),
		CardJ(11 << 10),
		CardQ(12 << 10),
		CardK(13 << 10)
		;

		private final static Map<Integer, PokerCardType> map = new HashMap<>();
		static
		{
			for(PokerCardType o : PokerCardType.values())
				map.put(o.key, o);
		}
		public static PokerCardType parse(int key)
		{
			return map.get(key);
		}

		private final int key;
		PokerCardType(int key)
		{
			this.key = key;
		}

		// ����ɫ����ת������Ȼ��
		public static int parseInteger(int card)
		{
			return (card >> 10);
		}

		// ����ɫ����ת����ʮ������ֵ
		public static int parseDecade(int card)
		{
			int value = parseInteger(card);
			return value > 10 ? 10 : value;
		}

		// ����ɫ����ת��������
		public static int parseType(int card)
		{
			return (parseInteger(card) << 10);
		}

		// ����ɫ����ת���ɻ�ɫ
		public static int parseSuit(int card)
		{
			return (card & PokerCardSuit.ALL.getKey());
		}

		// ֵ�ͻ��ϳ�һ����
		// �ϳ������value������
		/*public static int toPokerCard(int value, int suit)
		{
			return (1 << (10 + value) | suit);
		}*/

		// ���ͺͻ��ϳ�һ����
		public static int toPokerCard(PokerCardType type, PokerCardSuit suit)
		{
			return (type.getKey() | suit.getKey());
		}

		@Override
		public int getKey()
		{
			return key;
		}

		@Override
		public String toString()
		{
			return " " +
					parseType(key) + " ";
		}
	}

	public static void main(String[] args)
	{
		int card = PokerCardType.toPokerCard(PokerCardType.CardK, PokerCardSuit.Spade);
		int type = PokerCardType.parseType(card);
		PokerCardType ct = PokerCardType.parse(type);
		int suit = PokerCardType.parseSuit(card);
		PokerCardSuit cs = PokerCardSuit.parse(suit);
		int i = PokerCardType.parseInteger(card);
		int d = PokerCardType.parseDecade(card);

	}
}
