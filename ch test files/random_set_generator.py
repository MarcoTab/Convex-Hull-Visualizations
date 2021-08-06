import random
from alive_progress import alive_bar

def make_set(filename, ran, n):
    f = open(filename, 'w')
    for _ in range(n):
        f.write(str(round(random.uniform(ran[0], ran[1]), 4)))
        f.write(',')
        f.write(str(round(random.uniform(ran[0], ran[1]), 4)))
        f.write('\n')
        yield
    f.close()

def main():
    n = int(input("Set size > "))
    ran = tuple(float(x) for x in input('Give range (0,n recommended) > ').split(","))
    filename = input("Filename > ")
    
    with alive_bar(n) as bar:
        for i in make_set(filename, ran, n):
            bar()


if __name__ == '__main__':
    main()