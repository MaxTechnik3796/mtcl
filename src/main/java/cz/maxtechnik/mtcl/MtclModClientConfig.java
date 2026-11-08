package cz.maxtechnik.mtcl;

import net.neoforged.neoforge.common.ModConfigSpec;
public class MtclModClientConfig{
	private static final ModConfigSpec.Builder BUILDER=new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.BooleanValue
			DEBUG, DARKUP_CHAT_FILTER;
	static{
		DEBUG=BUILDER.define("debug",false);
		DARKUP_CHAT_FILTER=BUILDER.define("darkup_chat_filter",false);
		SPEC=BUILDER.build();
	}
}
