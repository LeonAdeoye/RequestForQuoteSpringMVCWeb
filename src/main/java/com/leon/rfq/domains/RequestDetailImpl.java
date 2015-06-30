package com.leon.rfq.domains;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.leon.rfq.common.EnumTypes.HedgeTypeEnum;
import com.leon.rfq.common.EnumTypes.StatusEnum;
import com.leon.rfq.common.RegexConstants;

@XmlRootElement(name="RequestDetailImpl", namespace = "com.leon.rfq.domains")
public final class RequestDetailImpl
{
	@Pattern(regexp=RegexConstants.REQUEST_PATTERN, message="{request.validation.snippet.pattern}")
	@Size(min=1, max=100, message="{request.validation.snippet.size}")
	private String request;
	
	@Size(min=1, max=20, message="{request.validation.bookCode.size}")
	private String bookCode;
	private int identifier;
	private int clientId;
	private boolean isOTC;
	private StatusEnum status;
	private String lastUpdatedBy;
	private BigDecimal underlyingPrice;
	private String underlyingRIC;

	private int lotSize;
	private int multiplier;
	private int contracts;
	private int quantity;

	private LocalDate tradeDate;
	private LocalDate expiryDate;
	private BigDecimal dayCountConvention;

	private BigDecimal notionalMillions;
	private BigDecimal notionalFXRate;
	private String notionalCurrency;

	private BigDecimal delta;
	private BigDecimal gamma;
	private BigDecimal theta;
	private BigDecimal vega;
	private BigDecimal rho;
	private BigDecimal timeValue;
	private BigDecimal intrinsicValue;
	private BigDecimal lambda;

	private BigDecimal deltaNotional;
	private BigDecimal gammaNotional;
	private BigDecimal thetaNotional;
	private BigDecimal vegaNotional;
	private BigDecimal rhoNotional;

	private BigDecimal deltaShares;
	private BigDecimal gammaShares;
	private BigDecimal thetaShares;
	private BigDecimal vegaShares;
	private BigDecimal rhoShares;

	private String premiumSettlementCurrency;
	private LocalDate premiumSettlementDate;
	private int premiumSettlementDaysOverride;
	private BigDecimal premiumSettlementFXRate;

	private BigDecimal salesCreditAmount;
	private BigDecimal salesCreditPercentage;
	private BigDecimal salesCreditFXRate;
	private String salesCreditCurrency;

	private BigDecimal bidImpliedVol;
	private BigDecimal bidPremiumPercentage;
	private BigDecimal bidPremiumAmount;
	private BigDecimal bidFinalAmount;
	private BigDecimal bidFinalPercentage;

	private BigDecimal impliedVol;
	private BigDecimal premiumAmount;
	private BigDecimal premiumPercentage;

	private BigDecimal askImpliedVol;
	private BigDecimal askPremiumPercentage;
	private BigDecimal askPremiumAmount;
	private BigDecimal askFinalAmount;
	private BigDecimal askFinalPercentage;

	@Size(max=300, message="{request.validation.salesComment.size}")
	private String salesComment;
	@Size(max=300, message="{request.validation.traderComment.size}")
	private String traderComment;
	@Size(max=300, message="{request.validation.clientComment.size}")
	private String clientComment;

	@Size(min=1, max=20, message="{request.validation.pickedUpBy.size}")
	private String pickedUpBy;
	private HedgeTypeEnum hedgeType;
	private BigDecimal hedgePrice;
	private BigDecimal totalPremium;

	private List<OptionDetailImpl> legs;

	public RequestDetailImpl()
	{
		this.delta = BigDecimal.ZERO;
		this.gamma = BigDecimal.ZERO;
		this.vega = BigDecimal.ZERO;
		this.theta = BigDecimal.ZERO;
		this.premiumAmount = BigDecimal.ZERO;
		this.intrinsicValue = BigDecimal.ZERO;
		this.timeValue = BigDecimal.ZERO;
		this.lambda = BigDecimal.ZERO;
		this.rho = BigDecimal.ZERO;
	}

	public void setOTC(boolean isOTC)
	{
		this.isOTC = isOTC;
	}

	public void setLegs(List<OptionDetailImpl> legs)
	{
		this.legs = legs;
	}

	public BigDecimal getTimeValue()
	{
		return this.timeValue;
	}

	public void setTimeValue(BigDecimal timeValue)
	{
		this.timeValue = timeValue;
	}

	public BigDecimal getIntrinsicValue()
	{
		return this.intrinsicValue;
	}

	public void setIntrinsicValue(BigDecimal intrinsicValue)
	{
		this.intrinsicValue = intrinsicValue;
	}

	public BigDecimal getLambda()
	{
		return this.lambda;
	}

	public void setLambda(BigDecimal lambda)
	{
		this.lambda = lambda;
	}

