from fileinput import FileInput

from day20a import MaybeImage, Tile, find_solution, load_all_tiles

MONSTER_WINDOW_HEIGHT = 3
MONSTER_WINDOW_WIDTH = 20
MONSTER_POSITIONS = [
    (0, 18), (1, 0), (1, 5), (1, 6), (1, 11), (1, 12), (1, 17), (1, 18),
    (1, 19), (2, 1), (2, 4), (2, 7), (2, 10), (2, 13), (2, 16),
]


def combine_image(img: MaybeImage) -> Tile:
    rows: list[str] = []

    for row_of_tiles in img.rows:
        assert row_of_tiles[0]
        tile_row_count = len(row_of_tiles[0].rows)

        for tile_row in range(1, tile_row_count - 1):
            row = ""

            for tile in row_of_tiles:
                assert tile
                row += tile.rows[tile_row][1:-1]
                # row += " "

            rows.append(row)

        # rows.append("")

    return Tile(0, tuple(rows))


def find_rougness_around_monsters(tile: Tile) -> int:
    stride = len(tile.rows[0])
    result = ones = sum(row.count("#") for row in tile.rows)

    for x_offset in range(0, stride - MONSTER_WINDOW_HEIGHT + 1):
        for y_offset in range(0, stride - MONSTER_WINDOW_WIDTH + 1):
            window = [
                row[y_offset:y_offset + MONSTER_WINDOW_WIDTH]
                for row in tile.rows[x_offset:x_offset + MONSTER_WINDOW_HEIGHT]
            ]

            # Check if window contains a monster
            if all(window[x][y] == "#" for (x, y) in MONSTER_POSITIONS):
                result -= len(MONSTER_POSITIONS)

    return result if ones != result else 0


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
    image = combine_image(solution)

    for transformed_img in image.possibilities():
        roughness = find_rougness_around_monsters(transformed_img)
        if roughness:
            print(roughness)
