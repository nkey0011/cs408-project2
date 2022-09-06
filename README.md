# cs408-project2
Project 2 for App Development with AndroidStudio.
This project is a crossword puzzle game. 
There are two tabs, the first one displaying a typical game board, and the second one is a hint tab.
Each square containing a number is a dynamically created onClick button, so to play the user must select a numbered square.
Once selected, an alert box containing an EditText box will pop up for user to enter a guess.
After the word guess is entered, and the enter button has been selected, the user's input is taken and compared to the word csv file.
The program compares the user input to both across and down, and if the guess is correct, will place the word into the correct spot in the puzzle.
Once the board is filled, a winning condition method is invoked to display a congratulations toast. 
