package muskala.parallellzw;

import java.lang.instrument.Instrumentation;

/**
 * Created by Marcin on 16.04.2016.
 */
public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
	instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
	return instrumentation.getObjectSize(o);
    }
}
