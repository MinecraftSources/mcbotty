package com.github.mcbotty.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MCDataInputStream extends DataInputStream {

	public MCDataInputStream(InputStream arg0) {
		super(arg0);
	}
	
	public int readVarInt() throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

}
