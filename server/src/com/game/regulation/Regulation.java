package com.game.regulation;

import com.game.room.Room;
import com.x.wechat.data.User;

/**
 * Created by fatum on 2017/2/27.
 * ϣ������Ϸ�Ĺ���д�����棬�����˿�ʼ���۶��ٷ�ɶ��
 */
public interface Regulation
{
	void start(User player, Room room);
	void over(User player, Room room);

	int bet();
	int profit();
	int prize(boolean win, int odds, int bull);

	int defaultBettingOdds();
	int defaultCallingOdds();
}
