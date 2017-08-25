package edu.cornsticks.geomgraph.Figures;

/**
 * Action structure
 */

class Action {

	Figure figure;
	String action;
	String param;

	Action(Figure figure, String action, String param) {
		this.figure = figure;
		this.action = action;
		this.param = param;
	}
}
