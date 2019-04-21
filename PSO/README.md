# PSO

This is a simple Java PSO implementation developed for an academic project.

There is a lot of room for improvement and probably bugs fixing.

It was implemented based on this article:

D. Bratton and J. Kennedy, "Defining a Standard for Particle Swarm Optimization," 2007 IEEE Swarm Intelligence Symposium, Honolulu, HI, 2007, pp. 120-127.
doi: 10.1109/SIS.2007.368035

with some extra parameters.

It implements:
-3 fitness functions (Sphere, Rastrigin and Rosenbrock);
-3 topologies (global, focal and ring);
-W inertia factor;
-W decay;
-Clerc constriction factor.

And it allows user to specify some parameters:
-Number of particles;
-Number of dimensions;
-Dimension bounds;
-Number of iterations;
-Particle velocity limitation;
-Particle position limitation.

User can also export simulation results to CSV files.

If you find any bug or want to suggest any improvement, please contact me at mailto:caiomelqui@gmail.com

## Authors

* **Caio Melquiades** - *Initial work* - [CaioSobreira](https://github.com/CaioSobreira)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.
