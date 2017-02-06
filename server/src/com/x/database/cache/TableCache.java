package com.x.database.cache;

/**
 * @author yanfengbing
 * @version 1.0
 * @since 3/17/16 1:54 PM
 */
public class TableCache extends Cache<Table>
{
	public TableCache()
	{
		super(Table.class, new TablePersister());
	}
}
