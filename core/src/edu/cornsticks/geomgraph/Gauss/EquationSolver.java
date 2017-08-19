package edu.cornsticks.geomgraph.Gauss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EquationSolver {

	public static List<Float> solveSystem(ArrayList<ArrayList<Float>> system)
	{
		LinearSystem<Float, MyEquation> list = new LinearSystem<Float, MyEquation>();
		for (List<Float> line : system){
			MyEquation eq = new MyEquation();
			eq.setEquation(line);
			list.push(eq);
		}

		Algorithm<Float, MyEquation> alg = new Algorithm<Float, MyEquation>(list);
		alg.calculate();

		Float [] x = new Float[system.size()];
		int i, j;
		for(i = list.size() - 1; i >= 0; i--) {
			Float sum = 0.0f;
			for(j = list.size() - 1; j > i; j--) {
				sum += list.itemAt(i, j) * x[j];
			}
			x[i] = (list.itemAt(i, list.size()) - sum) / list.itemAt(i, j);
		}
		return new ArrayList<Float>(Arrays.asList(x));
	}
}
