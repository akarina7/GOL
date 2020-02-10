package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static game.GameOfLife.CellState.*;

public class GameOfLifeTest {

  @Test
  public void Canary(){
    assertTrue(true);
  }

  @Test
  public void testDeadCellBehaviors() {
    assertAll(
      () -> assertEquals(DEAD, GameOfLife.nextState(DEAD, 0)),
      () -> assertEquals(DEAD, GameOfLife.nextState(DEAD, 1)),
      () -> assertEquals(DEAD, GameOfLife.nextState(DEAD, 2)),
      () -> assertEquals(DEAD, GameOfLife.nextState(DEAD, 5)),
      () -> assertEquals(DEAD, GameOfLife.nextState(DEAD, 8)),
      () -> assertEquals(ALIVE, GameOfLife.nextState(DEAD, 3))
    );
  }

  @Test
  public void testLiveCellBehaviors() {
    assertAll(
      () -> assertEquals(DEAD, GameOfLife.nextState(ALIVE, 1)),
      () -> assertEquals(DEAD, GameOfLife.nextState(ALIVE, 4)),
      () -> assertEquals(DEAD, GameOfLife.nextState(ALIVE, 8)),
      () -> assertEquals(ALIVE, GameOfLife.nextState(ALIVE, 2)),
      () -> assertEquals(ALIVE, GameOfLife.nextState(ALIVE, 3))
    );
  }                                     
  
  @Test
  public void testGenerateSignalsForOnePosition() {
    List<Point> points = List.of(new Point(2, 2), new Point(3, 2), new Point(4, 2),
                                 new Point(2, 3),                        new Point(4, 3),
                                 new Point(2, 4), new Point(3, 4), new Point(4, 4));

    assertEquals(points, GameOfLife.generateSignalForPositions(List.of(new Point(3, 3))));
  }

  @Test
  public void testGenerateSignalsForAnotherPosition() {
    List<Point> points = List.of(new Point(1, 3), new Point(2, 3), new Point(3, 3),
                                 new Point(1, 4),                        new Point(3, 4),
                                 new Point(1, 5), new Point(2, 5), new Point(3, 5));

    assertEquals(points, GameOfLife.generateSignalForPositions(List.of(new Point(2, 4))));
  }

  @Test
  public void testGenerateSignalsForPosition00() {
    List<Point> points = List.of(new Point(-1, -1), new Point(0, -1), new Point(1, -1),
                                 new Point(-1, 0),                          new Point(1, 0),
                                 new Point(-1, 1),  new Point(0, 1),  new Point(1, 1));

    assertEquals(points, GameOfLife.generateSignalForPositions(List.of(new Point(0, 0))));
  }

  @Test
  public void testGenerateSignalsForNumberInList() {
    assertAll(
      () -> assertEquals(0, GameOfLife.generateSignalForPositions(List.of()).size()),
      () -> assertEquals(8, GameOfLife.generateSignalForPositions(List.of(new Point(3, 3))).size()),
      () -> assertEquals(16, GameOfLife.generateSignalForPositions(List.of(new Point(3, 3), new Point(2, 4))).size()),
      () -> assertEquals(24, GameOfLife.generateSignalForPositions(List.of(new Point(3, 3), new Point(2, 4), new Point(0, 0))).size())
    );
  }

  @Test
  public void testCountSignals() {
    assertAll(
      () -> assertEquals(Map.of(), GameOfLife.countSignals(List.of())),
      () -> assertEquals(Map.of(new Point(3, 3), 1), GameOfLife.countSignals(List.of(new Point(3, 3)))),
      () -> assertEquals(Map.of(new Point(3, 3), 2), GameOfLife.countSignals(List.of(new Point(3, 3), new Point(3, 3)))),
      () -> assertEquals(Map.of(new Point(3, 3), 2, new Point(2, 4), 1), GameOfLife.countSignals(List.of(new Point(3, 3), new Point(2, 4), new Point(3, 3))))
    );
  }

  public class PointsComparator implements Comparator<Point> {
    @Override
    public int compare(Point a, Point b)
    {
      return Integer.compare(a.x, b.x);
    }
  }

  @Test
  public void testNextGeneration() {
   assertAll(
      () -> assertEquals(List.of(), GameOfLife.nextGeneration(List.of())),
      () -> assertEquals(List.of(), GameOfLife.nextGeneration(List.of(new Point(3, 3)))),
      () -> assertEquals(List.of(), GameOfLife.nextGeneration(List.of(new Point(2, 3), new Point(2, 4)))),
      () -> assertEquals(List.of(new Point(2, 1)), GameOfLife.nextGeneration(List.of(new Point(1, 1), new Point(1, 2), new Point(3, 0)))),
      () -> assertEquals(List.of(new Point(1, 1), new Point(1, 2), new Point(2, 1), new Point(2, 2))
                        .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()),
                        GameOfLife.nextGeneration(List.of(new Point(1, 1), new Point(1, 2), new Point(2, 2)))
                                .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()))
    );
  }

  @Test
  public void testPatterns() {
    assertAll(
      // block
      () -> assertEquals(List.of(new Point(2,2), new Point(3,2), new Point(2,3), new Point(3,3))
                        .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()),
                        GameOfLife.nextGeneration(List.of(new Point(2,2), new Point(3,2), new Point(2,3), new Point(3,3)))
                                .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList())),
      // beehive
      () -> assertEquals(List.of(new Point(4,4), new Point(5,3), new Point(5,5), new Point(6,3), new Point(6,5), new Point(7,4))
                        .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()),
                        GameOfLife.nextGeneration(List.of(new Point(4,4), new Point(5,3), new Point(5,5), new Point(6,3), new Point(6,5), new Point(7,4)))
                                .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList())),
      // horizontal blinker
      () -> assertEquals(List.of(new Point(4, 2), new Point(4, 3), new Point(4, 4))
                        .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()),
                        GameOfLife.nextGeneration(List.of(new Point(3, 3), new Point(4, 3), new Point(5, 3)))
                                .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList())),
      // vertical blinker
      () -> assertEquals(List.of(new Point(3, 3), new Point(4, 3), new Point(5, 3))
                        .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()),
                        GameOfLife.nextGeneration(List.of(new Point(4, 2), new Point(4, 3), new Point(4, 4)))
                                .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList())),
      // glider
      () -> assertEquals(List.of(new Point(4, 3), new Point(4, 4), new Point(5, 3), new Point(5, 2), new Point(3, 2))
                        .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()),
                        GameOfLife.nextGeneration(List.of(new Point(3, 3), new Point(4, 3), new Point(5, 3), new Point(5, 2), new Point(4, 1)))
                                .stream().sorted(Comparator.comparing(Point::getX).thenComparing(Point::getY)).collect(Collectors.toList()))
    );
  }

}
