package superapp.miniapps;

import superapp.boundaries.command.MiniAppCommandBoundary;

public interface MiniappInterface {

	public Object activateCommand(MiniAppCommandBoundary miniappCommandBoundary);
}
