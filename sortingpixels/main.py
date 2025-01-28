import matplotlib.pyplot as plt
import numpy as np

algorithms = ['Merge Sort', 'Counting Sort', 'Bucket Sort', 'Shell Sort']
bucket_sort = [
    (66858288, 30143800), (61379645, 31267000), (62620782, 30330900),
    (64671329, 30289800), (62274592, 30631600), (128808909, 37344700),
    (92155930, 44722000), (76537614, 36936600), (85390363, 35742300)
]

shell_sort = [
    (8487730, 4851800), (5402725, 4830700), (5370021, 4823200),
    (5275083, 4735800), (5479478, 4798300), (8317641, 4995100),
    (7365807, 6007500), (7005478, 4809400), (9792540, 5869600)
]

merge_sort = [
    (46854279, 42492400), (47954893, 42857400), (46197209, 42244900),
    (47240710, 43053000), (46704734, 42199500), (75423866, 41582700),
    (44775900, 40397800), (70492974, 42571700), (57772806, 44722600)
]

counting_sort = [
    (5352968, 4050000), (4668226, 4038600), (5220458, 4060000),
    (5003977, 3939400), (4615290, 3892900), (8375746, 5700000),
    (4692476, 3830000), (6994411, 4419200), (9014482, 5032600)
]
average_times = [0,0,0,0]
average_times[0] = np.mean([data[0] for data in bucket_sort])
average_times[1] = np.mean([data[0] for data in counting_sort])
average_times[2] = np.mean([data[0] for data in bucket_sort])
average_times[3] = np.mean([data[0] for data in shell_sort])


best_times = [0,0,0,0]
best_times[0] = np.min([data[1] for data in bucket_sort])
best_times[1] = np.min([data[1] for data in counting_sort])
best_times[2] = np.min([data[1] for data in bucket_sort])
best_times[3] = np.min([data[1] for data in shell_sort])

y_pos = np.arange(len(algorithms))

plt.figure(figsize=(10, 6))

plt.barh(y_pos, average_times, color='lightblue', label='Average Time')
plt.barh(y_pos, best_times, color='orange', label='Best Time')

plt.yticks(y_pos, algorithms)
plt.xlabel('Time (ns)')
plt.title('Sorting Algorithm Performance (Average vs Best Time)')
plt.legend()

plt.tight_layout()
plt.show()
