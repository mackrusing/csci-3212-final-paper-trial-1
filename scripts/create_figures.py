import pandas
import matplotlib.pyplot as pyplot
import seaborn
import numpy

# style
seaborn.set_style("whitegrid")
pyplot.rcParams["figure.dpi"] = 300
pyplot.rcParams["font.size"] = 10

# define models and topics
models = ["anthropic", "google", "openai"]
models_display = ["Claude Sonnet 4.5", "Google Gemini 2.5 Flash", "ChatGPT-5.1"]
models_display_short = ["Claude", "Gemini", "ChatGPT"]
topics = ["bfs", "dfs"]
categories = ["Correctness", "Completeness", "Visualization & Examples"]

# read csvs
all_data = []

for model in models:
    for topic in topics:
        filename = f"grades/{model}-{topic}-01.csv"
        df = pandas.read_csv(filename)
        df["Model"] = model
        df["Topic"] = topic.upper()
        all_data.append(df)

# combine all data
combined_df = pandas.concat(all_data, ignore_index=True)

# average scores by model and category
avg_by_model = combined_df.groupby("Model")[categories].mean()

# average scores by model and topic
avg_by_model_topic = (
    combined_df.groupby(["Model", "Topic"])[categories].mean().reset_index()
)


def model_to_display(model: str):
    for i, m in enumerate(models):
        if model == m:
            return models_display[i]


def model_to_display_short(model: str):
    for i, m in enumerate(models):
        if model == m:
            return models_display_short[i]


#
# overall model performance graph
#

fig, ax = pyplot.subplots(figsize=(10, 6))
x = numpy.arange(len(categories))
width = 0.25

for i, model in enumerate(models):
    scores = avg_by_model.loc[model].values
    ax.bar(x + i * width, scores, width, label=f"{model_to_display(model)}")

ax.set_xlabel("Categories", fontweight="bold")
ax.set_ylabel("Average Score (0-2)", fontweight="bold")
ax.set_title(
    "Average Model Performance Across All Categories", fontweight="bold", fontsize=12
)
ax.set_xticks(x + width)
ax.set_xticklabels(categories, rotation=15, ha="right")
ax.set_ylim(0, 2.2)
ax.legend()
ax.grid(axis="y", alpha=0.3)
pyplot.tight_layout()
pyplot.savefig("figures/overall.png", bbox_inches="tight")

#
# heatmap
#

pivot_data = []
for model in models:
    for topic in ["BFS", "DFS"]:
        model_topic_data = combined_df[
            (combined_df["Model"] == model) & (combined_df["Topic"] == topic)
        ]
        avg_scores = model_topic_data[categories].mean()
        pivot_data.append(
            {
                "Model-Topic": f"{model_to_display_short(model)} ({topic})",
                **avg_scores.to_dict(),
            }
        )

heatmap_df = pandas.DataFrame(pivot_data)
heatmap_df = heatmap_df.set_index("Model-Topic")

fig, ax = pyplot.subplots(figsize=(8, 6))
seaborn.heatmap(
    heatmap_df,
    annot=True,
    fmt=".2f",
    cmap="YlGnBu",
    vmin=0,
    vmax=2,
    cbar_kws={"label": "Score (0-2)"},
    ax=ax,
)
ax.set_title(
    "Heatmap: Average Scores by Model and Topic", fontweight="bold", fontsize=12
)
ax.set_ylabel("Model - Topic", fontweight="bold")
ax.set_xlabel("Categories", fontweight="bold")
pyplot.tight_layout()
pyplot.savefig("figures/heatmap.png", bbox_inches="tight")

#
# stats
#

print("=" * 60)
print("Average Scores by Model")
print("=" * 60 + "\n")
print(avg_by_model.round(2))
print("\n" + "=" * 60)
print("Average Scores by Model and Topic")
print("=" * 60 + "\n")
print(avg_by_model_topic.round(2))
print("\n" + "=" * 60)
print("Overall Statistics")
print("=" * 60)
for category in categories:
    print(f"\n{category}:")
    print(f"  Mean: {combined_df[category].mean():.2f}")
    print(f"  Std:  {combined_df[category].std():.2f}")
    print(f"  Min:  {combined_df[category].min():.2f}")
    print(f"  Max:  {combined_df[category].max():.2f}")
