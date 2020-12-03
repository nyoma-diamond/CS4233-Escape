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

- Edited `test2.egc` and `test3.egc` for testing square movement patterns
- Edited other `egc` files to not break earlier tests as a result of assumptions from Alpha
- Made distance checks work for `OMNI` movement pattern
- Implemented `LINEAR` movement pattern (FLY)

### 12/1

- Refactored some `PieceTypeDescriptor`s our of `EscapePieceImpl` to maintain SRP
- Changed `egc` files to use FLY attribute instead of DISTANCE because FLY cannot be blocked by pieces along the path (test pathfinding around pieces/blocks with DISTANCE later)
- Completed Orthogonal FLY movement
- Completed Diagonal FLY movement
- Changed `test3.egc` to be used for testing the DISTANCE attribute
- Completed Omni DISTANCE movement
- Completed Linear DISTANCE movement

### 12/2

- Reorganized TODO
- Completed Orthogonal DISTANCE movement
- Completed Diagonal DISTANCE movement
- Refactored some tests to be automated instead of needing to type them out every single time
- Created `test4.egc` for testing JUMP attribute
- Implemented JUMP for linear
- Gonna stop logging `egc` changes because I keep making them and I'm gonna start making new files for more cases because trying to fit things into single boards is too difficult
- Rewrote pathfinding to work with jumps (and possibly unintentionally made it work for all movement patterns)
- Implemented JUMP for ortho
- Implemented JUMP for omni (I did unintentionally make the algorithm work for things I haven't tested yet)
- **This algorithm is smarter than I am. I keep screwing up my tests and my code is calling me out on it**
- tmw you forget you wrote code and tests for something and then write new tests :^) (wrote more tests for ortho jumps)
- Implemented JUMP for diag
- Decision: not testing FLY + JUMP because (a) the code is visibly written in a way that means they won't conflict (JUMP code is handled in pathfinding, which doesn't run if the piece can FLY) and (b) it isn't worth my sanity and it's 4am and my sanity is worth more than that.
- Decision: Ignoring UNBLOCK and VALUE for beta because as far as I know they aren't being tested (please don't be tested I don't even know how they could possibly be used in Beta).

**PATHFINDING ALGORITHM EXPLANATION:**

The pathfinding algorithm is used to ensure that a valid path exists between a source coordinate and a target coordinate for a piece that cannot fly (but may or may not be able to jump). It is specifically used for OMNI, ORTHOGONAL, and DIAGONAL movement patterns (LINEAR is a special case with its own code). It is a modified depth-limited breadth-first search. Rather than storing a full queue of all the nodes to check, it stores three separate queues:

- `curLayer`: Nodes in the "layer" closest to the starting node (so the cost of getting to any node in `curLayer` is equal and the smallest of all unvisited nodes)
- `nextLayer`: Nodes in the "layer" immediately after `curLayer` (so the cost of getting to any node in `nextLayer` is one more than the cost of getting to any node in `curLayer`)
- `jumpLayer`: Nodes in the "layer" immediately after `nextLayer` **that require jumping to** (so the cost of getting to any node in `jumpLayer` is *two* more than the cost of getting to any node in `curLayer`)

The algorithm starts with `curLayer` containing only the source node and a stored `distance` of 0.

As long as the `distance` from the source is less than the maximum allowed distance for the piece the algorithm will do the following:

- Take the front-most node in `curLayer` out and store it
- Check if the node is the target node (if it is, return true)
- Add the node to the list of `visited` nodes
- if the corresponding location is empty (or the source node):
  - Get all the valid neighbours (clear spaces, exits, or spaces with enemy pieces that haven't been visited or aren't already queued) of the node and add them to `nextLayer`
  - If the piece can jump, get all valid jump neighbours (clear spaces, exits, or spaces with enemy pieces that haven't been visited or aren't already queued *that must be jumped to*) of the node and add them to `jumpLayer`
- if `curLayer` is empty (no more nodes at this distance):
  - if there are nodes in `nextLayer`:
    - Empty `nextLayer` into `curLayer`
    - Increment `distance` by one
    - If the piece can jump, empty `jumpLayer` into `nextLayer` (note: `jumpLayer` needs to be filtered again to make sure it doesn't contain any nodes that were in `nextLayer` because it is possible a node is added to `jumpLayer` before a closer path is found and added to `nextLayer`)
  - else if the piece can jump: (This is needed for the case where the piece can only access further nodes via jumping, such as if the piece is completely surrounded by BLOCKs)
    - Empty `jumpLayer` into `curLayer`
    - Increment `distance` by *two*

If this completes without returning true (finding a path) it will return false, indicating no path found.
