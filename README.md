# einstein-riddle-generator

Generates more riddles like this: https://udel.edu/~os/riddle.html

Generated riddles have more or less minimal length and are guaranteed to have exactly one solution.

## Installation

Clone the repo.

## Usage

```
$ lein run --size 4 --show-solution
Size: 4 Length: 9 Difficulty: none
Total possible combinations: 33469894423680N
Skipped 192
Skipped 195
"Elapsed time: 396.122352 msecs"

([left "cat" "snakes"]
 [same "Cubans" "steaks"]
 [same "Marlboro" "cheese"]
 [exact 2 "snakes"]
 [left "spaghetti" "pancakes"]
 [left "cheese" "Red Apple"]
 [neighbor "cheese" "German"]
 [neighbor "butterflies" "Italian"]
 [left "Irish" "Marlboro"])

Solution:
+---+---------+-------------+--------------+-----------+
|   | 1       | 2           | 3            | 4         |
+---+---------+-------------+--------------+-----------+
| 1 | Irish   | cat         | Cubans       | steaks    |
| 2 | Spanish | snakes      | Marlboro     | cheese    |
| 3 | German  | butterflies | Red Apple    | spaghetti |
| 4 | Italian | dog         | Chesterfield | pancakes  |
+---+---------+-------------+--------------+-----------+
```

## Options

* `--size N` — problem domain size, from 2 to 10.
* `--length L` — number of rules of the problem (by default some reasonable value is used).
* `--difficulty K` — number of `exact` rules. 0 is the toughest. Reasonable values 0, 1 and 2, default is ignore.
* `--show-solution` — by default the solution it printed in invisible letters. Specify this flag to avoid it.
 
## Testing

```
$ lein midje
```

## License

Copyright © 2016 Dmitrii Balakhonskii

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
