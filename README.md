#  EM Project – Univariate Gaussians 

> **Date:** 🟢 **October 31st, 2025**

---

##  General Description

This project is a **simple implementation of the Expectation–Maximization (EM)** algorithm  
for a **mixture of two univariate Gaussian distributions**, in **hard mode**  
(where each data point is fully assigned to a single Gaussian at every iteration).

The goal is **not** to write professional code,  
but rather to **understand and visualize** how the EM algorithm works step by step.

---

##  What the Program Does

- Takes or generates an input array of data points (`double[]`).
- Randomly initializes two Gaussian distributions, cluster proportions and points assignment.
- Iteratively applies:
  - **E-step (Hard assignment):** assigns each point to the Gaussian with the highest weighted probability.
  - **M-step:** updates the means and standard deviations of both Gaussians.
- Prints the evolution of parameters and the final count of points per cluster.

---

## ⚙️ Technical Details

### Language
- **JavaSE-1.8**

### External Libraries
- **Apache Commons RNG** – for random number generation  
- **XChart** *(optional)* – for visualizing points and Gaussian clusters  
