package superapp.logic;

import java.util.List;

import superapp.boundaries.command.MiniAppCommandBoundary;

public interface MiniAppCommandAsyncService extends MiniAppCommandService{

	public Object invokeCommandAsync(MiniAppCommandBoundary command); 
	
	public void deleteAllCommands(String superapp, String email);
	
	public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email, int size, int page);
	
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName, String userSuperapp, String email, int size, int page);

	
}
