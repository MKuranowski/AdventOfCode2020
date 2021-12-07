from fileinput import FileInput

import core

Deck = tuple[int, ...]
State = tuple[Deck, Deck]


def score(d: Deck) -> int:
    return sum(a * b for (a, b) in enumerate(reversed(d), 1))


def run(p1: Deck, p2: Deck) -> int:
    states: set[State] = set()

    while p1 and p2:
        c1, p1 = p1[0], p1[1:]
        c2, p2 = p2[0], p2[1:]

        if (p1, p2) in states:
            return score(p1)
        else:
            states.add((p1, p2))

        if len(p1) >= c1 and len(p2) >= c2:
            winner_score = run(p1[:c1], p2[:c2])
            if winner_score > 0:
                p1 = p1 + (c1, c2)
            else:
                p2 = p2 + (c2, c1)

        elif c1 > c2:
            p1 = p1 + (c1, c2)
        elif c1 == c2:
            assert False, "draw"
        else:
            p2 = p2 + (c2, c1)

    winner = p1 or p2
    return score(winner) * (1 if winner is p1 else -1)


if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    player1_lines, player2_lines = core.split_on((i.strip() for i in input), core.blank_str)
    deck1 = tuple(map(int, player1_lines[1:]))
    deck2 = tuple(map(int, player2_lines[1:]))
    print(run(deck1, deck2))
