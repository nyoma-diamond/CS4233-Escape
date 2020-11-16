# Development Log

## 11/12

- Added tasks to TODO list.
- Fixed formatting
- Made some minor changes based on updates on Canvas and Slack

## 11/13

- Created EscapeGameManagerImpl (class)
- Created AlphaCoordinate (interface)
- Created SquareCoordinate (class extends AlphaCoordinate)
- Completed DistanceTo for SquareCoordinate (sufficient for Alpha)
- Completed makeCoordinate

## 11/15

- Created CoordinateFactory (class)
- Changed EscapeGameMnagerImpl to use a factory for makeCoordinate
- Refactored AlphaCoordinate to use lambdas
  - Created TwoAndOneFunction (interface)
  - Deleted SquareCoordinate
  - Made AlphaCoordinate store coordinate type.
  - This way I don't need a separate class for every type of coordinate and can just use lambdas, which can be reused.
- Created AlphaPiece (class extends EscapePiece)
