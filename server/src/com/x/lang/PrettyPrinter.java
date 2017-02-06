package com.x.lang;

import java.io.PrintStream;
import java.util.Map;

import static java.lang.String.format;

public final class PrettyPrinter
{
	private static final char BORDER_KNOT = '+';
	private static final char HORIZONTAL_BORDER = '-';
	private static final char VERTICAL_BORDER = '|';

	private static final String DEFAULT_AS_NULL = "(NULL)";

	private final PrintStream out;
	private final String asNull;

	public PrettyPrinter(PrintStream out)
	{
		this(out, DEFAULT_AS_NULL);
	}

	public PrettyPrinter(PrintStream out, String asNull)
	{
		if(out == null)
		{
			throw new IllegalArgumentException("No print stream provided");
		}
		if(asNull == null)
		{
			throw new IllegalArgumentException("No NULL-value placeholder provided");
		}
		this.out = out;
		this.asNull = asNull;
	}

	public void print(String keyName, String valueName, Map map)
	{
		int size = map == null ? 1 : map.size() + 1;
		Object[][] table = new Object[size][2];
		table[0][0] = keyName;
		table[0][1] = valueName;
		int i = 1;
		if(map != null && map.size() > 0)
		{
			for(Object e : map.entrySet())
			{
				Map.Entry entry = (Map.Entry)e;
				table[i][0] = entry.getKey();
				table[i][1] = entry.getValue();
				++i;
			}
		}
		print(table);
	}

	public void print(Object[][] table)
	{
		if(table == null)
		{
			throw new IllegalArgumentException("No tabular data provided");
		}
		if(table.length == 0)
		{
			return;
		}
		final int[] widths = new int[getMaxColumns(table)];
		adjustColumnWidths(table, widths);
		printPreparedTable(table, widths, getHorizontalBorder(widths));
	}

	private void printPreparedTable(Object[][] table, int widths[], String horizontalBorder)
	{
		final int lineLength = horizontalBorder.length();
		out.println(horizontalBorder);
		for(final Object[] row : table)
		{
			if(row != null)
			{
				out.println(getRow(row, widths, lineLength));
				out.println(horizontalBorder);
			}
		}
	}

	private String getRow(Object[] row, int[] widths, int lineLength)
	{
		final StringBuilder builder = new StringBuilder(lineLength).append(VERTICAL_BORDER);
		final int maxWidths = widths.length;
		for(int i = 0; i < maxWidths; i++)
		{
			builder.append(padRight(getCellValue(safeGet(row, i, null)), widths[i])).append(VERTICAL_BORDER);
		}
		return builder.toString();
	}

	private String getHorizontalBorder(int[] widths)
	{
		final StringBuilder builder = new StringBuilder(256);
		builder.append(BORDER_KNOT);
		for(final int w : widths)
		{
			for(int i = 0; i < w; i++)
			{
				builder.append(HORIZONTAL_BORDER);
			}
			builder.append(BORDER_KNOT);
		}
		return builder.toString();
	}

	private int getMaxColumns(Object[][] rows)
	{
		int max = 0;
		for(final Object[] row : rows)
		{
			if(row != null && row.length > max)
			{
				max = row.length;
			}
		}
		return max;
	}

	private void adjustColumnWidths(Object[][] rows, int[] widths)
	{
		for(final Object[] row : rows)
		{
			if(row != null)
			{
				for(int c = 0; c < widths.length; c++)
				{
					final String cv = getCellValue(safeGet(row, c, asNull));
					final int l = getStringLength(cv);
					if(widths[c] < l)
					{
						widths[c] = l;
					}
				}
			}
		}
	}

	private String getCellValue(Object value)
	{
		return value == null ? asNull : value.toString();
	}

	private int getStringLength(String str)
	{
		String v = getCellValue(str);
		int l = 0;
		for(int i = 0; i < v.length(); ++i)
		{
			char c = v.charAt(i);
			if(c <= 255)
			{
				l += 1;
			}
			else
			{
				l += 2;
			}
		}
		return l;
	}

	private static String padRight(String s, int n)
	{
		return format("%1$-" + n + "s", s);
	}

	private static Object safeGet(Object[] array, int index, String defaultValue)
	{
		return index < array.length ? array[index] : defaultValue;
	}
}
