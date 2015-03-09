package by.slesh.itechart.fullcontact.dao.impl;

import by.slesh.itechart.fullcontact.dao.EntityDao;
import by.slesh.itechart.fullcontact.dao.PhoneTypeDao;
import by.slesh.itechart.fullcontact.dao.reader.DaoReadersContainer;
import by.slesh.itechart.fullcontact.domain.PhoneTypeEntity;

/**
 * @author Eugene Putsykovich(slesh) Mar 9, 2015
 *
 */
public class PhoneTypeDaoImpl extends EntityDao<PhoneTypeEntity> implements PhoneTypeDao {
    private static final String ORDER_BY = 
	    "ORDER BY phone_type_id ASC ";
    
    private static final String GET_PHONE_TYPE_ID_BY_VALUE_QUERY = 
	    	      "\n SELECT phone_type_id " 
		    + "\n FROM phones_types "
		    + "\n WHERE phone_type_value = ?";

    private static final String GET_ALL_PHONES_TYPES_QUERY =
        	      "\n SELECT phone_type_id, phone_type_value "
        	    + "\n FROM phones_types ";
    
    private static final String GET_LIMIT_QUERY = 
	    GET_ALL_PHONES_TYPES_QUERY
	    + "\n " + ORDER_BY 
	    + "\n LIMIT ?, ?";
    
    public PhoneTypeDaoImpl() {
    }

    public PhoneTypeDaoImpl(boolean isUseCurrentConnection, boolean isCloseConnectionAfterWork) {
	super(isUseCurrentConnection, isCloseConnectionAfterWork);
	setGetIdQuery(GET_PHONE_TYPE_ID_BY_VALUE_QUERY);
	setGetAllQuery(GET_ALL_PHONES_TYPES_QUERY);
	setGetLimitQuery(GET_LIMIT_QUERY);
	setReader(DaoReadersContainer.ENTITY_READER);
    }
}
