import math
import cairo
from colorsys import hsv_to_rgb

# draw player

basesize = 32
ss_w = 8
ss_h = 9


def createSurfaceContext(w, h):
    surface = cairo.ImageSurface(
        cairo.FORMAT_ARGB32,
        (int)(basesize * w + 0.5),
        (int)(basesize * h + 0.5)
    )
    ctx = cairo.Context(surface)
    ctx.scale(basesize, basesize)
    return surface, ctx


def ExportSurface(surf, fname):
    surf.write_to_png(fname)


def drawPlayerArc(ctx, r0, r1, r, g, b, a, startangle, endangle):
    ctx.arc(
        cx, cy,
        r1,
        -math.pi / 2 + math.radians(startangle),
        -math.pi / 2 + math.radians(endangle),
    )
    ctx.arc_negative(
        cx, cy,
        r0,
        -math.pi / 2 + math.radians(endangle),
        -math.pi / 2 + math.radians(startangle),
    )
    ctx.close_path()
    ctx.set_source_rgba(r, g, b, a)
    ctx.fill()


cx, cy = 0, 0
surface, ctx, fname = None, None, None
frame = 0
currentProgress = 0
outframes = 0


def startFrame(lfname, w, h):
    global surface, ctx, fname, cx, cy
    surface, ctx = createSurfaceContext(w, h)
    fname = "img\\%s.png" % lfname
    cx, cy = w / 2, h / 2
    return True


def endFrame():
    global surface, ctx, fname
    ExportSurface(surface, fname)
    surface = ctx = fname = None


# General circle image

startFrame("circle_general", 4, 4)
drawPlayerArc(
    ctx,
    0, 2,
    1, 1, 1, 1,
    0, 360
)
drawPlayerArc(
    ctx,
    1.85, 2,
    0, 0, 0, 1,
    0, 360
)
endFrame()

# Countdown bar

da = 3
for angle in range(0, 360 + da, da):
    startFrame('countdown_a%d' % angle, 3, 3)
    drawPlayerArc(
        ctx,
        1.4, 1.5,
        .85, .37, .16, 1,
        0, angle
    )
    endFrame()


# Text
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

basesize = 64

startFrame("roundbutton", 4, 1)
ctx.set_source_rgba(0, 0, 0, 1)
ctx.rectangle(0, 0, 4, 1)
ctx.fill()
ctx.set_source_rgba(1, 1, 1, 1)
ctx.rectangle(0.03, 0.03, 3.94, 0.94)
ctx.fill()
endFrame()


basesize = 64
startFrame("roundbox", 4, 4)
ctx.set_source_rgba(0, 0, 0, 1)
ctx.rectangle(0, 0, 4, 4)
ctx.fill()
ctx.set_source_rgba(1, 1, 1, 1)
ctx.rectangle(0.03, 0.03, 3.94, 3.94)
ctx.fill()
endFrame()