	public void setLastUpdatedBy(String lastUpdatedBy)
	{
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public String getLastUpdatedBy()
	{
		return this.lastUpdatedBy;
	}

	public List<OptionDetailImpl> getLegs()
	{
		return this.legs;
	}

	public Iterator<OptionDetailImpl> iterator()
	{
		return this.legs.iterator();
	}

	public String getRequest()
	{
		return this.request;
	}

	public void setRequest(String request)
	{
		this.request = request;
	}

	public String getBookCode()
	{
		return this.bookCode;
	}

	public void setBookCode(String bookCode)
	{
		this.bookCode = bookCode;
	}

	public int getIdentifier()
	{
		return this.identifier;
	}

	public void setIdentifier(int requestId)
	{
		this.identifier = requestId;
	}

	public int getClientId()
	{
		return this.clientId;
	}

	public void setClientId(int clientId)
	{
		this.clientId = clientId;
	}

	public BigDecimal getNotionalMillions()
	{
		return this.notionalMillions;
	}

	public void setNotionalMillions(BigDecimal notionalMillions)
	{
		this.notionalMillions = notionalMillions;
	}

	public BigDecimal getNotionalFXRate()
	{
		return this.notionalFXRate;
	}

	public void setNotionalFXRate(BigDecimal notionalFXRate)
	{
		this.notionalFXRate = notionalFXRate;
	}

	public String getNotionalCurrency()
	{
		return this.notionalCurrency;
	}

	public void setNotionalCurrency(String notionalCurrency)
	{
		this.notionalCurrency = notionalCurrency;
	}

	public LocalDate getTradeDate()
	{
		return this.tradeDate;
	}
	
	public String getTradeDateString()
	{
		return this.tradeDate.toString();
	}

	public void setTradeDate(LocalDate tradeDate)
	{
		this.tradeDate = tradeDate;
	}

	public LocalDate getExpiryDate()
	{
		return this.expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate)
	{
		this.expiryDate = expiryDate;
	}
	
	public void setDayCountConvention(BigDecimal dayCountConvention)
	{
		this.dayCountConvention = dayCountConvention;
	}
	
	public BigDecimal getDayCountConvention()
	{
		return this.dayCountConvention;
	}

	public int getLotSize()
	{
		return this.lotSize;
	}

	public void setLotSize(int lotSize)
	{
		this.lotSize = lotSize;
	}

	public int getMultiplier()
	{
		return this.multiplier;
	}

	public void setMultiplier(int multiplier)
	{
		this.multiplier = multiplier;
	}

	public int getContracts()
	{
		return this.contracts;
	}

	public void setContracts(int contracts)
	{
		this.contracts = contracts;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public BigDecimal getDeltaShares()
	{
		return this.deltaShares;
	}

	public void setDeltaShares(BigDecimal deltaShares)
	{
		this.deltaShares = deltaShares;
	}

	public BigDecimal getGammaShares()
	{
		return this.gammaShares;
	}

	public void setGammaShares(BigDecimal gammaShares)
	{
		this.gammaShares = gammaShares;
	}

	public BigDecimal getThetaShares()
	{
		return this.thetaShares;
	}

	public void setThetaShares(BigDecimal thetaShares)
	{
		this.thetaShares = thetaShares;
	}

	public BigDecimal getVegaShares()
	{
		return this.vegaShares;
	}

	public void setVegaShares(BigDecimal vegaShares)
	{
		this.vegaShares = vegaShares;
	}

	public BigDecimal getRhoShares()
	{
		return this.rhoShares;
	}

	public void setRhoShares(BigDecimal rhoShares)
	{
		this.rhoShares = rhoShares;
	}

	public BigDecimal getDeltaNotional()
	{
		return this.deltaNotional;
	}

	public void setDeltaNotional(BigDecimal deltaNotional)
	{
		this.deltaNotional = deltaNotional;
	}

	public BigDecimal getGammaNotional()
	{
		return this.gammaNotional;
	}

	public void setGammaNotional(BigDecimal gammaNotional)
	{
		this.gammaNotional = gammaNotional;
	}

	public BigDecimal getThetaNotional()
	{
		return this.thetaNotional;
	}

	public void setThetaNotional(BigDecimal thetaNotional)
	{
		this.thetaNotional = thetaNotional;
	}

	public BigDecimal getVegaNotional()
	{
		return this.vegaNotional;
	}

	public void setVegaNotional(BigDecimal vegaNotional)
	{
		this.vegaNotional = vegaNotional;
	}

	public BigDecimal getRhoNotional()
	{
		return this.rhoNotional;
	}

	public void setRhoNotional(BigDecimal rhoNotional)
	{
		this.rhoNotional = rhoNotional;
	}

	public BigDecimal getDelta()
	{
		return this.delta;
	}

	public void setDelta(BigDecimal delta)
	{
		this.delta = delta;
	}

	public BigDecimal getGamma()
	{
		return this.gamma;
	}

	public void setGamma(BigDecimal gamma)
	{
		this.gamma = gamma;
	}

	public BigDecimal getTheta()
	{
		return this.theta;
	}

	public void setTheta(BigDecimal theta)
	{
		this.theta = theta;
	}

	public BigDecimal getVega()
	{
		return this.vega;
	}

	public void setVega(BigDecimal vega)
	{
		this.vega = vega;
	}

	public BigDecimal getRho()
	{
		return this.rho;
	}

	public void setRho(BigDecimal rho)
	{
		this.rho = rho;
	}

	public String getPremiumSettlementCurrency()
	{
		return this.premiumSettlementCurrency;
	}

	public void setPremiumSettlementCurrency(String premiumSettlementCurrency)
	{
		this.premiumSettlementCurrency = premiumSettlementCurrency;
	}

	public LocalDate getPremiumSettlementDate()
	{
		return this.premiumSettlementDate;
	}

	public void setPremiumSettlementDate(LocalDate premiumSettlementDate)
	{
		this.premiumSettlementDate = premiumSettlementDate;
	}

	public BigDecimal getPremiumSettlementFXRate()
	{
		return this.premiumSettlementFXRate;
	}

	public void setPremiumSettlementFXRate(BigDecimal premiumSettlementFXRate)
	{
		this.premiumSettlementFXRate = premiumSettlementFXRate;
	}

	public int getPremiumSettlementDaysOverride()
	{
		return this.premiumSettlementDaysOverride;
	}

	public void setPremiumSettlementDaysOverride(int premiumSettlementDaysOverride)
	{
		this.premiumSettlementDaysOverride = premiumSettlementDaysOverride;
	}

	public String getSalesCreditCurrency()
	{
		return this.salesCreditCurrency;
	}

	public void setSalesCreditCurrency(String salesCreditCurrency)
	{
		this.salesCreditCurrency = salesCreditCurrency;
	}

	public BigDecimal getSalesCreditPercentage()
	{
		return this.salesCreditPercentage;
	}

	public void setSalesCreditPercentage(BigDecimal salesCreditPercentage)
	{
		this.salesCreditPercentage = salesCreditPercentage;
	}

	public BigDecimal getSalesCreditFXRate()
	{
		return this.salesCreditFXRate;
	}

	public void setSalesCreditFXRate(BigDecimal salesCreditFXRate)
	{
		this.salesCreditFXRate = salesCreditFXRate;
	}

	public BigDecimal getSalesCreditAmount()
	{
		return this.salesCreditAmount;
	}

	public void setSalesCreditAmount(BigDecimal salesCreditAmount)
	{
		this.salesCreditAmount = salesCreditAmount;
	}

	public BigDecimal getBidImpliedVol()
	{
		return this.bidImpliedVol;
	}

	public void setBidImpliedVol(BigDecimal bidImpliedVol)
	{
		this.bidImpliedVol = bidImpliedVol;
	}

	public BigDecimal getBidPremiumAmount()
	{
		return this.bidPremiumAmount;
	}

	public void setBidPremiumAmount(BigDecimal bidPremiumAmount)
	{
		this.bidPremiumAmount = bidPremiumAmount;
	}

	public BigDecimal getBidPremiumPercentage()
	{
		return this.bidPremiumPercentage;
	}

	public void setBidPremiumPercentage(BigDecimal bidPremiumPercentage)
	{
		this.bidPremiumPercentage = bidPremiumPercentage;
	}

	public BigDecimal getBidFinalAmount()
	{
		return this.bidFinalAmount;
	}

	public void setBidFinalAmount(BigDecimal bidFinalAmount)
	{
		this.bidFinalAmount = bidFinalAmount;
	}

	public BigDecimal getBidFinalPercentage()
	{
		return this.bidFinalPercentage;
	}

	public void setBidFinalPercentage(BigDecimal bidFinalPercentage)
	{
		this.bidFinalPercentage = bidFinalPercentage;
	}

	public BigDecimal getAskImpliedVol()
	{
		return this.askImpliedVol;
	}

	public void setAskImpliedVol(BigDecimal askImpliedVol)
	{
		this.askImpliedVol = askImpliedVol;
	}

	public BigDecimal getAskPremiumAmount()
	{
		return this.askPremiumAmount;
	}

	public void setAskPremiumAmount(BigDecimal askPremiumAmount)
	{
		this.askPremiumAmount = askPremiumAmount;
	}

	public BigDecimal getAskPremiumPercentage()
	{
		return this.askPremiumPercentage;
	}

	public void setAskPremiumPercentage(BigDecimal askPremiumPercentage)
	{
		this.askPremiumPercentage = askPremiumPercentage;
	}

	public BigDecimal getAskFinalAmount()
	{
		return this.askFinalAmount;
	}

	public void setAskFinalAmount(BigDecimal askFinalAmount)
	{
		this.askFinalAmount = askFinalAmount;
	}

	public BigDecimal getAskFinalPercentage()
	{
		return this.askFinalPercentage;
	}

	public void setAskFinalPercentage(BigDecimal askFinalPercentage)
	{
		this.askFinalPercentage = askFinalPercentage;
	}

	public BigDecimal getImpliedVol()
	{
		return this.impliedVol;
	}

	public void setImpliedVol(BigDecimal impliedVol)
	{
		this.impliedVol = impliedVol;
	}

	public BigDecimal getPremiumAmount()
	{
		return this.premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount)
	{
		this.premiumAmount = premiumAmount;
	}

	public BigDecimal getPremiumPercentage()
	{
		return this.premiumPercentage;
	}

	public void setPremiumPercentage(BigDecimal premiumPercentage)
	{
		this.premiumPercentage = premiumPercentage;
	}

	public StatusEnum getStatus()
	{
		return this.status;
	}

	public void setStatus(StatusEnum status)
	{
		this.status = status;
	}

	public boolean getIsOTC()
	{
		return this.isOTC;
	}

	public void setIsOTC(boolean isOTC)
	{
		this.isOTC = isOTC;
	}

	public String getSalesComment()
	{
		return this.salesComment;
	}

	public void setSalesComment(String salesComment)
	{
		this.salesComment = salesComment;
	}

	public String getTraderComment()
	{
		return this.traderComment;
	}

	public void setTraderComment(String traderComment)
	{
		this.traderComment = traderComment;
	}

	public String getClientComment()
	{
		return this.clientComment;
	}

	public void setClientComment(String clientComment)
	{
		this.clientComment = clientComment;
	}

	public BigDecimal getHedgePrice()
	{
		return this.hedgePrice;
	}

	public void setHedgePrice(BigDecimal hedgePrice)
	{
		this.hedgePrice = hedgePrice;
	}

	public BigDecimal getTotalPremium()
	{
		return this.totalPremium;
	}

	public void setTotalPremium(BigDecimal totalPremium)
	{
		this.totalPremium = totalPremium;
	}

	public String getPickedUpBy()
	{
		return this.pickedUpBy;
	}

	public void setPickedUpBy(String pickedUpBy)
	{
		this.pickedUpBy = pickedUpBy;
	}

	public HedgeTypeEnum getHedgeType()
	{
		return this.hedgeType;
	}

	public void setHedgeType(HedgeTypeEnum hedgeType)
	{
		this.hedgeType = hedgeType;
	}

	public BigDecimal getUnderlyingPrice()
	{
		return this.underlyingPrice;
	}

	public void setUnderlyingPrice(BigDecimal underlyingPrice)
	{
		this.underlyingPrice = underlyingPrice;
	}

	public String getUnderlyingRIC()
	{
		return this.underlyingRIC;
	}

	public void setUnderlyingRIC(String underlyingRIC)
	{
		this.underlyingRIC = underlyingRIC;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("RequestDetailImpl [request=");
		builder.append(this.request);
		builder.append(", bookCode=");
		builder.append(this.bookCode);
		builder.append(", identifier=");
		builder.append(this.identifier);
		builder.append(", clientId=");
		builder.append(this.clientId);
		builder.append(", isOTC=");
		builder.append(this.isOTC);
		builder.append(", status=");
		builder.append(this.status);
		builder.append(", lastUpdatedBy=");
		builder.append(this.lastUpdatedBy);
		builder.append(", underlyingPrice=");
		builder.append(this.underlyingPrice);
		builder.append(", underlyingRIC=");
		builder.append(this.underlyingRIC);
		builder.append(", lotSize=");
		builder.append(this.lotSize);
		builder.append(", multiplier=");
		builder.append(this.multiplier);
		builder.append(", contracts=");
		builder.append(this.contracts);
		builder.append(", quantity=");
		builder.append(this.quantity);
		builder.append(", tradeDate=");
		builder.append(this.tradeDate);
		builder.append(", expiryDate=");
		builder.append(this.expiryDate);
		builder.append(", dayCountConvention=");
		builder.append(this.dayCountConvention);
		builder.append(", notionalMillions=");
		builder.append(this.notionalMillions);
		builder.append(", notionalFXRate=");
		builder.append(this.notionalFXRate);
		builder.append(", notionalCurrency=");
		builder.append(this.notionalCurrency);
		builder.append(", delta=");
		builder.append(this.delta);
		builder.append(", gamma=");
		builder.append(this.gamma);
		builder.append(", theta=");
		builder.append(this.theta);
		builder.append(", vega=");
		builder.append(this.vega);
		builder.append(", rho=");
		builder.append(this.rho);
		builder.append(", timeValue=");
		builder.append(this.timeValue);
		builder.append(", intrinsicValue=");
		builder.append(this.intrinsicValue);
		builder.append(", lambda=");
		builder.append(this.lambda);
		builder.append(", deltaNotional=");
		builder.append(this.deltaNotional);
		builder.append(", gammaNotional=");
		builder.append(this.gammaNotional);
		builder.append(", thetaNotional=");
		builder.append(this.thetaNotional);
		builder.append(", vegaNotional=");
		builder.append(this.vegaNotional);
		builder.append(", rhoNotional=");
		builder.append(this.rhoNotional);
		builder.append(", deltaShares=");
		builder.append(this.deltaShares);
		builder.append(", gammaShares=");
		builder.append(this.gammaShares);
		builder.append(", thetaShares=");
		builder.append(this.thetaShares);
		builder.append(", vegaShares=");
		builder.append(this.vegaShares);
		builder.append(", rhoShares=");
		builder.append(this.rhoShares);
		builder.append(", premiumSettlementCurrency=");
		builder.append(this.premiumSettlementCurrency);
		builder.append(", premiumSettlementDate=");
		builder.append(this.premiumSettlementDate);
		builder.append(", premiumSettlementDaysOverride=");
		builder.append(this.premiumSettlementDaysOverride);
		builder.append(", premiumSettlementFXRate=");
		builder.append(this.premiumSettlementFXRate);
		builder.append(", salesCreditAmount=");
		builder.append(this.salesCreditAmount);
		builder.append(", salesCreditPercentage=");
		builder.append(this.salesCreditPercentage);
		builder.append(", salesCreditFXRate=");
		builder.append(this.salesCreditFXRate);
		builder.append(", salesCreditCurrency=");
		builder.append(this.salesCreditCurrency);
		builder.append(", bidImpliedVol=");
		builder.append(this.bidImpliedVol);
		builder.append(", bidPremiumPercentage=");
		builder.append(this.bidPremiumPercentage);
		builder.append(", bidPremiumAmount=");
		builder.append(this.bidPremiumAmount);
		builder.append(", bidFinalAmount=");
		builder.append(this.bidFinalAmount);
		builder.append(", bidFinalPercentage=");
		builder.append(this.bidFinalPercentage);
		builder.append(", impliedVol=");
		builder.append(this.impliedVol);
		builder.append(", premiumAmount=");
		builder.append(this.premiumAmount);
		builder.append(", premiumPercentage=");
		builder.append(this.premiumPercentage);
		builder.append(", askImpliedVol=");
		builder.append(this.askImpliedVol);
		builder.append(", askPremiumPercentage=");
		builder.append(this.askPremiumPercentage);
		builder.append(", askPremiumAmount=");
		builder.append(this.askPremiumAmount);
		builder.append(", askFinalAmount=");
		builder.append(this.askFinalAmount);
		builder.append(", askFinalPercentage=");
		builder.append(this.askFinalPercentage);
		builder.append(", salesComment=");
		builder.append(this.salesComment);
		builder.append(", traderComment=");
		builder.append(this.traderComment);
		builder.append(", clientComment=");
		builder.append(this.clientComment);
		builder.append(", pickedUpBy=");
		builder.append(this.pickedUpBy);
		builder.append(", hedgeType=");
		builder.append(this.hedgeType);
		builder.append(", hedgePrice=");
		builder.append(this.hedgePrice);
		builder.append(", totalPremium=");
		builder.append(this.totalPremium);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.askFinalAmount == null) ? 0 : this.askFinalAmount.hashCode());
		result = (prime
				* result)
				+ ((this.askFinalPercentage == null) ? 0 : this.askFinalPercentage
						.hashCode());
		result = (prime * result)
				+ ((this.askImpliedVol == null) ? 0 : this.askImpliedVol.hashCode());
		result = (prime
				* result)
				+ ((this.askPremiumAmount == null) ? 0 : this.askPremiumAmount.hashCode());
		result = (prime
				* result)
				+ ((this.askPremiumPercentage == null) ? 0 : this.askPremiumPercentage
						.hashCode());
		result = (prime * result)
				+ ((this.bidFinalAmount == null) ? 0 : this.bidFinalAmount.hashCode());
		result = (prime
				* result)
				+ ((this.bidFinalPercentage == null) ? 0 : this.bidFinalPercentage
						.hashCode());
		result = (prime * result)
				+ ((this.bidImpliedVol == null) ? 0 : this.bidImpliedVol.hashCode());
		result = (prime
				* result)
				+ ((this.bidPremiumAmount == null) ? 0 : this.bidPremiumAmount.hashCode());
		result = (prime
				* result)
				+ ((this.bidPremiumPercentage == null) ? 0 : this.bidPremiumPercentage
						.hashCode());
		result = (prime * result)
				+ ((this.bookCode == null) ? 0 : this.bookCode.hashCode());
		result = (prime * result)
				+ ((this.clientComment == null) ? 0 : this.clientComment.hashCode());
		result = (prime * result) + this.clientId;
		result = (prime * result) + this.contracts;
		result = (prime
				* result)
				+ ((this.dayCountConvention == null) ? 0 : this.dayCountConvention
						.hashCode());
		result = (prime * result) + ((this.delta == null) ? 0 : this.delta.hashCode());
		result = (prime * result)
				+ ((this.deltaNotional == null) ? 0 : this.deltaNotional.hashCode());
		result = (prime * result)
				+ ((this.deltaShares == null) ? 0 : this.deltaShares.hashCode());
		result = (prime * result)
				+ ((this.expiryDate == null) ? 0 : this.expiryDate.hashCode());
		result = (prime * result) + ((this.gamma == null) ? 0 : this.gamma.hashCode());
		result = (prime * result)
				+ ((this.gammaNotional == null) ? 0 : this.gammaNotional.hashCode());
		result = (prime * result)
				+ ((this.gammaShares == null) ? 0 : this.gammaShares.hashCode());
		result = (prime * result)
				+ ((this.hedgePrice == null) ? 0 : this.hedgePrice.hashCode());
		result = (prime * result)
				+ ((this.hedgeType == null) ? 0 : this.hedgeType.hashCode());
		result = (prime * result) + this.identifier;
		result = (prime * result)
				+ ((this.impliedVol == null) ? 0 : this.impliedVol.hashCode());
		result = (prime * result)
				+ ((this.intrinsicValue == null) ? 0 : this.intrinsicValue.hashCode());
		result = (prime * result) + (this.isOTC ? 1231 : 1237);
		result = (prime * result) + ((this.lambda == null) ? 0 : this.lambda.hashCode());
		result = (prime * result)
				+ ((this.lastUpdatedBy == null) ? 0 : this.lastUpdatedBy.hashCode());
		result = (prime * result) + this.lotSize;
		result = (prime * result) + this.multiplier;
		result = (prime
				* result)
				+ ((this.notionalCurrency == null) ? 0 : this.notionalCurrency.hashCode());
		result = (prime * result)
				+ ((this.notionalFXRate == null) ? 0 : this.notionalFXRate.hashCode());
		result = (prime
				* result)
				+ ((this.notionalMillions == null) ? 0 : this.notionalMillions.hashCode());
		result = (prime * result)
				+ ((this.pickedUpBy == null) ? 0 : this.pickedUpBy.hashCode());
		result = (prime * result)
				+ ((this.premiumAmount == null) ? 0 : this.premiumAmount.hashCode());
		result = (prime
				* result)
				+ ((this.premiumPercentage == null) ? 0 : this.premiumPercentage
						.hashCode());
		result = (prime
				* result)
				+ ((this.premiumSettlementCurrency == null) ? 0
						: this.premiumSettlementCurrency.hashCode());
		result = (prime
				* result)
				+ ((this.premiumSettlementDate == null) ? 0 : this.premiumSettlementDate
						.hashCode());
		result = (prime * result) + this.premiumSettlementDaysOverride;
		result = (prime
				* result)
				+ ((this.premiumSettlementFXRate == null) ? 0
						: this.premiumSettlementFXRate.hashCode());
		result = (prime * result) + this.quantity;
		result = (prime * result) + ((this.request == null) ? 0 : this.request.hashCode());
		result = (prime * result) + ((this.rho == null) ? 0 : this.rho.hashCode());
		result = (prime * result)
				+ ((this.rhoNotional == null) ? 0 : this.rhoNotional.hashCode());
		result = (prime * result)
				+ ((this.rhoShares == null) ? 0 : this.rhoShares.hashCode());
		result = (prime * result)
				+ ((this.salesComment == null) ? 0 : this.salesComment.hashCode());
		result = (prime
				* result)
				+ ((this.salesCreditAmount == null) ? 0 : this.salesCreditAmount
						.hashCode());
		result = (prime
				* result)
				+ ((this.salesCreditCurrency == null) ? 0 : this.salesCreditCurrency
						.hashCode());
		result = (prime
				* result)
				+ ((this.salesCreditFXRate == null) ? 0 : this.salesCreditFXRate
						.hashCode());
		result = (prime
				* result)
				+ ((this.salesCreditPercentage == null) ? 0 : this.salesCreditPercentage
						.hashCode());
		result = (prime * result) + ((this.status == null) ? 0 : this.status.hashCode());
		result = (prime * result) + ((this.theta == null) ? 0 : this.theta.hashCode());
		result = (prime * result)
				+ ((this.thetaNotional == null) ? 0 : this.thetaNotional.hashCode());
		result = (prime * result)
				+ ((this.thetaShares == null) ? 0 : this.thetaShares.hashCode());
		result = (prime * result)
				+ ((this.timeValue == null) ? 0 : this.timeValue.hashCode());
		result = (prime * result)
				+ ((this.totalPremium == null) ? 0 : this.totalPremium.hashCode());
		result = (prime * result)
				+ ((this.tradeDate == null) ? 0 : this.tradeDate.hashCode());
		result = (prime * result)
				+ ((this.traderComment == null) ? 0 : this.traderComment.hashCode());
		result = (prime * result)
				+ ((this.underlyingPrice == null) ? 0 : this.underlyingPrice.hashCode());
		result = (prime * result)
				+ ((this.underlyingRIC == null) ? 0 : this.underlyingRIC.hashCode());
		result = (prime * result) + ((this.vega == null) ? 0 : this.vega.hashCode());
		result = (prime * result)
				+ ((this.vegaNotional == null) ? 0 : this.vegaNotional.hashCode());
		result = (prime * result)
				+ ((this.vegaShares == null) ? 0 : this.vegaShares.hashCode());
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
		if (!(obj instanceof RequestDetailImpl))
		{
			return false;
		}
		RequestDetailImpl other = (RequestDetailImpl) obj;
		if (this.askFinalAmount == null)
		{
			if (other.askFinalAmount != null)
			{
				return false;
			}
		} else if (!this.askFinalAmount.equals(other.askFinalAmount))
		{
			return false;
		}
		if (this.askFinalPercentage == null)
		{
			if (other.askFinalPercentage != null)
			{
				return false;
			}
		} else if (!this.askFinalPercentage.equals(other.askFinalPercentage))
		{
			return false;
		}
		if (this.askImpliedVol == null)
		{
			if (other.askImpliedVol != null)
			{
				return false;
			}
		} else if (!this.askImpliedVol.equals(other.askImpliedVol))
		{
			return false;
		}
		if (this.askPremiumAmount == null)
		{
			if (other.askPremiumAmount != null)
			{
				return false;
			}
		} else if (!this.askPremiumAmount.equals(other.askPremiumAmount))
		{
			return false;
		}
		if (this.askPremiumPercentage == null)
		{
			if (other.askPremiumPercentage != null)
			{
				return false;
			}
		} else if (!this.askPremiumPercentage.equals(other.askPremiumPercentage))
		{
			return false;
		}
		if (this.bidFinalAmount == null)
		{
			if (other.bidFinalAmount != null)
			{
				return false;
			}
		} else if (!this.bidFinalAmount.equals(other.bidFinalAmount))
		{
			return false;
		}
		if (this.bidFinalPercentage == null)
		{
			if (other.bidFinalPercentage != null)
			{
				return false;
			}
		} else if (!this.bidFinalPercentage.equals(other.bidFinalPercentage))
		{
			return false;
		}
		if (this.bidImpliedVol == null)
		{
			if (other.bidImpliedVol != null)
			{
				return false;
			}
		} else if (!this.bidImpliedVol.equals(other.bidImpliedVol))
		{
			return false;
		}
		if (this.bidPremiumAmount == null)
		{
			if (other.bidPremiumAmount != null)
			{
				return false;
			}
		} else if (!this.bidPremiumAmount.equals(other.bidPremiumAmount))
		{
			return false;
		}
		if (this.bidPremiumPercentage == null)
		{
			if (other.bidPremiumPercentage != null)
			{
				return false;
			}
		} else if (!this.bidPremiumPercentage.equals(other.bidPremiumPercentage))
		{
			return false;
		}
		if (this.bookCode == null)
		{
			if (other.bookCode != null)
			{
				return false;
			}
		} else if (!this.bookCode.equals(other.bookCode))
		{
			return false;
		}
		if (this.clientComment == null)
		{
			if (other.clientComment != null)
			{
				return false;
			}
		} else if (!this.clientComment.equals(other.clientComment))
		{
			return false;
		}
		if (this.clientId != other.clientId)
		{
			return false;
		}
		if (this.contracts != other.contracts)
		{
			return false;
		}
		if (this.dayCountConvention == null)
		{
			if (other.dayCountConvention != null)
			{
				return false;
			}
		} else if (!this.dayCountConvention.equals(other.dayCountConvention))
		{
			return false;
		}
		if (this.delta == null)
		{
			if (other.delta != null)
			{
				return false;
			}
		} else if (!this.delta.equals(other.delta))
		{
			return false;
		}
		if (this.deltaNotional == null)
		{
			if (other.deltaNotional != null)
			{
				return false;
			}
		} else if (!this.deltaNotional.equals(other.deltaNotional))
		{
			return false;
		}
		if (this.deltaShares == null)
		{
			if (other.deltaShares != null)
			{
				return false;
			}
		} else if (!this.deltaShares.equals(other.deltaShares))
		{
			return false;
		}
		if (this.expiryDate == null)
		{
			if (other.expiryDate != null)
			{
				return false;
			}
		} else if (!this.expiryDate.equals(other.expiryDate))
		{
			return false;
		}
		if (this.gamma == null)
		{
			if (other.gamma != null)
			{
				return false;
			}
		} else if (!this.gamma.equals(other.gamma))
		{
			return false;
		}
		if (this.gammaNotional == null)
		{
			if (other.gammaNotional != null)
			{
				return false;
			}
		} else if (!this.gammaNotional.equals(other.gammaNotional))
		{
			return false;
		}
		if (this.gammaShares == null)
		{
			if (other.gammaShares != null)
			{
				return false;
			}
		} else if (!this.gammaShares.equals(other.gammaShares))
		{
			return false;
		}
		if (this.hedgePrice == null)
		{
			if (other.hedgePrice != null)
			{
				return false;
			}
		} else if (!this.hedgePrice.equals(other.hedgePrice))
		{
			return false;
		}
		if (this.hedgeType != other.hedgeType)
		{
			return false;
		}
		if (this.identifier != other.identifier)
		{
			return false;
		}
		if (this.impliedVol == null)
		{
			if (other.impliedVol != null)
			{
				return false;
			}
		} else if (!this.impliedVol.equals(other.impliedVol))
		{
			return false;
		}
		if (this.intrinsicValue == null)
		{
			if (other.intrinsicValue != null)
			{
				return false;
			}
		} else if (!this.intrinsicValue.equals(other.intrinsicValue))
		{
			return false;
		}
		if (this.isOTC != other.isOTC)
		{
			return false;
		}
		if (this.lambda == null)
		{
			if (other.lambda != null)
			{
				return false;
			}
		} else if (!this.lambda.equals(other.lambda))
		{
			return false;
		}
		if (this.lastUpdatedBy == null)
		{
			if (other.lastUpdatedBy != null)
			{
				return false;
			}
		} else if (!this.lastUpdatedBy.equals(other.lastUpdatedBy))
		{
			return false;
		}
		if (this.lotSize != other.lotSize)
		{
			return false;
		}
		if (this.multiplier != other.multiplier)
		{
			return false;
		}
		if (this.notionalCurrency == null)
		{
			if (other.notionalCurrency != null)
			{
				return false;
			}
		} else if (!this.notionalCurrency.equals(other.notionalCurrency))
		{
			return false;
		}
		if (this.notionalFXRate == null)
		{
			if (other.notionalFXRate != null)
			{
				return false;
			}
		} else if (!this.notionalFXRate.equals(other.notionalFXRate))
		{
			return false;
		}
		if (this.notionalMillions == null)
		{
			if (other.notionalMillions != null)
			{
				return false;
			}
		} else if (!this.notionalMillions.equals(other.notionalMillions))
		{
			return false;
		}
		if (this.pickedUpBy == null)
		{
			if (other.pickedUpBy != null)
			{
				return false;
			}
		} else if (!this.pickedUpBy.equals(other.pickedUpBy))
		{
			return false;
		}
		if (this.premiumAmount == null)
		{
			if (other.premiumAmount != null)
			{
				return false;
			}
		} else if (!this.premiumAmount.equals(other.premiumAmount))
		{
			return false;
		}
		if (this.premiumPercentage == null)
		{
			if (other.premiumPercentage != null)
			{
				return false;
			}
		} else if (!this.premiumPercentage.equals(other.premiumPercentage))
		{
			return false;
		}
		if (this.premiumSettlementCurrency == null)
		{
			if (other.premiumSettlementCurrency != null)
			{
				return false;
			}
		} else if (!this.premiumSettlementCurrency
				.equals(other.premiumSettlementCurrency))
		{
			return false;
		}
		if (this.premiumSettlementDate == null)
		{
			if (other.premiumSettlementDate != null)
			{
				return false;
			}
		} else if (!this.premiumSettlementDate.equals(other.premiumSettlementDate))
		{
			return false;
		}
		if (this.premiumSettlementDaysOverride != other.premiumSettlementDaysOverride)
		{
			return false;
		}
		if (this.premiumSettlementFXRate == null)
		{
			if (other.premiumSettlementFXRate != null)
			{
				return false;
			}
		} else if (!this.premiumSettlementFXRate
				.equals(other.premiumSettlementFXRate))
		{
			return false;
		}
		if (this.quantity != other.quantity)
		{
			return false;
		}
		if (this.request == null)
		{
			if (other.request != null)
			{
				return false;
			}
		} else if (!this.request.equals(other.request))
		{
			return false;
		}
		if (this.rho == null)
		{
			if (other.rho != null)
			{
				return false;
			}
		} else if (!this.rho.equals(other.rho))
		{
			return false;
		}
		if (this.rhoNotional == null)
		{
			if (other.rhoNotional != null)
			{
				return false;
			}
		} else if (!this.rhoNotional.equals(other.rhoNotional))
		{
			return false;
		}
		if (this.rhoShares == null)
		{
			if (other.rhoShares != null)
			{
				return false;
			}
		} else if (!this.rhoShares.equals(other.rhoShares))
		{
			return false;
		}
		if (this.salesComment == null)
		{
			if (other.salesComment != null)
			{
				return false;
			}
		} else if (!this.salesComment.equals(other.salesComment))
		{
			return false;
		}
		if (this.salesCreditAmount == null)
		{
			if (other.salesCreditAmount != null)
			{
				return false;
			}
		} else if (!this.salesCreditAmount.equals(other.salesCreditAmount))
		{
			return false;
		}
		if (this.salesCreditCurrency == null)
		{
			if (other.salesCreditCurrency != null)
			{
				return false;
			}
		} else if (!this.salesCreditCurrency.equals(other.salesCreditCurrency))
		{
			return false;
		}
		if (this.salesCreditFXRate == null)
		{
			if (other.salesCreditFXRate != null)
			{
				return false;
			}
		} else if (!this.salesCreditFXRate.equals(other.salesCreditFXRate))
		{
			return false;
		}
		if (this.salesCreditPercentage == null)
		{
			if (other.salesCreditPercentage != null)
			{
				return false;
			}
		} else if (!this.salesCreditPercentage.equals(other.salesCreditPercentage))
		{
			return false;
		}
		if (this.status != other.status)
		{
			return false;
		}
		if (this.theta == null)
		{
			if (other.theta != null)
			{
				return false;
			}
		} else if (!this.theta.equals(other.theta))
		{
			return false;
		}
		if (this.thetaNotional == null)
		{
			if (other.thetaNotional != null)
			{
				return false;
			}
		} else if (!this.thetaNotional.equals(other.thetaNotional))
		{
			return false;
		}
		if (this.thetaShares == null)
		{
			if (other.thetaShares != null)
			{
				return false;
			}
		} else if (!this.thetaShares.equals(other.thetaShares))
		{
			return false;
		}
		if (this.timeValue == null)
		{
			if (other.timeValue != null)
			{
				return false;
			}
		} else if (!this.timeValue.equals(other.timeValue))
		{
			return false;
		}
		if (this.totalPremium == null)
		{
			if (other.totalPremium != null)
			{
				return false;
			}
		} else if (!this.totalPremium.equals(other.totalPremium))
		{
			return false;
		}
		if (this.tradeDate == null)
		{
			if (other.tradeDate != null)
			{
				return false;
			}
		} else if (!this.tradeDate.equals(other.tradeDate))
		{
			return false;
		}
		if (this.traderComment == null)
		{
			if (other.traderComment != null)
			{
				return false;
			}
		} else if (!this.traderComment.equals(other.traderComment))
		{
			return false;
		}
		if (this.underlyingPrice == null)
		{
			if (other.underlyingPrice != null)
			{
				return false;
			}
		} else if (!this.underlyingPrice.equals(other.underlyingPrice))
		{
			return false;
		}
		if (this.underlyingRIC == null)
		{
			if (other.underlyingRIC != null)
			{
				return false;
			}
		} else if (!this.underlyingRIC.equals(other.underlyingRIC))
		{
			return false;
		}
		if (this.vega == null)
		{
			if (other.vega != null)
			{
				return false;
			}
		} else if (!this.vega.equals(other.vega))
		{
			return false;
		}
		if (this.vegaNotional == null)
		{
			if (other.vegaNotional != null)
			{
				return false;
			}
		} else if (!this.vegaNotional.equals(other.vegaNotional))
		{
			return false;
		}
		if (this.vegaShares == null)
		{
			if (other.vegaShares != null)
			{
				return false;
			}
		} else if (!this.vegaShares.equals(other.vegaShares))
		{
			return false;
		}
		return true;
	}
	
}