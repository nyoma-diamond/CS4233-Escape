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

### 12/3

- Started on triangles
- Refactored out `TwoAndOneFunction` (replaced with `ToIntBiFunction` taking two `EscapeCoordinate`s)
- Completed TRIANGLE coordinate `DistanceTo`
- Completed everything Beta needs for triangle (I kinda forgot to keep track of things, but uhhhhhhhh it should hopefully work. Only pathfinding changes necessary were just changing how neighbours were calculated)
- Hella refactoring (primarily reducing redundant code)

## Gamma

### 12/9

- Changed BLOCKs to player2 pieces in most of my test because BLOCKs dont act how i thought they did and while they worked for testing in Alpha and Beta they wont work in Gamma.
- Created Gamma TODO
- Turns out something about my code made it work despite not following my initial assumption that if a location is stored it must be either filled, a BLOCK, or an EXIT. Changing my code to not allow capturing pieces made it *actually* follow the assumption, breaking my test for the assumption that shouldve failed but didn't because my code didn't follow the assumption. Fixed it. So now my code *actually* follows the assumption (tl;dr im stupid but my code worked before and still works now so idc)
- Changed rules in all configuration files to avoid breaking tests
- Changed `EscapeSettings` to store each possible rule as a separate variable
- Made REMOVE work so I dont break old tests for piece capture
- Fixed pathfinding so you can't jump over BLOCKs anymore
  - I thought you could jump over blocks in Beta and wrote my code based on that :/
  - MY CODE IS AWFUL AND I ABSOLUTELY HATE IT. Originally `getNeighbours` just provided the neighbours without caring for if they'd be valid moves and they'd just be filtered, except to make it so you can't jump over blocks I needed to make it filter out anything that'd require jumping over a block to get to. I wanna see if I can pull this out so the check isn't done in `getNeighbours` but I'm pretty sure to do that I'd need to completely rewrite my pathfinding.
    - Maybe I can make another predicate like `validNeighbour` or put something inside `validNeighbour` to take the filter out of `getNeighbours`?
- Completed turn limits (pretty easy, was just a matter of actually recording the turn current turn)
  - Chose to split `isGameOver` into its own function rather than store the game state. This choice was made out of convenience and for readability. This may change as observers get added.
  - Edit: changed `isGameOver` to `isInProgress` (flipping the output) to make the internals more readable
- Made it so you can't jump over exits (like you're SUPPOSED to AAAAAAAAAAAAAAAAAAA)
  - This is why I hate unclear specifications, because I have to literally UNDO and UNFACTOR code to make fixes for things that are unclear or aren't required in an immediate release. Just tell me clearly what I need instead of being vague until I need it because it just makes more work for me :_( This would have been a complete non-issue if I understood this was the case earlier because I wouldn't have written my code in the way I did.
- I THINK UNBLOCK WORKS
  - Spent like an hour trying to debug what was apparently just a single conditional I forgot existed that filtered out all non-clear spaces
- Implemented observers
  - I just have one function that notifies all observers with a provided message. Anywhere that observers need to be notified (failed move, game win, etc.) it just calls that

### 12/10

- Doing more tests with observers to make sure I have all win states covered and specific observer notifications handled.
- Implemented game end states. Code checks at the beginning of `move` to see if the game is already over (so observers can be told "{player} already won") and at the end to see if the move caused a win (so observers can be told "{player} wins").
  - There is an exception to this: for the case where a player runs out of pieces, the "{player} wins" case needs to be checked at the beginning of the move of the player with no more pieces instead of at the end of the other player's move. I strongly disagree with needing to do it this way. IMO it should the observers should be notified "{player} wins" at the *end* of the turn of the player that *didn't* run out of pieces so that because (a) you should already know the game is over then and (b) it's more consistent with the other cases (trying to implement it the way we're told to makes the code much less coherent)
- ~~My code is awful and I cannot refactor it for the life of me. I might just give up to maintain my mental health because this is genuinely making me incredibly upset. The only solution to these problems would require rewriting half of my entire codebase and given that it's Thursday and I haven't started the report yet, that's not happening.~~
  - Came back when I was less stressed and did some research and refactored a bunch of stuff, but not everything. See below
- ~~I've given up on refactoring this. My code is awful and I'm going to submit it awful. To whoever is reading this: I'm sorry. I lack the mental capacity to deal with this anymore. I just want to be done. The code passes the tests and that's good enough for me.~~
  - Came back when I was less stressed and did some research and refactored a bunch of stuff, but not everything. See below
- Changed `notifyObservers` to take an int and store all possible messages in an array which can be referenced by said integer ID. Doesn't help much but it might make it easier to go through the code without triggering a gag reflex (and also allows me to reuse some strings a bit more). Only problem is it introduces "magic numbers" but I think that factoring it out like this so I don't have random strings in the middle of my code is worth it.
  - Edit: nevermind this was a mistake I'm reverting this because now it's impossible to refactor other things because I have no idea what things refer to anymore. Gonna refactor some other first and then maybe put this back in place.
- Refactored `gameisOver` and `checkForWin` into one function (they performed almost identical checks, just sent observers different things)
- Refactored out a bunch of my conditionals in `getNeighbours` to a predicate used exclusively for filtering jump neighbours
- Refactored my checks for whether source and target coordinates are valid (not pathing, checking the actual coordinates) into its own function `validateCoordinates` (this helps readability).
- Also refactored out "as the crow flies" location validation into it's own method `validateUnbounded`
- Refactored movement pattern dependent code into the `Movement` class to simplify `EscapeGameManagerImpl` (THIS WAS A VERY LARGE REFACTOR)
  - By factoring out movement like this it allows me to greatly simplify parts of my code, relocate some things outside of `EscapeGameManagerImpl` so it's less of a god class, and make my code more variable (if there were further releases this would be great, but there aren't :/ )
