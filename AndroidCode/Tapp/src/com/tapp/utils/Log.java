package com.tapp.utils;

/**
 * A wrapper for log-output. Useful to disable log output before shipping the
 * application (the debug information can still be collected internally and send
 * to the developer when an error appears)
 * 
 * @author Spobo
 * 
 */
public class Log {
	public static final int VERBOSE = android.util.Log.VERBOSE;
	public static final int DEBUG = android.util.Log.DEBUG;
	public static final int ERROR = android.util.Log.ERROR;
	public static final int INFO = android.util.Log.INFO;
	public static final int WARN = android.util.Log.WARN;
	public static final int ASSERT = android.util.Log.ASSERT;
	public static boolean isLogEnabled = true;

	public interface LogInterface {

		void d(String logTag, String logText);

		void e(String logTag, String logText);

		void e(String logTag, String logText, Throwable exception);

		void w(String logTag, String logText);

		void w(String logTag, String logText, Throwable exception);

		void v(String logTag, String logText);

		void i(String logTag, String logText);

	}

	/**
	 * @param callDepth
	 *            Normally 2 (or 1 if directly used). This method is calles from
	 *            a util class like a Log class you dont want the name of the
	 *            log method but the method which called the log method, so pass
	 *            2 as the call depth! (1 would be the Log class and 0 would be
	 *            the Thread.getStackTrace() method)
	 * @return
	 */
	public static String getCurrentMethod(int callDepth) {
		StackTraceElement x = Thread.currentThread().getStackTrace()[callDepth];
		return x.getClassName() + "." + x.getMethodName() + "(..): (" + x.getClassName() + ".java:" + x.getLineNumber() + ")";
	}

	private static LogInterface instance;

	public static LogInterface getInstance() {
		if (instance == null)
			instance = newDefaultAndroidLog();
		return instance;
	}

	public static void setInstance(LogInterface instance) {
		Log.instance = instance;
	}

	private static LogInterface newDefaultAndroidLog() {
		return new LogInterface() {

			@Override
			public void w(String logTag, String logText) {
				android.util.Log.w(logTag, logText);
			}

			@Override
			public void w(String logTag, String logText, Throwable exception) {
				android.util.Log.e(logTag, logText, exception);
			}

			@Override
			public void v(String logTag, String logText) {
				android.util.Log.v(logTag, logText);
			}

			@Override
			public void i(String logTag, String logText) {
				android.util.Log.i(logTag, logText);
			}

			@Override
			public void e(String logTag, String logText) {
				android.util.Log.e(logTag, logText);
			}

			@Override
			public void e(String logTag, String logText, Throwable exception) {
				android.util.Log.e(logTag, logText, exception);
			}

			@Override
			public void d(String logTag, String logText) {
				android.util.Log.d(logTag, logText);
			}

		};
	}

	public static void d(String logTag, String logText) {
		if (isLogEnabled) {
			getInstance().d(logTag, logText);
		}
	}

	public static void e(String logTag, String logText) {
		if (isLogEnabled) {
			getInstance().e(logTag, logText);
		}
	}

	public static void e(String logTag, String logText, Throwable exception) {
		if (isLogEnabled) {
			getInstance().e(logTag, logText, exception);
		}
	}

	public static void w(String logTag, String logText) {
		if (isLogEnabled) {
			getInstance().w(logTag, logText);
		}
	}

	public static void w(String logTag, String logText, Throwable exception) {
		if (isLogEnabled) {
			getInstance().w(logTag, logText, exception);
		}
	}

	public static void v(String logTag, String logText) {
		if (isLogEnabled) {
			getInstance().v(logTag, logText);
		}
	}

	public static void i(String logTag, String logText) {
		if (isLogEnabled) {
			getInstance().i(logTag, logText);
		}
	}

	public static void printStackTrace(Throwable exception) {
		if (isLogEnabled) {
			exception.printStackTrace();
		}
	}

	public static void out(String message) {
		if (isLogEnabled) {
			System.out.println(message);
		}
	}

	public static void err(String message) {
		if (isLogEnabled) {
			System.err.println(message);
		}
	}
}
