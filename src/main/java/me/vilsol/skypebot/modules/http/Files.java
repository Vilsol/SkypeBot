package me.vilsol.skypebot.modules.http;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public final class Files {

    /**
     * Prevents instantiation.
     */
    private Files() {
    }

    /**
     * Gets the differences between two files.
     *
     * @param file1 The to check the differences in from the other file.
     * @param file2 The file the first file compares to.
     * @return The lines where there is a difference.
     * @throws java.io.IOException
     */
    public static ArrayList<String[]> getDifference(File file1, File file2) throws IOException {
        ArrayList<String[]> difference = new ArrayList<>();

        BufferedReader reader1 = new BufferedReader(new FileReader(file1));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));

        ArrayList<String> lines1 = new ArrayList<>();
        ArrayList<String> lines2 = new ArrayList<>();

        String line;
        while ((line = reader1.readLine()) != null) {
            lines1.add(line);
        }
        while ((line = reader2.readLine()) != null) {
            lines2.add(line);
        }

        for (int lineNum = 0; lineNum <= Math.max(lines1.size(), lines2.size()); ++lineNum) {
            String line1 = lines1.get(lineNum);
            String line2 = lines2.get(lineNum);
            if (line1.equals(line2)) difference.set(lineNum, new String[]{line1, line2});
        }

        return difference;
    }

    /**
     * Downloads a file from a URL into a file Object.
     *
     * @param url The URL to read the bytes from.
     * @return The file the bytes were written to.
     * @throws java.io.IOException
     */
    public static File download(String url) throws IOException {
        ReadableByteChannel channel = Channels.newChannel(new URL(url).openStream());
        File outputFile = new File(url.split("/")[url.split("/").length - 1]);
        if (outputFile.exists()) outputFile.createNewFile();

        FileOutputStream output = new FileOutputStream(url.split("/")[url.split("/").length - 1]);
        output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

        return outputFile;
    }

    /**
     * Reads lines from a file, only requiring the file location and catches all IOExceptions.
     *
     * @param file The file location and name get the file from which to read the lines from.
     * @return The lines read from the file.
     */
    public static ArrayList<String> readLines(String file) {
        try {
            return readLines(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read lines from a file.
     *
     * @param file The file to read the lines from.
     * @return The lines read from the file.
     * @throws java.io.IOException
     */
    public static ArrayList<String> readLines(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        return readBuffer(reader);
    }

    /**
     * Reads lines from a BufferedReader.
     *
     * @param reader The BufferedReader to read lines from.
     * @return The lines in the BufferedReader.
     */
    public static ArrayList<String> readBuffer(BufferedReader reader) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            String line;
            while ((line = reader.readLine()) != null) lines.add(line + "\n");

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    /**
     * Writes a list of lines into a file.
     *
     * @param file  The file to write the lines into.
     * @param lines The lines to write into the file.
     * @return If the writing succeeded.
     */
    public static boolean writeLines(File file, List<String> lines) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (String line : lines) writer.write(line + System.lineSeparator());

            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
