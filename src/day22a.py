from collections import deque
from fileinput import FileInput

import core


def score(d: deque[int]) -> int:
    return sum(a * b for (a, b) in enumerate(reversed(d), 1))


def run(p1: deque[int], p2: deque[int]) -> int:
    while p1 and p2:
        c1 = p1.popleft()
        c2 = p2.popleft()

        if c1 > c2:
            p1.append(c1)
            p1.append(c2)
        elif c1 == c2:
            assert False
        else:
            p2.append(c2)
            p2.append(c1)

    winner = p1 or p2
    return score(winner)


if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    player1_lines, player2_lines = core.split_on((i.strip() for i in input), core.blank_str)
    deque1 = deque(map(int, player1_lines[1:]))
    deque2 = deque(map(int, player2_lines[1:]))
    print(run(deque1, deque2))
