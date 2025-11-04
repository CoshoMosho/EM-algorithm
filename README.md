# EM Project – Gaussian Mixture Models

> **Date:** 🟢 **november 4th, 2025**

---

## 📖 General Description

This project implements the **Expectation-Maximization (EM) algorithm** for **multivariate Gaussian mixture models**. The implementation supports both **hard assignment** (where each data point is fully assigned to a single Gaussian) and **soft assignment** (where points have probabilistic membership across multiple Gaussians).

The goal is to provide a **comprehensive understanding** of how the EM algorithm works with multivariate distributions, including parameter estimation, cluster assignment, and convergence behavior.

---

## 🚀 What the Program Does

### Core Functionality
- **Generates synthetic data** from multiple multivariate Gaussian distributions with customizable parameters
- **Implements the complete EM algorithm** for Gaussian mixture models:
  - **E-step**: Computes responsibilities (probabilistic cluster assignments)
  - **M-step**: Updates means, covariance matrices, and mixture weights
- **Automatically estimates** the parameters of the underlying Gaussian distributions
- **Assigns points to clusters** based on maximum likelihood

---

## ⚙️ Technical Details

### Language
- **Java 8+** (JavaSE-1.8 compatible)

### External Libraries
- **Apache Commons Math 4.x** – Multivariate normal distributions and linear algebra operations
- **Apache Commons RNG** – High-quality random number generation
- **Apache Commons Numbers** – Core numerical utilities (dependency for Math 4.x)
- **Apache Commons statistics-distributions** – more statistic libraries


## 🎯 Key Components

### Data Generation 
- Creates synthetic datasets from multiple multivariate Gaussians
- Configurable number of clusters, dimensions, and points
- Balanced or random point distribution across clusters

### EM Algorithm 
- **Initialization**: Random style parameter initialization
- **E-step**: Responsibility computation using multivariate normal densities
- **M-step**: Maximum likelihood parameter updates
- **Convergence**: Log-likelihood based stopping criteria

### Results Analysis 
- Cluster assignments and probabilistic responsibilities
- Estimated distribution parameters (means, covariances, weights)
- Convergence statistics and model evaluation metrics
