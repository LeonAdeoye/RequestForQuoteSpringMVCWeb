package com.leon.rfq.simulators;

public interface FxRateSimulator
{

	void initialize();

	/**
	 * Terminates the FxRate simulator stopping all FxRate generation.
	 */
	void terminate();

	/**
	 * Adds the underlying product using the underlying RIC as the lookup key to the FxRate publishing map.
	 *
	 * @param  currency	the RIC of the underlying product used as key to add it.
	 * @param  FxRateMean		the mean FxRate used random FxRate generator with normal distribution.
	 * @param  FxRateVariance	the variance used random FxRate generator with normal distribution.
	 * @throws					IllegalArgumentException if currency parameter is an empty string ||
	 * 							FxRateMean <= 0 || FxRateVariance <= 0.
	 */
	void add(String currency, double FxRateMean, double FxRateVariance);

	/**
	 * Removes the underlying product using the underlying RIC as the lookup key from the FxRate publisher map.
	 *
	 * @param  currency	the RIC of the underlying product used as key to remove it.
	 * @throws					IllegalArgumentException if currency parameter is an empty string.
	 */
	void remove(String currency);

	/**
	 * Suspends all FxRate generation by all underlyings.
	 */
	void suspendAll();

	/**
	 * Suspends the underlying product from generating FxRates using the underlying RIC as the lookup key.
	 *
	 * @param  currency	the RIC of the underlying product used as key to suspend it.
	 * @throws					IllegalArgumentException if currency parameter is an empty string.
	 */
	void suspend(String currency);

	/**
	 * Restart all FxRate generation by all underlyings.
	 */
	void awakenAll();

	/**
	 * Restart the FxRate generation of the specified underlying product.
	 *
	 * @param  currency	the RIC of the underlying product used as key to awaken it.
	 * @throws					IllegalArgumentException if currency parameter is an empty string.
	 */
	void awaken(String currency);

}