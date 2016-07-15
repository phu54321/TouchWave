import math
import cairo


output_svg = True


class Tilemap:
    def __init__(self, mapdata):
        self.w = len(mapdata[0])
        self.h = len(mapdata)
        self.mapdata = mapdata

    def width(self):
        return self.w

    def height(self):
        return self.h

    def __getitem__(self, pos):
        x, y = pos
        try:
            return self.mapdata[y][x]
        except IndexError:
            return ' '

tilemap = Tilemap([
    '                    ',
    '    ############### ',
    '    #...........$$# ',
    '    #...........$$# ',
    ' ####...........$$# ',
    ' #@@...........#### ',
    ' #@@...........#    ',
    ' #@@...........#    ',
    ' ###############    ',
    '                    ',
])


cellSize = 32

w = tilemap.width()
h = tilemap.height()
imgw, imgh = cellSize * w, cellSize * h

if output_svg:
    surface = cairo.SVGSurface(
        "output_map.svg",
        imgw,
        imgh
    )

else:
    surface = cairo.ImageSurface(
        cairo.FORMAT_ARGB32,
        imgw,
        imgh
    )

ctx = cairo.Context(surface)
ctx.scale(cellSize, cellSize)


def isFloorTile(ch):
    return ch in '.@$'

tilemap_color = {
    ' ': (0.95, 0.97, 0.98),
    '#': (0.34, 0.34, 0.42),
    '@': (0.50, 0.89, 0.75),
    '$': (0.82, 0.67, 0.97)
}

floor_colors = [
    (0.89, 0.89, 0.89),
    (1., 1., 1.),
]

wall_thickness = 0.13

w = tilemap.width()
h = tilemap.height()
for y in range(h):
    for x in range(w):
        tile = tilemap[x, y]
        if tile == '.':
            color = floor_colors[(x ^ y) & 1]
            ctx.rectangle(x, y, 1, 1)
            ctx.set_source_rgb(*color)
            ctx.fill()

        elif tile == '#':
            # Draw background void
            ctx.set_source_rgb(*tilemap_color[' '])
            ctx.rectangle(x, y, 1, 1)
            ctx.fill()

            # Draw walls
            ctx.set_source_rgb(*tilemap_color['#'])
            dirx = [-1, 0, 1, 1, 1, 0, -1, -1]
            diry = [1, 1, 1, 0, -1, -1, -1, 0]

            dir_wallxy_table = {
                -1: 0,
                0: 0,
                1: 1 - wall_thickness,
            }

            dir_wallwh_table = {
                -1: wall_thickness,
                0: 1,
                1: wall_thickness,
            }

            for i in range(8):
                dstx = x + dirx[i]
                dsty = y + diry[i]
                if not (0 <= dstx < w and 0 <= dsty < h):
                    continue

                elif not isFloorTile(tilemap[dstx, dsty]):
                    continue

                wallx = x + dir_wallxy_table[dirx[i]]
                wally = y + dir_wallxy_table[diry[i]]
                wallw = dir_wallwh_table[dirx[i]]
                wallh = dir_wallwh_table[diry[i]]

                ctx.rectangle(
                    wallx, wally,
                    wallw, wallh
                )
                ctx.fill()

        else:
            color = tilemap_color[tile]
            ctx.rectangle(x, y, 1, 1)
            ctx.set_source_rgb(*color)
            ctx.fill()


# Opponents

opponent_color = (0.22, 0.40, 0.80, 0.84)
opponent_size = 0.27
opponents = []

for i in range(6):
    opponents.extend([
        (5.5 + 2 * i, 8.7),
        (5.5 + 2 * i, 6.7),
        (5.5 + 2 * i, 4.7),
        (5.5 + 2 * i, 2.7),

        (4.5 + 2 * i, 1.3),
        (4.5 + 2 * i, 3.3),
        (4.5 + 2 * i, 5.3),
        (4.5 + 2 * i, 7.3),
    ])

ctx.set_source_rgba(*opponent_color)
for x, y in opponents:
    ctx.arc(x, y, opponent_size, 0, 2 * math.pi)
    ctx.fill()

# draw player

player_pos_x = 8.3
player_pos_y = 4.7


def drawPlayerArc(ctx, r0, r1, r, g, b, a,
                  startangle, endangle):
    # WIDTH, HEIGHT = 40, 40  # Small circle

    ctx.move_to(player_pos_x, player_pos_y)
    ctx.arc(
        player_pos_x, player_pos_y,
        r1,
        -math.pi / 2 + math.radians(startangle),
        -math.pi / 2 + math.radians(endangle),
    )

    ctx.arc_negative(
        player_pos_x, player_pos_y,
        r0,
        -math.pi / 2 + math.radians(endangle),
        -math.pi / 2 + math.radians(startangle),
    )

    ctx.close_path()
    ctx.set_source_rgba(r, g, b, a)
    ctx.fill()

drawPlayerArc(
    ctx,
    1.1, 1.5,
    0.85, 0.37, 0.16, 0.43,
    0, 160
)

# drawPlayerArc(ctx, 1.0, 0, 0, 0.25, 0.4, 160, 230)

drawPlayerArc(
    ctx,
    0, 0.32,
    0.94, 0.26, 0.26, 0.65,
    0, 360
)
# drawPlayerArc(ctx, 0.32, 0, 0, 0.6, 0.3, 0, 210)


# Pause button
def draw_rounded_rect(x0, y0, x1, y1, r):
    ctx.move_to(x0 + r, y0)
    ctx.line_to(x1 - r, y0)
    ctx.arc(x1 - r, y0 + r, r, -math.pi / 2, 0)
    ctx.line_to(x1, y1 - r)
    ctx.arc(x1 - r, y1 - r, r, 0, math.pi / 2)
    ctx.line_to(x0 + r, y1)
    ctx.arc(x0 + r, y1 - r, r, math.pi / 2, math.pi)
    ctx.line_to(x0, y0 + r)
    ctx.arc(x0 + r, y0 + r, r, math.pi, 3 * math.pi / 2)
    ctx.close_path()

windowSmallSize = min(w, h)
size = windowSmallSize / 8
padding = size / 5
roundr = size / 4

cx = w - (size / 2) - padding
cy = padding + (size / 2)
draw_rounded_rect(cx - size/2, cy - size/2, cx + size/2, cy + size/2, roundr)
ctx.set_source_rgba(0.5, 0.5, 0.5, 0.3)
ctx.fill()
draw_rounded_rect(cx - size/2, cy - size/2, cx + size/2, cy + size/2, roundr)
ctx.set_source_rgba(0, 0, 0, 0.7)
ctx.set_line_width(0.06 * size)
ctx.stroke()
ctx.set_line_width(0.18 * size)
ctx.move_to(cx - 0.17 * size, cy - 0.28 * size)
ctx.line_to(cx - 0.17 * size, cy + 0.28 * size)
ctx.close_path()
ctx.stroke()
ctx.move_to(cx + 0.17 * size, cy - 0.28 * size)
ctx.line_to(cx + 0.17 * size, cy + 0.28 * size)
ctx.close_path()
ctx.stroke()


if output_svg:
    ctx.show_page()

else:
    surface.write_to_png('output_map.png')  # Output to PNG
