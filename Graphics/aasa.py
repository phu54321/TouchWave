def nPr(n, r):
    m = 1
    for i in range(n, n - r, -1):
        m *= i
    return m


def p(k):
    return 1 - nPr(365, k) / (365 ** k)


for i in range(50):
    print(i, p(i))
