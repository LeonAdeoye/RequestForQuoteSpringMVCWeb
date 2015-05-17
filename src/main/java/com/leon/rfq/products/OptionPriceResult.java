package com.leon.rfq.products;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OptionPriceResult")
public final class OptionPriceResult
{
	private double delta;
	private double gamma;
	private double rho;
	private double theta;
	private double vega;
	private double price;
	private double rangeVariable;

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("\n Delta=");
		sb.append(this.delta);
		sb.append("\n Gamma=");
		sb.append(this.gamma);
		sb.append("\n Vega=");
		sb.append(this.vega);
		sb.append("\n Theta=");
		sb.append(this.theta);
		sb.append("\n Rho=");
		sb.append(this.rho);
		sb.append("\n Price=");
		sb.append(this.price);
		sb.append("\n Range variable=");
		sb.append(this.rangeVariable);
		return sb.toString();
	}
	
	public void add(OptionPriceResult priceResult)
	{
		this.setDelta(this.getDelta() + priceResult.getDelta());
		this.setGamma(this.getGamma() + priceResult.getGamma());
		this.setVega(this.getVega() + priceResult.getVega());
		this.setTheta(this.getTheta() + priceResult.getTheta());
		this.setRho(this.getRho() + priceResult.getRho());
		this.setPrice(this.getPrice() + priceResult.getPrice());
	}

	public void setDelta(double delta)
	{
		this.delta = delta;
	}

	public double getDelta()
	{
		return this.delta;
	}
	
	public void setRangeVariable(double rangeVariable)
	{
		this.rangeVariable = rangeVariable;
	}

	public double getRangeVariable()
	{
		return this.rangeVariable;
	}

	public void setGamma(double gamma)
	{
		this.gamma = gamma;
	}

	public double getGamma()
	{
		return this.gamma;
	}

	public void setTheta(double theta)
	{
		this.theta = theta;
	}

	public double getTheta()
	{
		return this.theta;
	}

	public void setVega(double vega)
	{
		this.vega = vega;
	}

	public double getVega()
	{
		return this.vega;
	}

	public void setRho(double rho)
	{
		this.rho = rho;
	}

	public double getRho()
	{
		return this.rho;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public double getPrice()
	{
		return this.price;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.delta);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.gamma);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.price);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.rangeVariable);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.rho);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.theta);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.vega);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof OptionPriceResult))
		{
			return false;
		}
		OptionPriceResult other = (OptionPriceResult) obj;
		if (Double.doubleToLongBits(this.delta) != Double
				.doubleToLongBits(other.delta))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.gamma) != Double
				.doubleToLongBits(other.gamma))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.price) != Double
				.doubleToLongBits(other.price))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.rangeVariable) != Double
				.doubleToLongBits(other.rangeVariable))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.rho) != Double.doubleToLongBits(other.rho))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.theta) != Double
				.doubleToLongBits(other.theta))
		{
			return false;
		}
		if (Double.doubleToLongBits(this.vega) != Double
				.doubleToLongBits(other.vega))
		{
			return false;
		}
		return true;
	}
	
}
