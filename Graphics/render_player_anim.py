import math
import cairo
from colorsys import hsv_to_rgb

# draw player

surface = cairo.ImageSurface(cairo.FORMAT_ARGB32, 72 * 8, 72 * 5)
ctx = cairo.Context(surface)
ctx.scale(24, 24)


def drawPlayerArc(ctx, r0, r1, r, g, b, a, startangle, endangle):
    ctx.arc(
        1.5, 1.5,
        r1,
        -math.pi / 2 + math.radians(startangle),
        -math.pi / 2 + math.radians(endangle),
    )
    ctx.arc_negative(
        1.5, 1.5,
        r0,
        -math.pi / 2 + math.radians(endangle),
        -math.pi / 2 + math.radians(startangle),
    )
    ctx.close_path()
    ctx.set_source_rgba(r, g, b, a)
    ctx.fill()


frame = 0
currentProgress = 0
outframes = 0


def startFrame():
    global outframes
    if frame % 2 == 0:
        ctx.rectangle(0, 0, 3, 3)
        ctx.set_source_rgb(1, 1, 1)
        ctx.fill()
        return True

    else:
        return False


def endFrame():
    global frame, outframes
    frame += 1

    if frame % 2 == 1:
        outframes += 1
        if outframes == 8:
            ctx.translate(-21, 3)
            outframes = 0
        else:
            ctx.translate(3, 0)


def fadeIn(start, end, t):
    if 0 <= t <= 1:
        ratio = math.sin(t * (math.pi / 2))
        return start + (end - start) * ratio
    else:
        return end

# fade in
for i in range(20):
    if startFrame():
        drawPlayerArc(
            ctx,
            1.1, 1.5,  # fadeIn(0.6, 0.9, i / 20),
            .85, .37, .16, fadeIn(0, .43, i / 20),
            0, currentProgress
        )
        drawPlayerArc(
            ctx,
            0, 0.32,
            .94, .26, .26, fadeIn(1, 0.65, i / 20),
            0, 360
        )
    currentProgress += 2
    endFrame()

for i in range(40):
    if startFrame():
        drawPlayerArc(
            ctx,
            1.1, 1.5,
            .85, .37, .16, .43,
            0, currentProgress
        )
        drawPlayerArc(
            ctx,
            0, 0.32,
            .94, .26, .26, .65,
            0, 360
        )
    currentProgress += 2
    endFrame()

# Cancle invincibility at this time

for i in range(20):
    if startFrame():
        drawPlayerArc(
            ctx,
            1.1, 1.5,  # fadeIn(0.9, 0.6, i / 20),
            .85, .37, .16, fadeIn(0.43, 0, i/20),
            0, currentProgress
        )
        drawPlayerArc(
            ctx,
            0, 0.32,
            .94, .26, .26, fadeIn(0.65, 1, i / 20),
            0, 360
        )
    endFrame()


surface.write_to_png("output2.png")
