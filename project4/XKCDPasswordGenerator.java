
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class XKCDPasswordGenerator {

    public static String specialCharacters = "~!@#$%^&*.:;";

    public static void main(String[] args) throws Exception {

        int num_words = 4;
        int num_caps = 0;
        int num_rand_numbers = 0;
        int num_rand_symbols = 0;
        int max_word_length = 16;
        String usage = "usage: xkcdpwgen [-h] [-w WORDS] [-c CAPS] [-n NUMBERS] [-s SYMBOLS]\n\n" +
                "Generate a secure, memorable password using the XKCD method\n\n" +
                "optional arguments:\n" +
                "    -h, --help            show this help message and exit\n" +
                "    -w WORDS, --words WORDS\n" +
                "                          include WORDS words in the password (default=4)\n" +
                "    -c CAPS, --caps CAPS  capitalize the first letter of CAPS random words\n" +
                "                          (default=0)\n" +
                "    -n NUMBERS, --numbers NUMBERS\n" +
                "                          insert NUMBERS random numbers in the password\n" +
                "                          (default=0)\n" +
                "    -s SYMBOLS, --symbols SYMBOLS\n" +
                "                          insert SYMBOLS random symbols in the password\n" +
                "                          (default=0)\n";

        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if (s.equals("-h") || s.equals("--help")) {
                System.out.println(usage);
                System.exit(0);
            } else if (s.equals("-w") || s.equals("--words")) {
                if (i + 1 < args.length) {
                    i++;
                    try {
                        num_words = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: -w or --words flag used incorrectly, expects a number\n");
                        System.out.println(usage);
                    }
                }
            } else if (s.equals("-c") || s.equals("--caps")) {
                if (i + 1 < args.length) {
                    i++;
                    try {
                        num_caps = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: -c or --caps flag used incorrectly, expects a number\n");
                        System.out.println(usage);
                    }
                }
            } else if (s.equals("-n") || s.equals("--numbers")) {
                if (i + 1 < args.length) {
                    i++;
                    try {
                        num_rand_numbers = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: -n or --numbers flag used incorrectly, expects a number\n");
                        System.out.println(usage);
                    }
                }
            } else if (s.equals("-s") || s.equals("--symbols")) {
                if (i + 1 < args.length) {
                    i++;
                    try {
                        num_rand_symbols = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: -s or --symbols flag used incorrectly, expects a number\n");
                        System.out.println(usage);
                    }
                }
            } else {
                System.out.printf("ERROR: Flag \"%s\" not recognized\n", args[i]);
                System.out.println(usage);
                System.exit(1);
            }
        }

        RandomAccessFile file = new RandomAccessFile("words.txt", "r");

        long length = file.length();
        Random random = new Random();
        ArrayList<String> output = new ArrayList<String>();

        // add random words
        for (int i = 0; i < num_words; i++) {
            long location = (long) random.nextInt((int) length - max_word_length);
            file.seek(location);
            file.readLine();
            output.add(file.readLine());
        }
        Collections.shuffle(output);

        // randomly capitalize words
        for (int i = 0; i < Math.min(num_caps, output.size()); i++) {
            String capitalized = output.get(i).substring(0, 1).toUpperCase()
                    + output.get(i).substring(1, output.get(i).length());
            output.set(i, capitalized);
        }
        Collections.shuffle(output);

        // randomly add numbers
        for (int i = 0; i < num_rand_numbers; i++) {
            // random roll to decide if at begining or end of word
            String numString = output.get(i % output.size());
            if (Math.random() > 0.5) {
                numString = numString + random.nextInt(10);
            } else {
                numString = random.nextInt(10) + numString;
            }
            output.set(i % output.size(), numString);
        }
        Collections.shuffle(output);

        // randomly add numbers
        for (int i = 0; i < num_rand_symbols; i++) {
            // random roll to decide if at begining or end of word
            String numString = output.get(i % output.size());
            int randomSymbolIndex = random.nextInt(12);
            if (Math.random() > 0.5) {
                numString = numString + XKCDPasswordGenerator.specialCharacters.charAt(randomSymbolIndex);
            } else {
                numString = XKCDPasswordGenerator.specialCharacters.charAt(randomSymbolIndex) + numString;
            }
            output.set(i % output.size(), numString);
        }
        Collections.shuffle(output);

        System.out.println(output);

        file.close();
    }

}