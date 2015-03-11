package by.slesh.itechart.fullcontact.dao.impl;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.SexDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.SexEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 5, 2015
 *
 */
public class SexDaoImpl extends EntityDao<SexEntity> implements SexDao {
    private static final String ORDER_BY = 
	    "ORDER BY sex_id ASC ";
    
    private static final String GET_SEX_ID_BY_VALUE_QUERY = 
	    	      "\n SELECT sex_id "
		    + "\n FROM sexes " 
		    + "\n WHERE sex_value = ?";
    
    private static final String GET_ALL_QUERY = 
	    	      "\n SELECT sex_id, sex_value"
		    + "\n FROM sexes";

    private static final String GET_LIMIT_QUERY = 
	    		GET_ALL_QUERY 
	    		+ "\n" + ORDER_BY
	    		+ "\n LIMIT ?, ?";
    
    
    public SexDaoImpl() {
    }
    
    public SexDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setGetIdQuery(GET_SEX_ID_BY_VALUE_QUERY);
	setGetAllQuery(GET_ALL_QUERY);
	setGetLimitQuery(GET_LIMIT_QUERY);
	setReader(DaoReadersContainer.ENTITY_READER);
    }
	}
