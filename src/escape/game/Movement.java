package escape.game;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import escape.exception.EscapeException;
import escape.required.Coordinate.CoordinateType;
import escape.required.EscapePiece.MovementPattern;
import escape.util.PieceTypeDescriptor;


abstract class Movement { 
	//TODO: javadoc
	abstract List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager);
	abstract String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance);
	boolean pathExists(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor, EscapeGameManagerImpl manager) {
		return manager.omniPath(source, target, descriptor);
	}


	private static final Movement ORTHOGONAL = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();
			int d = jump ? 2 : 1;
			int x = coordinate.getX();
			int y = coordinate.getY();
			
			neighbours.add(manager.makeCoordinate(x + d, y));
			neighbours.add(manager.makeCoordinate(x - d, y));
			neighbours.add(manager.makeCoordinate(x, y + d));
			neighbours.add(manager.makeCoordinate(x, y - d)); 
			
			return neighbours;
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return Math.abs(target.getX() - source.getX()) + Math.abs(target.getY() - source.getY()) > maxDistance 
				? "Invalid move: Target is too far away" 
				: null;
		}
	};


	private static final Movement DIAGONAL = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();
			int d = jump ? 2 : 1;
			int x = coordinate.getX();
			int y = coordinate.getY();

			neighbours.add(manager.makeCoordinate(x + d, y + d));
			neighbours.add(manager.makeCoordinate(x + d, y - d));
			neighbours.add(manager.makeCoordinate(x - d, y + d));
			neighbours.add(manager.makeCoordinate(x - d, y - d));

			return neighbours;
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return (target.getX() + target.getY()) % 2 != (source.getX() + source.getY()) % 2
				? "Invalid move: DIAGONAL pieces can't move that way" 
				: ((Math.abs(target.getX() - source.getX()) > maxDistance || Math.abs(target.getY() - source.getY()) > maxDistance) 
					? "Invalid move: Target is too far away" 
					: null);
		}
	};


	private static final Movement OMNI = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			List<EscapeCoordinate> neighbours = new LinkedList<EscapeCoordinate>();

			if (coordinate.coordinateType == CoordinateType.SQUARE) {
				neighbours.addAll(ORTHOGONAL.getNeighbours(coordinate, jump, manager));
				neighbours.addAll(DIAGONAL.getNeighbours(coordinate, jump, manager));
			} else if (coordinate.coordinateType == CoordinateType.TRIANGLE) { //Currently only works for triangle
				int d = jump ? 2 : 1;
				int x = coordinate.getX();
				int y = coordinate.getY();
				if (jump) { //we're getting jump neighbours
					neighbours.add(manager.makeCoordinate(x + 1, y + 1));
					neighbours.add(manager.makeCoordinate(x - 1, y + 1));
					neighbours.add(manager.makeCoordinate(x + 1, y - 1));
					neighbours.add(manager.makeCoordinate(x - 1, y - 1));
				} else if ((x + y) % 2 == 0) { //points down
					neighbours.add(manager.makeCoordinate(x + 1, y));
				} else {
					neighbours.add(manager.makeCoordinate(x - 1, y)); //points up
				}
	
				neighbours.add(manager.makeCoordinate(x, y - d));
				neighbours.add(manager.makeCoordinate(x, y + d));
			} else {
				throw new EscapeException("NOT IMPLEMENTED");
			}

			return neighbours;
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return source.DistanceTo(target) > maxDistance 
				? "Invalid move: Target is too far away"
				: null;
		}
	};


	private static final Movement LINEAR = new Movement() {
		List<EscapeCoordinate> getNeighbours(EscapeCoordinate coordinate, boolean jump, EscapeGameManagerImpl manager) {
			throw new EscapeException("Linear doesn't have neighbours");
		}

		String validateUnbounded(EscapeCoordinate source, EscapeCoordinate target, int maxDistance) {
			return source.DistanceTo(target) > maxDistance 
				? "Invalid move: Target is too far away"
				: ((Math.abs(target.getX() - source.getX()) != Math.abs(target.getY() - source.getY()) 
					&& target.getX() - source.getX() != 0 && target.getY() - source.getY() != 0) 
					? "Invalid move: LINEAR pieces can't move that way"
					: null);
		}

		@Override
		boolean pathExists(EscapeCoordinate source, EscapeCoordinate target, PieceTypeDescriptor descriptor, EscapeGameManagerImpl manager) {
			return manager.linearPath(source, target, descriptor);
		}
	};




	static final ImmutableMap<MovementPattern, Movement> movementTypes = ImmutableMap.<MovementPattern, Movement>builder()
		.put(MovementPattern.ORTHOGONAL, ORTHOGONAL)
		.put(MovementPattern.DIAGONAL, DIAGONAL)
		.put(MovementPattern.OMNI, OMNI)
		.put(MovementPattern.LINEAR, LINEAR)
		.build();

}
