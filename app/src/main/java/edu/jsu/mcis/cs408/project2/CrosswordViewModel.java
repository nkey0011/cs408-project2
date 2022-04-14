package edu.jsu.mcis.cs408.project2;

/*
Work Here
 */

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class
CrosswordViewModel extends ViewModel {

    private static final int WORD_DATA_FIELDS = 6;
    private static final int WORD_HEADER_FIELDS = 2;
    public static final char BLOCK_CHAR = '*';
    public static final char BLANK_CHAR = ' ';

    private static final String TAG = "CrosswordViewModel";

    private final MutableLiveData<HashMap<String, Word>> words = new MutableLiveData<>();

    private final MutableLiveData<char[][]> letters = new MutableLiveData<>();
    private final MutableLiveData<int[][]> numbers = new MutableLiveData<>();

    private final MutableLiveData<Integer> puzzleWidth = new MutableLiveData<>();
    private final MutableLiveData<Integer> puzzleHeight = new MutableLiveData<>();

    private final MutableLiveData<String> cluesAcross = new MutableLiveData<>();
    private final MutableLiveData<String> cluesDown = new MutableLiveData<>();

    // Initialize Shared Model

    public void init(Context context) {

        if (words.getValue() == null) {
            loadWords(context);
            addAllWordsToGrid(); // for testing only; remove later!
        }

    }

    // Add Word to Grid

    private void addWordToGrid(String key) {

        // Get word from collection (look up using the given key)

        Word word = Objects.requireNonNull(words.getValue()).get(key);

        // Was the word found in the collection?

        if (word != null) {

            // If so, get properties (row, column, and the word itself)

            int row = word.getRow();
            int column = word.getColumn();
            String w = word.getWord();

            // Add word to Letters array, one character at a time

            for (int i = 0; i < w.length(); i++){

                letters.getValue()[row][column] = w.charAt(i);

                if ( word.isAcross() )
                    column++;
                else if ( word.isDown() )
                    row++;

            }

        }

    }

    // Add all words to grid (for testing purposes only!)

    private void addAllWordsToGrid() {
        for (Map.Entry<String, Word> e : Objects.requireNonNull(words.getValue()).entrySet()) {
            //addWordToGrid( e.getKey() );
        }
    }

    // Load game data from puzzle file ("puzzle.csv")

    private void loadWords(Context context) {

        HashMap<String, Word> map = new HashMap<>();
        StringBuilder clueAcrossBuffer = new StringBuilder();
        StringBuilder clueDownBuffer = new StringBuilder();

        // Open puzzle file

        int id = R.raw.puzzle;
        BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(id)));

        try {

            String line = br.readLine();
            String[] fields = line.trim().split("\t");

            // Is first row of puzzle file a valid header?

            if (fields.length == WORD_HEADER_FIELDS) {

                // If so, get puzzle height and width from header

                int height = Integer.parseInt(fields[0]);
                int width = Integer.parseInt(fields[1]);

                //Log.d(TAG, "Height: " + height + ", Width: " + width);

                // Initialize letter and number arrays

                char[][] lArray = new char[height][width];
                int[][] nArray = new int[height][width];

                for (int i = 0; i < height; ++i) {
                    for (int j = 0; j < width; ++j) {
                        lArray[i][j] = BLOCK_CHAR;
                        nArray[i][j] = 0;
                    }
                }

                // Read game data (remainder of puzzle file)

                while ((line = br.readLine()) != null) {

                    // Get word fields from next row

                    fields = line.trim().split("\t");

                    // Is this a valid word?

                    if (fields.length == WORD_DATA_FIELDS) {

                        // If so, initialize new word

                        Word word = new Word(fields);

                        // Get row and column

                        int row = word.getRow();
                        int column = word.getColumn();

                        // Add box number

                        nArray[row][column] = word.getBox();

                        // Clear grid squares

                        String w = word.getWord();

                        int r = row;
                        int c = column;

                        for (int i = 0; i < w.length(); i++) {

                            lArray[r][c] = BLANK_CHAR;

                            if ( word.isAcross() )
                                c++;
                            else if ( word.isDown() )
                                r++;

                        }

                        // Append Clue to StringBuilder (either clueAcrossBuffer or clueDownBuffer)

                        String clue = word.getClue();
                        WordDirection direction = word.getDirection();


                        if (direction == WordDirection.ACROSS) {
                            clueAcrossBuffer.append(nArray[row][column]).append(": ").append(clue).append("\n");
                        }
                        else if  (direction == WordDirection.DOWN) {
                            clueDownBuffer.append(nArray[row][column]).append(": ").append(clue).append("\n");
                        }

                        // Create unique key; add word to collection

                        String key = word.getBox() + word.getDirection().toString().toUpperCase();
                        map.put(key, word);


                    }

                }

                // Initialize MutableLiveData Members

                Log.e(TAG, "Height: " + height + ", Width: " + width);

                words.setValue(map);

                puzzleHeight.setValue(height);
                puzzleWidth.setValue(width);

                letters.setValue(lArray);
                numbers.setValue(nArray);

                cluesAcross.setValue(clueAcrossBuffer.toString());
                cluesDown.setValue(clueDownBuffer.toString());

            }

            br.close();

        }
        catch (Exception e) { Log.e(TAG, e.toString()); }

    }

    // Getter Methods

    public LiveData<char[][]> getLetters() { return letters; }

    public LiveData<int[][]> getNumbers() { return numbers; }

    public LiveData<String> getCluesAcross() { return cluesAcross; }

    public LiveData<String> getCluesDown() { return cluesDown; }

    public LiveData<Integer> getPuzzleWidth() { return puzzleWidth; }

    public LiveData<Integer> getPuzzleHeight() { return puzzleHeight; }

    // add getWord() and getNumber()
    public int getBoxNumber(int row, int column) {
        return Objects.requireNonNull(numbers.getValue())[row][column];
    }

    public int getNumber(int row, int column){
        return Objects.requireNonNull(numbers.getValue())[row][column];
    }


    public Word getWord(int box, String word) {
        // get word from collection at the key (should be uppercased)


        return Objects.requireNonNull(words.getValue())[box][word];


    }






    // add booleon method to count # of blank squares left
}