# Development Log

## Alpha

### 11/12

- Added tasks to TODO list.
- Fixed formatting
- Made some minor changes based on updates on Canvas and Slack

### 11/13

- Created `EscapeGameManagerImpl` (class)
- Created `AlphaCoordinate` (interface)
- Created `SquareCoordinate` (class extends `AlphaCoordinate`)
- Completed `DistanceTo` for `SquareCoordinate` (sufficient for Alpha)
- Completed `makeCoordinate`

### 11/15

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

### 11/16

- Refactored some functionality out of `makeCoordinate` and into their own methods
  - No reason to have massive blocks of code in `makeCoordinate` when it could be made significantly clearer by splitting functionality out to their own methods.
- Finished implementing `move`
- Refactored some functionality out of `move` and into their own methods
  - No reason to have massive blocks of code in `makeCoordinate` when it could be made significantly clearer by splitting functionality out to their own methods.

### 11/17

- Fixed some bugs resultant of specification misinterpretations
  - Removed internal `putCoordinate` function from `EscapeGameManagerImpl` because it was unnecessary and caused further bugs after adjusting to properly fit the specification.
  - `move` now returns true for moving a piece to the coordinate its already on
  - `makeCoordinate` doesn't return null for out of bounds coordinates anymore
  - `move` now works for coordinates not made by the same board
  - `DistanceTo` now works for coordinates that are out of bounds (actually already did, but actually tested for it now)
- Overrode `equals` and `hashCode` for `AlphaCoordinate`
  - this is to make it so different coordinate objects that refer to the same location and coordinate type are logically equivalent. Necessary for retreiving pieces at coordinates that are logically equivalent but aren't the same literal object
- Refactored `AlphaLocation` not to store x and y position (unnecessary after previous refactors and bug fixes)
- Changed `AlphaPiece.setPiece` to return void (returning the old piece was unnecessary because I will never need it) and created `AlphaPiece.removePiece` which removes the current piece and returns it

### 11/18

- Refactored `move`
  - Empty locations are no longer stored. Locations stored in `positions` are either a BLOCK, EXIT, or have a piece in them. This saves on memory and allows for some simplification of conditionals.
  - Other adjustments to improve performance
  - Replaced the no-input version of `getLocation` in `LocationFactory` to take an `AlphaPiece`.
    - In my implementation locations are always either an EXIT, BLOCK, or have a piece. There's no reason to create an empty CLEAR location. This allows for further performance optimizations
  - Deleted `removePiece` from `AlphaLocation` (didn't need it anymore)
- Removed some other unused code
- Moved `TwoAndOneFunction` into the `CoordinateFactory` file (not really necessary to have its own file)

## Beta

### 11/28

- Renamed `alpha` package to `game` and changed class names to remove references to Alpha.
- Made new test file for Beta
- Made and filled new TODO file for Beta
- Moving to the same space is no longer allowed (also needed to change a test from Alpha so this won't cause tests to fail wrongly)
- Removed `PieceTypeInitializer`
  - Class is unused and redundant with `PieceTypeDescriptor`
- Made `EscapePieceImpl` store a `PieceTypeDescriptor` instead of just a `PieceName` (this way I can call the attributes of the piece instead of needing to store the attributes somewhere else and reference them separately)
- Added piece initialization to `EscapeGameManagerImpl`

### 11/29

- Edited `test3.egc` for testing square movement patterns
- Edited other `egc` files to not break earlier tests as a result of assumptions from Alpha
- Made distance checks work for OMNI movement type
