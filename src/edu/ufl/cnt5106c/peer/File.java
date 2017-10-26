package edu.ufl.cnt5106c.peer;

import edu.ufl.cnt5106c.config.CommonConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by sayak on 10/26/17.
 */
class File {
    private Piece[] pieces;
    private int numberOfPieces;

    public File(CommonConfig commonConfig, boolean hasFile) {
        long fileSize = commonConfig.getSharedFileSize();
        int pieceSize = commonConfig.getSharedFilePieceSize();
        this.numberOfPieces = (int) Math.ceil((double) fileSize / (double) pieceSize);
        pieces = new Piece[this.numberOfPieces];
        if(hasFile) {
            String fileName = commonConfig.getSharedFileName();
            try {
                byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
                int i = 0, j = 0;
                while(i < allBytes.length){
                    byte bytes[]= Arrays.copyOfRange(allBytes, i, i + pieceSize);
                    i += pieceSize;
                    pieces[j++] = new Piece(bytes);
                }
            } catch (IOException ioException) {
                System.out.println("Exception while reading file");
                ioException.printStackTrace();
            }
        }
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }

    public Piece getPieceAtIndex(int index) {
        return pieces[index];
    }

    public void setPieceAtIndex(int index, Piece piece) {
        pieces[index] = piece;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }
}
