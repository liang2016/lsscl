/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.mailingList;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonValue;
import com.serotonin.json.TypeFactory;
import com.serotonin.mango.util.LocalizableJsonException;

public class EmailRecipientFactory implements TypeFactory {
    @Override
    public Class<?> getType(JsonValue jsonValue) throws JsonException {
        if (jsonValue.isNull())
            return null;

        JsonObject json = jsonValue.toJsonObject();

        String text = json.getString("recipientType");
        if (text == null)
            throw new LocalizableJsonException("emport.error.recipient.missing", "recipientType",
                    EmailRecipient.TYPE_CODES);

        int type = EmailRecipient.TYPE_CODES.getId(text);
        if (!EmailRecipient.TYPE_CODES.isValidId(type))
            throw new LocalizableJsonException("emport.error.recipient.invalid", "recipientType", text,
                    EmailRecipient.TYPE_CODES.getCodeList());

        if (type == EmailRecipient.TYPE_MAILING_LIST)
            return MailingList.class;
        if (type == EmailRecipient.TYPE_USER)
            return UserEntry.class;
        return AddressEntry.class;
    }
}
