//Pokemon Ranger Mission Inserter

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PokemonRangerMissionInserter {
	public static void main(String[] args) {
		System.out.println("* Pokemon Ranger Mission Inserter *");
		if (args.length == 0 || args.length == 3) {
			Scanner input = new Scanner(System.in);
			String inputSaveFileLocation;
			if (args.length == 0) {
				System.out.print("Enter the input save file's location: ");
				inputSaveFileLocation = input.nextLine();
			} else {
				inputSaveFileLocation = args[0];
			}
			String gameOption;
			if (args.length == 0) {
				System.out.println("Select the save file's game:");
				System.out.println("1) Pokemon Ranger: Shadows of Almia");
				System.out.println("2) Pokemon Ranger: Guardian Signs");
				System.out.print("Game option: ");
				gameOption = input.nextLine();
			} else {
				gameOption = args[1];
			}
			String outputSaveFileLocation;
			if (args.length == 0) {
				System.out.print("Enter the output save file's location: ");
				outputSaveFileLocation = input.nextLine();
			} else {
				outputSaveFileLocation = args[2];
			}
			input.close();
			try {
				FileInputStream inputStream = new FileInputStream(inputSaveFileLocation);
				int token;
				ArrayList<Integer> inputSaveFile = new ArrayList<>();
				do {
					try {
						token = inputStream.read();
					} catch (Exception e) {
						token = -1;
					}
					if (token >= 0 && token <= 255) {
						inputSaveFile.add(token);
					}
				} while (token >= 0 && token <= 255);
				inputStream.close();
				int inputSaveFileSize = 262144;
				if (inputSaveFile.size() == inputSaveFileSize) {
					if (isValidInteger(gameOption) &&
							(Integer.parseInt(gameOption) == 1 || Integer.parseInt(gameOption) == 2)) {
						int gameIndex = Integer.parseInt(gameOption) - 1;
						String[] insertionFileNames = {"RangerNetMissions2.bin", "RangerNetMissions3.bin"};
						try {
							inputStream = new FileInputStream(insertionFileNames[gameIndex]);
							ArrayList<Integer> insertionFile = new ArrayList<>();
							do {
								try {
									token = inputStream.read();
								} catch (Exception e) {
									token = -1;
								}
								if (token >= 0 && token <= 255) {
									insertionFile.add(token);
								}
							} while (token >= 0 && token <= 255);
							inputStream.close();
							int[] insertionFileSizes = {148034, 153344};
							if (insertionFile.size() == insertionFileSizes[gameIndex]) {
								try {
									FileOutputStream outputStream = new FileOutputStream(outputSaveFileLocation);
									for (int i = 0; i < inputSaveFile.size() - insertionFile.size(); i++) {
										outputStream.write(inputSaveFile.get(i));
									}
									for (int i = 0; i < insertionFile.size(); i++) {
										outputStream.write(insertionFile.get(i));
									}
									outputStream.close();
									System.out.println("Success: " + outputSaveFileLocation + " was created!");
								} catch (Exception e) {
									System.out.println("Error: Could not create " + outputSaveFileLocation + ".");
								}
							} else {
								System.out.println("Error: " + insertionFileNames[gameIndex] + " is not " +
										insertionFileSizes[gameIndex] + " bytes in size.");
							}
						} catch (Exception e) {
							System.out.println("Error: " + insertionFileNames[gameIndex] + " not found.");
						}
					} else {
						System.out.println("Error: Invalid game option.");
					}
				} else {
					System.out.println("Error: " + inputSaveFileLocation + " is not " + inputSaveFileSize +
							" bytes in size.");
				}
			} catch (Exception e) {
				System.out.println("Error: " + inputSaveFileLocation + " not found.");
			}
		} else {
			System.out.println("This program's usage is as follows:");
			System.out.println("java PokemonRangerMissionInserter");
			System.out.println("java PokemonRangerMissionInserter <input save file location> <game option> " +
					"<output save file location>");
		}
	}
	
	private static boolean isValidInteger(String string) {
		if (string.length() >= 2 && string.length() <= 10 && string.charAt(0) == '-') {
			for (int i = 1; i < string.length(); i++) {
				if (string.charAt(i) < '0' || string.charAt(i) > '9') {
					return false;
				}
			}
			return true;
		} else if (string.length() >= 1 && string.length() <= 9) {
			for (int i = 0; i < string.length(); i++) {
				if (string.charAt(i) < '0' || string.charAt(i) > '9') {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	private static void error(String message) {
		System.out.println("Error: " + message);
		System.exit(1);
	}
}
