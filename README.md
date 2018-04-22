# bdh-project


## Table of Contents

1. Package structure
2. Installation guide
3. User manual

----------------------------
|   1. Package structure   |
----------------------------
The structure is partially adapted from homework 3.

bdh-project/
```
   data/                         <= location of intermediate 
       |                            results
       |- count_features.csv     <= hospital treatment counts
       |- max_chart_values       <= max values of fitness metrics
       |- min_chart_values       <= min values of fitness metrics
       |- text_features.csv      <= topic distribution features
   notebooks/                    <= location of IPython notebooks,
                                    such as the experiments
   project/                      <= location of build.properties
   sbt/                          <= sbt related files
   src/.../cse8803/clustering    <= LDA
                  /features      <= feature construction from notes
                  /ioutils       <= csvutils
                  /main          <= entry point of scala program
                  /model         <= note case class
```
-----------------------------
|   2. Installation guide   |
-----------------------------
### 2.1 Software requirements
- Docker in Local OS setup
- Python 3.x
- Python packages: numpy, scipy, pandas, matplotlib, scikit-learn, 
xgboost, jupyter notebook

### 2.3 Data requirements
- Download and put the MIMIC-3 tables in the location `data/MIMIC-III/`

---------------------------
|   3. Running the Code   |
---------------------------
### 3.1 Running scala code
The instructions are the same as for the homework sbt environment. 
Note that we changed the Scala version from 1.3.1 to 1.5.1

`sbt/sbt compile run`

### 3.2 Running the Jupyter notebooks
From the command line, run `jupyter notebook`, which starts a webserver.
Navigate in the browser to the published url, change to the directory
notebooks and open the notebooks.
