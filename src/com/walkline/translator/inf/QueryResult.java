package com.walkline.translator.inf;

import java.util.Vector;

/**
 * 查询结果类
 */
public interface QueryResult extends com.walkline.translator.inf.Object
{
	public String getFrom();

	public String getTo();

	public Vector getTranslateResult();

	public int getTranslateResultCount();
}