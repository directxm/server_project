package com.x.wechat.data.cache;

import com.x.database.cache.Key;
import com.x.database.cache.Persister;
import com.x.logging.Logger;
import com.x.wechat.data.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名: UserPersister </br>
 * 描述: 游戏角色持久化操作 </br>
 * 开发人员: fatum </br>
 * 创建时间: 16-10-26 </br>
 * 发布版本: V1.0 </br>
 */
/*public class UserPersister implements Persister<User>
{
	private static UserPersister __INSTANCE;
	private String _source;
	private Map<String, Object> _properties;
	private EntityManagerFactory entityManagerFactory;

	public UserPersister(String source, Map<String, Object> properties)
	{
		this._source = source;
		this._properties = properties;

		if(entityManagerFactory != null)
		{
			entityManagerFactory.close();
			entityManagerFactory = null;
		}
		entityManagerFactory = Persistence.createEntityManagerFactory(_source, _properties);
	}

	@Override
	public User find(Key key)
	{
		Logger.debug(this.getClass(), "find from database " + key);
		if(key == null || key.getClass() != UserKey.class) return null;
		UserKey rKey = (UserKey)key;

		EntityManager em = getEntityManager();
		try
		{
			em.getTransaction().begin();
			User row = em.find(User.class, rKey.toString());
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
			returnEntityManager();
		}
		return null;
	}

	@Override
	public void save(User value)
	{
		if(value == null || value.getClass() != User.class) return;
		EntityManager em = getEntityManager();
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
			returnEntityManager();
		}
	}

	@Override
	public void remove(Key key)
	{
		Logger.debug(this.getClass(), "remove from database {}", key);
		if(key == null || key.getClass() != UserKey.class)
			return;
		UserKey rKey = (UserKey)key;

		EntityManager em = getEntityManager();
		try
		{
			em.getTransaction().begin();
			User t = em.find(User.class, rKey.toString());
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
			returnEntityManager();
		}
	}

	@Override
	public Map<Key, User> loadAll(int bound, int cacheId)
	{
		EntityManager em = getEntityManager();
		try
		{
			String sql = "select t from User t" +
					" where t.accessTime>=:limit_time";
			TypedQuery<User> query = em.createQuery(sql, User.class);

			long now = System.currentTimeMillis();
			Timestamp limit_time = new Timestamp(now - 24L * 60L * 60L * 1000L);
			query.setParameter("limit_time", limit_time.getTime());
			List<User> list = query.getResultList();
			Map<Key, User> map = new HashMap<>();
			for(User t : list)
			{
				Key key = t.key();
				// 匹配是否是该Cache的数据 (key.hash(index) 从 0 开始，所以需要加一)
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
			returnEntityManager();
		}
		return null;
	}



	private static final ThreadLocal<EntityManager> __ENTITYMANAGER = new ThreadLocal<EntityManager>()
	{
		@Override
		protected EntityManager initialValue()
		{
			super.initialValue();
			EntityManager em = null;
			try
			{
				em = __INSTANCE.createEntityManager();
			}
			catch(Exception e)
			{
				Logger.error("ThreadLocal<EntityManager>.initialValue", e);
			}
			//Logger.debug("TreadLocal<EntityManager> create one");
			return em;
		}

		@Override
		public void remove()
		{
			EntityManager em = get();
			if(em != null)
			{
				em.close();
			}
			//Logger.debug("TreadLocal<EntityManager> close one");
			super.remove();
		}
	};

	protected EntityManager createEntityManager()
	{
		return entityManagerFactory.createEntityManager();
	}

	protected EntityManager getEntityManager()
	{
		return __ENTITYMANAGER.get();
	}

	protected void returnEntityManager()
	{
		/*try
		{
			__ENTITYMANAGER.get().clear();
		}
		catch(Exception e)
		{

		}*/
		/*__ENTITYMANAGER.remove();
	}

	protected void close()
	{
		entityManagerFactory.close();
	}
}*/
