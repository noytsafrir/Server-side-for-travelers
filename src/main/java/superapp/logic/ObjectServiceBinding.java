package superapp.logic;

import superapp.data.ObjectEntity;

public interface ObjectServiceBinding  extends ObjectService{

	
	    void bindObjects(ObjectEntity parent, ObjectEntity child);
	    void unbindObjects(ObjectEntity parent, ObjectEntity child);
	}

