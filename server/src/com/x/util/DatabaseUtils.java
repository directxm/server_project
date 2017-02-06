package com.x.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

/**
 * @author yanfengbing
 * @since 13-9-23 PM3:32
 */
public class DatabaseUtils
{
	public static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);
	private static DatabaseUtils __INSTANCE;
	private String _source;
	private Map<String, Object> _properties;
	private EntityManagerFactory entityManagerFactory;

	public static DatabaseUtils getInstance()
	{
		return __INSTANCE;
	}

	public static void setInstance(DatabaseUtils database)
	{
		__INSTANCE = database;
	}

	protected DatabaseUtils(String source)
	{
		this._source = source;
	}

	protected DatabaseUtils(String source, Map<String, Object> properties)
	{
		this._source = source;
		this._properties = properties;
	}

	protected void setProperties(Map<String, Object> properties)
	{
		this._properties = properties;
	}

	protected void init()
	{
		if(entityManagerFactory != null)
		{
			entityManagerFactory.close();
			entityManagerFactory = null;
		}
		entityManagerFactory = Persistence.createEntityManagerFactory(_source, _properties);
	}

	protected EntityManager createEntityManager()
	{
		return entityManagerFactory.createEntityManager();
	}

	public EntityManager getEntityManager()
	{
		return __ENTITYMANAGER.get();
	}

	public void returnEntityManager()
	{
		/*try
		{
			__ENTITYMANAGER.get().clear();
		}
		catch(Exception e)
		{

		}*/
		__ENTITYMANAGER.remove();
	}

	public void close()
	{
		entityManagerFactory.close();
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
				em = DatabaseUtils.getInstance().createEntityManager();
			}
			catch(Exception e)
			{
				LOGGER.error("ThreadLocal<EntityManager>.initialValue", e);
			}
			//LOGGER.debug("TreadLocal<EntityManager> create one");
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
			//LOGGER.debug("TreadLocal<EntityManager> close one");
			super.remove();
		}
	};
}
