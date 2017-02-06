package com.x.database.cache;

import com.x.util.DatabaseUtils;
import com.x.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/17/16 11:30 AM
 */
public class TablePersister implements Persister<Table>
{
	@Override
	public Table find(Key key)
	{
		Logger.debug(this.getClass(), "find from database " + key);
		if(key == null || key.getClass() != TableKey.class) return null;
		TableKey rKey = (TableKey)key;

		EntityManager em = DatabaseUtils.getInstance().getEntityManager();
		try
		{
			em.getTransaction().begin();
			Table row = em.find(Table.class, rKey.toString());
			em.getTransaction().commit();
			if(row != null)
			{
				return row;
			}
		}
		catch(Exception e)
		{
			try
			{
				em.getTransaction().rollback();
			}
			catch(Exception ignored)
			{

			}
			Logger.warn(this.getClass(), "find key " + key, e);
		}
		finally
		{
			DatabaseUtils.getInstance().returnEntityManager();
		}
		return null;
	}

	@Override
	public void save(Table value)
	{
		if(value == null || value.getClass() != Table.class) return;
		EntityManager em = DatabaseUtils.getInstance().getEntityManager();
		try
		{
			em.getTransaction().begin();

			Logger.debug(this.getClass(), "save to database " + value);
			em.merge(value);
			em.flush();
			em.getTransaction().commit();
		}
		catch(Exception e)
		{
			try
			{
				em.getTransaction().rollback();
			}
			catch(Exception ignored)
			{

			}
			Logger.warn(this.getClass(), "save value " + value, e);
		}
		finally
		{
			DatabaseUtils.getInstance().returnEntityManager();
		}
	}

	@Override
	public void remove(Key key)
	{
		Logger.debug(this.getClass(), "remove from database {}", key);
		if(key == null || key.getClass() != TableKey.class)
			return;
		TableKey rKey = (TableKey)key;

		EntityManager em = DatabaseUtils.getInstance().getEntityManager();
		try
		{
			em.getTransaction().begin();
			Table t = em.find(Table.class, rKey.toString());
			if(t != null)
			{
				em.remove(t);
			}
			em.getTransaction().commit();
		}
		catch(Exception e)
		{
			try
			{
				em.getTransaction().rollback();
			}
			catch(Exception ignored)
			{

			}
			Logger.warn(this.getClass(), "remove value {}, {}", key, e);
		}
		finally
		{
			DatabaseUtils.getInstance().returnEntityManager();
		}

	}

	@Override
	public Map<Key, Table> loadAll(int bound, int cacheId)
	{
		EntityManager em = DatabaseUtils.getInstance().getEntityManager();
		try
		{
			String sql = "select t from Table t" +
					" where t.accessTime>=:limit_time";
			TypedQuery<Table> query = em.createQuery(sql, Table.class);

			long now = System.currentTimeMillis();
			Timestamp limit_time = new Timestamp(now - 24L * 60L * 60L * 1000L);
			query.setParameter("limit_time", limit_time.getTime());
			List<Table> list = query.getResultList();
			Map<Key, Table> map = new HashMap<>();
			for(Table t : list)
			{
				Key key = t.key();
				//匹配是否是该Cache的数据 (key.hash(index) 从 0 开始，所以需要加一)
				if(key.hash(bound) == (cacheId % bound))
				{
					map.put(key, t);
				}
			}
			list.clear();
			return map;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DatabaseUtils.getInstance().returnEntityManager();
		}
		return null;
	}
}
