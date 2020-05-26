//Pokemon Ranger Mission Inserter

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PokemonRangerMissionInserter {
	public static void main(String[] args) {
		System.out.println("* Pokemon Ranger Mission Inserter *");
		if (args.length != 0 && args.length != 3) {
			error("This program's usage is as follows:\n" +
					"java PokemonRangerMissionInserter\n" +
					"java PokemonRangerMissionInserter <input save file location> <game option> " +
					"<output save file location>");
		}
		Scanner input = new Scanner(System.in);
		String inputSaveFileLocation;
		if (args.length == 0) {
			System.out.print("Enter the input save file's location: ");
			inputSaveFileLocation = input.nextLine();
		} else {
			inputSaveFileLocation = args[0];
		}
		FileInputStream inputStream1 = null;
		try {
			inputStream1 = new FileInputStream(inputSaveFileLocation);
		} catch (Exception e) {
			error(inputSaveFileLocation + " is not a valid file.");
		}
		String gameString;
		if (args.length == 0) {
			System.out.println("Select the save file's game:");
			System.out.println("1) Pokemon Ranger: Shadows of Almia");
			System.out.println("2) Pokemon Ranger: Guardian Signs");
			System.out.print("Game option: ");
			gameString = input.nextLine();
		} else {
			gameString = args[1];
		}
		Scanner lineInput = new Scanner(gameString);
		if (!lineInput.hasNextInt()) {
			error("Invalid game input.");
		}
		int gameOption = lineInput.nextInt();
		if (gameOption < 1 || gameOption > 2) {
			error("Invalid game option.");
		}
		lineInput.close();
		int gameIndex = gameOption - 1;
		String outputSaveFileLocation;
		if (args.length == 0) {
			System.out.print("Enter the output save file's location: ");
			outputSaveFileLocation = input.nextLine();
		} else {
			outputSaveFileLocation = args[2];
		}
		File outputSaveFileDirectory = new File(outputSaveFileLocation).getParentFile();
		if (outputSaveFileDirectory != null && !outputSaveFileDirectory.exists() && outputSaveFileDirectory.mkdirs()) {
			System.out.println(outputSaveFileDirectory + " was created.");
		}
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(outputSaveFileLocation);
		} catch (Exception e) {
			error("Unable to create " + outputSaveFileLocation + ".");
		}
		input.close();
		int token1;
		ArrayList<Integer> inputSaveFile = new ArrayList<>();
		do {
			try {
				token1 = inputStream1.read();
			} catch (Exception e) {
				token1 = -1;
			}
			if (token1 >= 0 && token1 <= 255) {
				inputSaveFile.add(token1);
			}
		} while (token1 >= 0 && token1 <= 255);
		try {
			inputStream1.close();
		} catch (Exception e) {
			error("Unable to close inputStream1.");
		}
		int inputSaveFileSize = 262144;
		if (inputSaveFile.size() != inputSaveFileSize) {
			error(inputSaveFileLocation + " is not " + inputSaveFileSize + " bytes in size.");
		}
		String[] insertionFileLocations = {"RangerNetMissions2.bin", "RangerNetMissions3.bin"};
		FileInputStream inputStream2 = null;
		try {
			inputStream2 = new FileInputStream(insertionFileLocations[gameIndex]);
		} catch (Exception e) {
			error(insertionFileLocations[gameIndex] + " not found.");
		}
		int token2;
		ArrayList<Integer> insertionFile = new ArrayList<>();
		do {
			try {
				token2 = inputStream2.read();
			} catch (Exception e) {
				token2 = -1;
			}
			if (token2 >= 0 && token2 <= 255) {
				insertionFile.add(token2);
			}
		} while (token2 >= 0 && token2 <= 255);
		try {
			inputStream2.close();
		} catch (Exception e) {
			error("Unable to close inputStream2.");
		}
		int[] insertionFileSizes = {148034, 153344};
		if (insertionFile.size() != insertionFileSizes[gameIndex]) {
			error(insertionFileLocations[gameIndex] + " is not " + insertionFileSizes[gameIndex] + " bytes in size.");
		}
		for (int i = 0; i < inputSaveFile.size() - insertionFile.size(); i++) {
			try {
				outputStream.write(inputSaveFile.get(i));
			} catch (Exception e) {
				error("Unable to write the input save file to " + outputSaveFileLocation + ".");
			}
		}
		for (int i = 0; i < insertionFile.size(); i++) {
			try {
				outputStream.write(insertionFile.get(i));
			} catch (Exception e) {
				error("Unable to write the insertion file to " + outputSaveFileLocation + ".");
			}
		}
		try {
			outputStream.close();
		} catch (Exception e) {
			error("Unable to close outputStream.");
		}
		System.out.println("Success: " + outputSaveFileLocation + " was created!");
	}
	
	private static void error(String message) {
		System.out.println("Error: " + message);
		System.exit(1);
	}
}
