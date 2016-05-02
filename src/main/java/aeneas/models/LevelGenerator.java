package aeneas.models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import aeneas.models.Bullpen.BullpenLogic;

import javafx.scene.paint.Color;

public class LevelGenerator {

  private static int levelNumber = 1;

  public static void main(String[] args) {
    File file = new File("build/levels/default/");
    file.mkdirs();

    for (Level l : generateDefaultLevels()) {
      saveNewLevel(l);
    }
  }

  public static ArrayList<Level> generateDefaultLevels() {
    ArrayList<Level> levels = new ArrayList<Level>();

    levels.add(newPuzzle(30, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, }));
    levels.add(newLightning(100, new int[] { 8, 6, 4, 11, 15 }));
    ArrayList<ReleaseNumber> nums = new ArrayList<ReleaseNumber>();
    nums.add(new ReleaseNumber(0, 0, Color.RED, 1));
    nums.add(new ReleaseNumber(1, 8, Color.RED, 2));
    nums.add(new ReleaseNumber(9, 2, Color.RED, 3));
    nums.add(new ReleaseNumber(5, 11, Color.RED, 4));
    nums.add(new ReleaseNumber(4, 0, Color.RED, 6));
    levels.add(newRelease(16, new int[] { 0, 28, 22, 13, 15, 10 }, nums));

    levels.add(newPuzzle(30,
        new int[] { 4, 0, 12, 2, 30, 10, 23, 5, 14, 22, 11, 6, 7, 24, 5, 10, 12,
            15, 1, 21, 2, 3, 4, 5, 8, 7, 28, 29, 10, 34, 12, 13, 14, 15, 12, 14,
            27, 2, 5 }));
    levels.add(newLightning(95, new int[] { 2, 0, 1, 10, 14 }));
    ArrayList<ReleaseNumber> nums2 = new ArrayList<ReleaseNumber>();
    nums2.add(new ReleaseNumber(0, 11, Color.BLUE, 1));
    nums2.add(new ReleaseNumber(5, 6, Color.BLUE, 2));
    nums2.add(new ReleaseNumber(8, 6, Color.BLUE, 3));
    nums2.add(new ReleaseNumber(2, 4, Color.BLUE, 4));
    nums2.add(new ReleaseNumber(0, 1, Color.BLUE, 6));
    nums2.add(new ReleaseNumber(10, 0, Color.RED, 1));
    nums2.add(new ReleaseNumber(2, 1, Color.RED, 2));
    nums2.add(new ReleaseNumber(6, 9, Color.RED, 3));
    nums2.add(new ReleaseNumber(1, 2, Color.RED, 4));
    nums2.add(new ReleaseNumber(4, 0, Color.RED, 5));
    nums2.add(new ReleaseNumber(0, 3, Color.RED, 6));
    levels.add(newRelease(12,
        new int[] { 0, 30, 34, 3, 17, 25, 28, 19, 4, 2, 12, 15, 13, 22 },
        nums2));

    levels.add(newPuzzle(30,
        new int[] { 10, 11, 2, 14, 22, 21, 0, 2, 6, 3, 4, 27, 5, 26, 7, 12, 8,
            1, 9, 14, 1, 4, 0, 6, 25, 2, 0, 12, 6, 4, 12, 4, 23, 24, 12, 12, 12,
            31, 34 }));
    levels.add(newLightning(90, new int[] { 1, 6, 4, 11, 13 }));
    ArrayList<ReleaseNumber> nums3 = new ArrayList<ReleaseNumber>();
    nums3.add(new ReleaseNumber(0, 0, Color.GREEN, 3));
    levels.add(newRelease(12, new int[] { 10, 11, 2, 14, 2 }, nums3));

    levels.add(newPuzzle(25,
        new int[] { 31, 4, 15, 9, 26, 5, 34, 8, 9, 7, 9, 32, 3, 8, 4, 6, 26, 4,
            33, 2, 4, 6, 15, 16, 23, 32, 1, 2, 5, 8, 33, 25, 23, 20, 14, 7, 0,
            0, 10, 11, 12, 13, 17, 18, 19 }));
    levels.add(newLightning(85, new int[] { 0, 3, 3, 11, 12 }));
    ArrayList<ReleaseNumber> nums4 = new ArrayList<ReleaseNumber>();
    nums4.add(new ReleaseNumber(1, 0, Color.ORANGE, 1));
    nums4.add(new ReleaseNumber(5, 5, Color.ORANGE, 2));
    nums4.add(new ReleaseNumber(9, 5, Color.ORANGE, 3));
    nums4.add(new ReleaseNumber(4, 4, Color.ORANGE, 4));
    nums4.add(new ReleaseNumber(4, 5, Color.ORANGE, 5));
    nums4.add(new ReleaseNumber(3, 1, Color.ORANGE, 6));
    nums4.add(new ReleaseNumber(8, 0, Color.YELLOW, 1));
    nums4.add(new ReleaseNumber(3, 2, Color.YELLOW, 2));
    nums4.add(new ReleaseNumber(6, 10, Color.YELLOW, 3));
    nums4.add(new ReleaseNumber(1, 3, Color.YELLOW, 4));
    nums4.add(new ReleaseNumber(4, 1, Color.YELLOW, 5));
    nums4.add(new ReleaseNumber(0, 4, Color.YELLOW, 6));
    nums4.add(new ReleaseNumber(9, 1, Color.RED, 1));
    nums4.add(new ReleaseNumber(1, 2, Color.RED, 2));
    nums4.add(new ReleaseNumber(7, 10, Color.RED, 3));
    nums4.add(new ReleaseNumber(2, 3, Color.RED, 4));
    nums4.add(new ReleaseNumber(5, 1, Color.RED, 5));
    nums4.add(new ReleaseNumber(1, 4, Color.RED, 6));
    levels.add(newRelease(10,
        new int[] { 4, 13, 7, 4, 8, 5, 9, 11, 8, 4, 3, 6, 10, 4, 9, 0, 8, 8, 26,
            14, 7, 4, 30, 7, 31, 19, 0, 4, 7, 20, 21, 22, 23, 24, 25, 26, 29,
            30, 31, 32, 33, 34 },
        nums4));

