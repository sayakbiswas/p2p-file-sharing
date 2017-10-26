package edu.ufl.cnt5106c.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sayak on 10/26/17.
 */
public class CommonConfig {
    private int numberOfPreferredNeighbors;
    private float unchokingInterval;
    private float optimisticUnchokingInterval;
    private String sharedFileName;
    private long sharedFileSize;
    private int sharedFilePieceSize;

    public int getNumberOfPreferredNeighbors() {
        return numberOfPreferredNeighbors;
    }

    public void setNumberOfPreferredNeighbors(int numberOfPreferredNeighbors) {
        this.numberOfPreferredNeighbors = numberOfPreferredNeighbors;
    }

    public float getUnchokingInterval() {
        return unchokingInterval;
    }

    public void setUnchokingInterval(float unchokingInterval) {
        this.unchokingInterval = unchokingInterval;
    }

    public float getOptimisticUnchokingInterval() {
        return optimisticUnchokingInterval;
    }

    public void setOptimisticUnchokingInterval(float optimisticUnchokingInterval) {
        this.optimisticUnchokingInterval = optimisticUnchokingInterval;
    }

    public String getSharedFileName() {
        return sharedFileName;
    }

    public void setSharedFileName(String sharedFileName) {
        this.sharedFileName = sharedFileName;
    }

    public long getSharedFileSize() {
        return sharedFileSize;
    }

    public void setSharedFileSize(long sharedFileSize) {
        this.sharedFileSize = sharedFileSize;
    }

    public int getSharedFilePieceSize() {
        return sharedFilePieceSize;
    }

    public void setSharedFilePieceSize(int sharedFilePieceSize) {
        this.sharedFilePieceSize = sharedFilePieceSize;
    }

    public CommonConfig(String configFile) {
        try {
            loadCommonConfig(configFile);
        } catch (Exception e) {
            System.out.println("Error initializing CommonConfig");
            e.printStackTrace();
        }
    }

    public void loadCommonConfig(String configFile) throws IOException, Exception { //TODO: Handle this
        BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
        String line = "";
        while((line = bufferedReader.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            switch (tokens[0]) {
                case "NumberOfPreferredNeighbors":
                    setNumberOfPreferredNeighbors(Integer.parseInt(tokens[1]));
                    break;
                case "UnchokingInterval":
                    setUnchokingInterval(Float.parseFloat(tokens[1]));
                    break;
                case "OptimisticUnchokingInterval":
                    setOptimisticUnchokingInterval(Float.parseFloat(tokens[1]));
                    break;
                case "FileName":
                    setSharedFileName(tokens[1]);
                    break;
                case "FileSize":
                    setSharedFileSize(Long.parseLong(tokens[1]));
                    break;
                case "PieceSize":
                    setSharedFilePieceSize(Integer.parseInt(tokens[1]));
                    break;
                default:
                    throw new Exception("Unknown configuration option in Common.cfg"); //TODO: Change to a custom exception
            }
        }
    }
}
