package com.leon.rfq.common;

public class Tag
{
	private String value;
	private String label;

	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getLabel()
	{
		return this.label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public Tag(String value, String label)
	{
		this.value = value;
		this.label = label;
	}
	
	public Tag(String label)
	{
		this.value = label;
		this.label = label;
	}
}
