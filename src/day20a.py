import re
from dataclasses import dataclass
from fileinput import FileInput
from typing import Iterable, NamedTuple, Optional

import core

Spot = tuple[int, int]


class SearchState(NamedTuple):
    image: "MaybeImage"
    spots_to_fill: set[Spot]
    available_tiles: frozenset[int]


@dataclass(frozen=True)
class Tile:
    id: int
    rows: tuple[str, ...]

    @property
    def cols(self) -> tuple[str, ...]:
        return tuple(self.get_col(idx) for idx, _ in enumerate(self.rows))

    def get_row(self, idx: int) -> str:
        return self.rows[idx]

    def get_col(self, idx: int) -> str:
        return "".join(row[idx] for row in self.rows)

    def at(self, spot: Spot) -> str:
        return self.rows[spot[0]][spot[1]]

    @classmethod
    def from_lines(cls, lines: list[str]) -> "Tile":
        id_match = re.search(r"\d+", lines[0])
        assert id_match

        return cls(
            int(id_match[0]),
            tuple(lines[1:].copy()),
        )

    def flipped_vert(self) -> "Tile":
        """
        >>> Tile(0, ["123", "456", "789"]).flipped_vert().rows
        ['321', '654', '987']
        """
        return Tile(self.id, tuple("".join(reversed(row)) for row in self.rows))

    def flipped_hor(self) -> "Tile":
        """
        >>> Tile(0, ["123", "456", "789"]).flipped_hor().rows
        ['789', '456', '123']
        """
        return Tile(self.id, tuple(reversed(self.rows)))

    def rotated_cw(self) -> "Tile":
        """
        >>> Tile(0, ["123", "456", "789"]).rotated_cw().rows
        ['741', '852', '963']
        """
        return Tile(self.id, tuple("".join(reversed(col)) for col in self.cols))

    def rotated_ccw(self) -> "Tile":
        """
        >>> Tile(0, ["123", "456", "789"]).rotated_ccw().rows
        ['369', '258', '147']
        """
        return Tile(self.id, tuple(reversed(self.cols)))

    def rotated_180(self) -> "Tile":
        """
        >>> Tile(0, ["123", "456", "789"]).rotated_180().rows
        ['987', '654', '321']
        """
        return Tile(self.id, tuple("".join(reversed(row)) for row in reversed(self.rows)))

    def possibilities(self) -> set["Tile"]:
        return {
            self,
            self.flipped_vert(),
            self.flipped_hor(),
            self.rotated_cw(),
            self.rotated_ccw(),
            self.rotated_180(),
            self.flipped_hor().rotated_cw(),
            self.flipped_hor().rotated_ccw(),
        }


@dataclass
class MaybeImage:
    stride: int
    rows: list[list[Optional[Tile]]]

    @classmethod
    def empty(cls, stride: int) -> "MaybeImage":
        return MaybeImage(stride, [[None for _ in range(stride)] for _ in range(stride)])

    def at(self, spot: Spot) -> Optional[Tile]:
        return self.rows[spot[0]][spot[1]]

    def would_match(self, spot: Spot, tile: Tile) -> bool:
        ok = True

        if ok and spot[0] > 0:
            # Up
            check_against = self.rows[spot[0] - 1][spot[1]]
            ok = check_against is None or tile.get_row(0) == check_against.get_row(-1)

        if ok and spot[0] < self.stride - 1:
            # Down
            check_against = self.rows[spot[0] + 1][spot[1]]
            ok = check_against is None or tile.get_row(-1) == check_against.get_row(0)

        if ok and spot[1] > 0:
            # Left
            check_against = self.rows[spot[0]][spot[1] - 1]
            ok = check_against is None or tile.get_col(0) == check_against.get_col(-1)

        if ok and spot[1] < self.stride - 1:
            # Right
            check_against = self.rows[spot[0]][spot[1] + 1]
            ok = check_against is None or tile.get_col(-1) == check_against.get_col(0)

        return ok

    def insert_in_copy(self, spot: Spot, tile: Tile) -> "MaybeImage":
        new = MaybeImage(self.stride, [row.copy() for row in self.rows])
        new.rows[spot[0]][spot[1]] = tile
        return new

    def tile_ids(self) -> list[list[Optional[int]]]:
        return [[(i.id if i else None) for i in row] for row in self.rows]


def next_spots(current_spot: Spot, stride: int) -> Iterable[Spot]:
    if current_spot[0] > 0:
        yield current_spot[0] - 1, current_spot[1]
    if current_spot[1] > 0:
        yield current_spot[0], current_spot[1] - 1


def find_solution(img: MaybeImage, spots_left: list[Spot], tiles_left: dict[int, Tile]) \
        -> Optional[MaybeImage]:
    if not spots_left:
        return img

    spot = spots_left[0]
    new_spots = spots_left[1:]

    for tile_id in tiles_left:
        new_tiles = tiles_left.copy()
        tile = new_tiles.pop(tile_id)

        for possible_tile in tile.possibilities():
            if not img.would_match(spot, possible_tile):
                continue

            new_img = img.insert_in_copy(spot, possible_tile)
            if (solution := find_solution(new_img, new_spots, new_tiles)):
                return solution


def load_all_tiles(input: Iterable[str]) -> dict[int, Tile]:
    return {
        tile.id: tile for tile in
        (Tile.from_lines(tile_lines) for tile_lines
         in core.split_on((i.strip() for i in input), core.empty_str))
    }


if __name__ == "__main__":
    input: "FileInput[str]" = FileInput()
    tiles = load_all_tiles(input)

    stride = int(len(tiles) ** 0.5)
    solution = find_solution(
        MaybeImage.empty(stride),
        list(reversed(list((r, c) for c in range(stride) for r in range(stride)))),
        tiles,
    )
    assert solution
    print(solution.at((0, 0)).id      # type: ignore
          * solution.at((-1, 0)).id   # type: ignore
          * solution.at((0, -1)).id   # type: ignore
          * solution.at((-1, -1)).id  # type: ignore
          )
