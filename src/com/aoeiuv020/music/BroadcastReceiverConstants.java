/* ***************************************************
	^> File Name: BroadcastReceiverConstants.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/13 - 00:43:06
*************************************************** */
package com.aoeiuv020.music;
public class BroadcastReceiverConstants
{
	public static final String PACKAGE=BroadcastReceiverConstants.class.getPackage().getName();
	public static final String CONTROL=PACKAGE+"."+"control";
	//下面几个int都是ControlReceiver收的参数，
	public static final int PLAY=1;
	public static final int START=2;
	public static final int STOP=3;
	public static final int PAUSE=4;
	public static final int START_OR_PAUSE=5;
	public static final int STATUS_REQUIRE=6;
	public static final int METADATA_REQUIRE=7;
	public static final int SEEK=8;
	public static final String STATUS=PACKAGE+"."+"status";
	public static final String METADATA=PACKAGE+"."+"metadata";
	public static final String PROCESS=PACKAGE+"."+"process";
	public static final String TYPE="audio/*";
}
