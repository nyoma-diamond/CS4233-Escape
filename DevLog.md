# Development Log

## 11/12

- Added tasks to TODO list.
- Fixed formatting
- Made some minor changes based on updates on Canvas and Slack

## 11/13

- Created `EscapeGameManagerImpl` (class)
- Created `AlphaCoordinate` (interface)
- Created `SquareCoordinate` (class extends `AlphaCoordinate`)
- Completed `DistanceTo` for `SquareCoordinate` (sufficient for Alpha)
- Completed `makeCoordinate`

## 11/15

- Created `CoordinateFactory` (class)
- Changed `EscapeGameMnagerImpl` to use a factory for `makeCoordinate`
- Refactored `AlphaCoordinate` to use lambdas
  - Created `TwoAndOneFunction` (interface)
    - Functional interface for function that takes two inputs of the same time and one of a different type and returns another different type.
  - Deleted `SquareCoordinate`
  - Made `AlphaCoordinate` store coordinate type.
  - This way I don't need a separate class for every type of coordinate and can just use lambdas, which can be reused. Might change this in the future because it's kinda jank, but for the time being it's simpler.
- Created `AlphaPiece` (class extends `EscapePiece`)
- Created `AlphaLocation` (class)
- Created `LocationFactory` (class)
  - This is dependent on both `AlphaLocation` and `AlphaPiece` because it generates a location containing a piece if provided one by the location initializer
- Made `EscapeGameManagerImpl` store locations
- Made `EscapeGameManagerImpl` use a `HashMap` to associate coordinates with locations.
- Finished implementing `getPieceAt`

## 11/16

- Refactored some functionality out of `makeCoordinate` and into their own methods
  - No reason to have massive blocks of code in `makeCoordinate` when it could be made significantly clearer by splitting functionality out to their own methods.
- Finished implementing `move`
- Refactored some functionality out of `move` and into their own methods
  - No reason to have massive blocks of code in `makeCoordinate` when it could be made significantly clearer by splitting functionality out to their own methods.

## 11/17

- Fixed some bugs resultant of specification misinterpretations
  - Removed internal `putCoordinate` function from `EscapeGameManagerImpl` because it was unnecessary and caused further bugs after adjusting to properly fit the specification.
  - `move` now returns true for moving a piece to the coordinate its already on
  - `makeCoordinate` doesn't return null for out of bounds coordinates anymore
  - `move` now works for coordinates not made by the same board
  - `DistanceTo` now works for coordinates that are out of bounds (actually already did, but actually tested for it now)
- Overrode `equals` and `hashCode` for `AlphaCoordinate`
  - this is to make it so different coordinate objects that refer to the same location and coordinate type are logically equivalent.
- Refactored `AlphaLocation` not to store x and y position (unnecessary after previous refactors and bug fixes)
