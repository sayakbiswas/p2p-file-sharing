package edu.ufl.cnt5106c.peer;

import java.util.Arrays;

/**
 * Created by sayak on 10/26/17.
 */
class Piece {
    byte[] bytes;
    public Piece(byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }
}