    levels.add(newPuzzle(25,
        new int[] { 4, 2, 12, 2, 2, 10, 3, 5, 14, 12, 11, 6, 7, 5, 5, 10, 12,
            15, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 12, 14, 3,
            2, 5 }));
    levels.add(newLightning(80, new int[] { 1, 4, 1, 11, 13 }));
    ArrayList<ReleaseNumber> nums5 = new ArrayList<ReleaseNumber>();
    nums5.add(new ReleaseNumber(3, 6, Color.CYAN, 1));
    nums5.add(new ReleaseNumber(0, 1, Color.CYAN, 2));
    nums5.add(new ReleaseNumber(4, 6, Color.CYAN, 5));
    nums5.add(new ReleaseNumber(4, 4, Color.CYAN, 6));
    nums5.add(new ReleaseNumber(5, 5, Color.GREEN, 1));
    nums5.add(new ReleaseNumber(8, 7, Color.GREEN, 2));
    nums5.add(new ReleaseNumber(4, 8, Color.GREEN, 3));
    nums5.add(new ReleaseNumber(10, 5, Color.GREEN, 4));
    nums5.add(new ReleaseNumber(0, 2, Color.GREEN, 5));
    nums5.add(new ReleaseNumber(5, 2, Color.GREEN, 6));
    nums5.add(new ReleaseNumber(3, 1, Color.RED, 1));
    nums5.add(new ReleaseNumber(7, 8, Color.RED, 2));
    nums5.add(new ReleaseNumber(8, 7, Color.RED, 3));
    nums5.add(new ReleaseNumber(8, 5, Color.RED, 4));
    nums5.add(new ReleaseNumber(5, 1, Color.RED, 5));
    nums5.add(new ReleaseNumber(7, 7, Color.RED, 6));
    nums5.add(new ReleaseNumber(0, 3, Color.WHITE, 1));
    nums5.add(new ReleaseNumber(2, 8, Color.WHITE, 2));
    nums5.add(new ReleaseNumber(11, 0, Color.WHITE, 3));
    nums5.add(new ReleaseNumber(2, 0, Color.WHITE, 4));
    nums5.add(new ReleaseNumber(4, 9, Color.WHITE, 5));
    nums5.add(new ReleaseNumber(3, 2, Color.WHITE, 6));
    levels.add(newRelease(10,
        new int[] { 11, 0, 7, 6, 14, 29, 5, 16, 7, 9, 5, 28, 3, 5, 4, 29, 4, 0,
            7, 18, 11, 5, 29, 5, 29, 31, 9, 13, 28, 0, 6, 21, 34, 31, 4, 3, 8,
            21, 17, 23, 4, 8, 7, 5, 24, 0, 9, 6, 11, 9, 3, 9, 21, 6, 7, 12, 7,
            2, 8, 31, 4, 7, 7, 2, 2, 7, 1, 8, 5, 2, 7, 5, 6, 2, 1, 5, 1, 18 },
        nums5));

    return levels;
  }

  private static ReleaseLevel newRelease(int moves, int[] pieceIndeces,
      ArrayList<ReleaseNumber> release_numbers) {
    Bullpen bullpen = new Bullpen(BullpenLogic.releaseLogic());
    addPieces(bullpen, pieceIndeces);
    ReleaseBoard board = new ReleaseBoard(release_numbers);
    ReleaseLevel l = new ReleaseLevel(bullpen, board);
    l.setAllowedMoves(moves);
    return l;
  }

  private static LightningLevel newLightning(int time, int[] pieceIndeces) {
    Bullpen bullpen = new Bullpen(BullpenLogic.lightningLogic());
    addPieces(bullpen, pieceIndeces);
    LightningLevel l = new LightningLevel(bullpen, time);
    return l;
  }

  private static PuzzleLevel newPuzzle(int moves, int[] pieceIndeces) {
    Bullpen bullpen = new Bullpen(BullpenLogic.puzzleLogic());
    addPieces(bullpen, pieceIndeces);
    PuzzleLevel l = new PuzzleLevel(bullpen, new PuzzleBoard());
    l.setAllowedMoves(moves);
    return l;
  }

  private static void addPieces(Bullpen bullpen, int[] pieceIndeces) {
    Piece[] pieces = PieceFactory.getPieces();
    for (int i : pieceIndeces) {
      bullpen.addPiece(pieces[i].clone());
    }
  }

  private static void saveNewLevel(Level l) {
    String path = "build/levels/default/" + levelNumber + ".ksb";
    File file = new File(path);
    levelNumber++;
    try {
      l.save(file);
      System.out.println("wrote level: " + path);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
