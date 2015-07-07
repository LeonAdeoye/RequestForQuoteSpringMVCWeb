package com.leon.rfq.common;

//The V is the return result
@FunctionalInterface
public interface TriFunction<T, R, U, V>
{
	V apply(T t, R r, U u);
}
