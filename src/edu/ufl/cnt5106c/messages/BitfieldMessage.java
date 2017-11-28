package edu.ufl.cnt5106c.messages;

import java.nio.ByteBuffer;

/**
 * Created by sayak on 10/26/17.
 */
public class BitfieldMessage extends Message {
    public static byte[] getMessage(boolean[] hasPieces){
        int messageLength = (int) Math.ceil(hasPieces.length / 8.0);
        int messageType = 5;
        byte[] message = new byte[(5 + messageLength)];
        byte[] messageLengthField = ByteBuffer.allocate(4).putInt(messageLength).array();
        int i = 0;
        for(i = 0; i < 4; i++) {
            message[i] = messageLengthField[i];
        }
        message[i++] = (byte) messageType;
        int power = 7;
        for(int k = 1; k <= hasPieces.length; k++)
        {
            if(hasPieces[k - 1]){
                message[i] |= (byte) Math.pow(2, power);
            }
            if(k % 8 == 0)
            {
                i++;
                power = 8;
            }
            power--;
        }
        return message;
    }
    public static boolean[] getBitFieldPieces(byte message[])//Added by Ankit
    {
        int len=message.length;
        int payload_len = len-5;
        boolean pieces[] = new boolean[payload_len*8];
        int z=7;
        int k=5;
        for(int i=0;k<message.length;i++){
            if((message[k] & (byte)Math.pow(2, z)) != 0){
                pieces[i]=true;
            }
            z--;
            if(z<0){
                k++;
                z=7;
            }
        }
        return pieces;

    }
}
