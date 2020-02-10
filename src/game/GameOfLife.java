package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameOfLife {

  public enum CellState { ALIVE, DEAD }

  public static CellState nextState(CellState currentState, int numberOfLiveNeighbors) {
    return (numberOfLiveNeighbors == 3 || currentState == CellState.ALIVE && numberOfLiveNeighbors == 2) ? CellState.ALIVE : CellState.DEAD;
  }
  
  public static List<Point> generateSignalForPositions(List<Point> positions) {
    List<Point> signals = new ArrayList<>();

    for(Point position: positions) {
      int x = position.x;
      int y = position.y;

      signals.addAll(List.of(new Point(x - 1, y - 1), new Point(x, y - 1), new Point(x + 1, y - 1),
                             new Point(x - 1, y),                                new Point(x + 1, y),
                             new Point(x - 1, y + 1), new Point(x, y + 1), new Point(x + 1, y + 1)));
    }

    return signals;
  }

  public static Map<Point, Integer> countSignals(List<Point> positions) {
    Map<Point, Integer> counts = new HashMap<>();

    for(Point position: positions) {
      if(counts.containsKey(position))
        counts.replace(position, counts.get(position) + 1);
      else
        counts.put(position, 1);
    }

    return counts;
  }

  public static List<Point> nextGeneration(List<Point> currentGeneration) {
    List<Point> nextGeneration = new ArrayList<>();

    Map<Point, Integer> cells = countSignals(generateSignalForPositions(currentGeneration));

    for(Point cell: cells.keySet()) {
      if(nextState(currentGeneration.contains(cell) ? CellState.ALIVE : CellState.DEAD, cells.get(cell)) == CellState.ALIVE)
        nextGeneration.add(cell);
    }

    return nextGeneration;
  }

}
