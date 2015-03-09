package by.slesh.itechart.fullcontact.dao.impl;

import java.io.IOException;
import java.sql.SQLException;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.FamilyStatusDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.FamilyStatusEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 *
 */
public class FamilyStatusDaoImpl extends EntityDao<FamilyStatusEntity> implements FamilyStatusDao {
    private static final String ORDER_BY = 
	    "ORDER BY family_status_id ASC ";
    
    private static final String GET_FAMILY_STATUS_ID_BY_VALUE_QUERY = 
	    	      "\n SELECT family_status_id " 
		    + "\n FROM family_status "
		    + "\n WHERE family_status_value = ?";

    private static final String GET_ALL_QUERY = 
	    	      "\n SELECT family_status_id, family_status_value " 
		    + "\n FROM family_status ";
    
    private static final String GET_LIMIT_QUERY = 
        	    	GET_ALL_QUERY
        	    + "\n " + ORDER_BY 
        	    + "\n LIMIT ?, ?";
    
    public FamilyStatusDaoImpl() {
    }

    public FamilyStatusDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setGetIdQuery(GET_FAMILY_STATUS_ID_BY_VALUE_QUERY);
	setGetLimitQuery(GET_LIMIT_QUERY);
	setGetAllQuery(GET_ALL_QUERY);
	setReader(DaoReadersContainer.ENTITY_READER);
    }
    
    @Override
    public long deleteRange(long contactId, long[] ids) throws ClassNotFoundException, IOException, SQLException {
	throw new SQLException("not supported this operation!");
    }
}
