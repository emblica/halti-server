

package emblica.halti.solver;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import emblica.halti.domain.ServiceDistribution;


public class HaltiSolver {

  public static Solver<ServiceDistribution> createSolver(String solverConfig) {
      SolverFactory<ServiceDistribution> solverFactory = SolverFactory.createFromXmlResource(solverConfig);
      return solverFactory.buildSolver();
  }
}
