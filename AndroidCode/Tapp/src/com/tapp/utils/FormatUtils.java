package com.tapp.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class FormatUtils {

	private static FormatUtils INSTANCE = new FormatUtils();

	private AtomicInteger seq;

	private FormatUtils() {
		seq = new AtomicInteger(0);
	}

	public int getUniqueId() {
		return seq.incrementAndGet();
	}

	public static FormatUtils getInstance() {
		return INSTANCE;
	}
}