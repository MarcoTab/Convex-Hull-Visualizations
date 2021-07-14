import pandas as pd
fn = input("Gib file nam > ")

f = pd.read_csv(fn)
second = False
fn = input("Gib nu file nam > ")
o = open(fn, "w")
for val in f:
    if not second:
        o.write(val)
        o.write(",")
        second = True
    else:
        o.write(val)
        o.write("\n")
        second = False
