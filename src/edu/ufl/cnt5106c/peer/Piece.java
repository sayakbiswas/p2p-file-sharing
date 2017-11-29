package edu.ufl.cnt5106c.peer;

import java.util.Arrays;

/**
 * Created by sayak on 10/26/17.
 */
class Piece {
    private byte[] data;
    public Piece(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public byte[] getData() {
        return this.data;
    }
}
