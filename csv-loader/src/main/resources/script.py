import sys
import pandas as pd
path = sys.argv[1]
data = pd.read_csv(path)
print(",".join([str(column_name) for column_name in list(data.columns)]))
print(len(data.values))
for row in list(data.values):
        print(",".join([str(value) for value in row]))