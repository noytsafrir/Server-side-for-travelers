package superapp.logic;

import superapp.boundaries.command.MiniAppCommandBoundary;

public interface MiniAppCommandAsyncService extends MiniAppCommandService{

	public Object invokeCommandAsync(MiniAppCommandBoundary command); 

	
}
