package com.github.mcbotty.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MCDataOutputStream extends DataOutputStream {

	public MCDataOutputStream(OutputStream arg0) {
		super(arg0);
	}
 
    public void writeVarInt(int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
              writeByte(paramInt);
              return;
            }
 
            writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

}
