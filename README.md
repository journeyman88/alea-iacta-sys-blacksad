# alea-iacta-sys-blacksad
A RPG system module for Alea Iacta Est implementing Blacksad RPG

## Description
This command will roll a minimum of 6d6 combining the Action dice, the Tension dice and the Complimentary dice, calculating for each types the successes generated and the failure that erase succeses.

### Roll modifiers
Passing these parameters, the associated modifier will be enabled:

* `-v` : Will enable a more verbose mode that will show a detailed version of every result obtained in the roll.

## Help print
```
Blacksad RPG [ blacksad | sad ]

Usage: sad -a <actionDice> !alea sad -a <actionDice> -t <tensionDice>

Description:
This command will roll a minimum of 6d6 combining the Action
dice, the Tension dice and the Complimentary dice,
calculating for each types the successes generated and the
failure that erase succeses.

Options:
  -a, --action=actionDice   Number of action dice to roll
  -t, --tension=tensionDice Number of tension dice to roll
  -h, --help                Print the command help
  -v, --verbose             Enable verbose output

```
